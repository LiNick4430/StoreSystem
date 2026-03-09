package com.storesystem.backend.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.exception.PurchaseOrderErrorException;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.purchase.CreateNewDetialDTO;
import com.storesystem.backend.model.dto.purchase.CreateNewOrderDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseDetailDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderSearchAllDTO;
import com.storesystem.backend.model.dto.purchase.ReceivedOrderDTO;
import com.storesystem.backend.model.dto.purchase.UpdateDetialDTO;
import com.storesystem.backend.model.dto.purchase.UpdateOrderDTO;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.model.entity.PurchaseDetail;
import com.storesystem.backend.model.entity.PurchaseOrder;
import com.storesystem.backend.model.entity.Supplier;
import com.storesystem.backend.model.enums.PurchaseStatus;
import com.storesystem.backend.repository.PurchaseDetailRepository;
import com.storesystem.backend.repository.PurchaseOrderRepository;
import com.storesystem.backend.repository.spec.PurchaseOrderSpec;
import com.storesystem.backend.service.InventoryLogService;
import com.storesystem.backend.service.PurchaseService;
import com.storesystem.backend.util.NumberUtil;
import com.storesystem.backend.util.PageUtil;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService{

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EntityFetcher entityFetcher;

	@Autowired
	private NumberUtil numberUtil;

	@Autowired
	private InventoryLogService inventoryLogService;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	private PurchaseDetailRepository purchaseDetailRepository;

	// 搜尋 該供應商 的 所有進貨單
	@Override
	public PageDTO<PurchaseOrderDTO> searchAllPurchaseOrder(PurchaseOrderSearchAllDTO dto) {
		// 1. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(dto.getPage(), dto.getSize());

		// 2. 分類執行
		Specification<PurchaseOrder> spec = Specification.unrestricted();

		spec = spec.and(PurchaseOrderSpec.hasSupplierId(dto.getSupplierId()));

		if (dto.getStatus() != null) {
			spec = spec.and(PurchaseOrderSpec.hasPurchaseStatus(dto.getStatus()));
		}

		if (dto.getOrderYearMonth() != null) {
			spec = spec.and(PurchaseOrderSpec.inMonth(dto.getOrderYearMonth()));
		}

		if (dto.getProductId() != null) {
			spec = spec.and(PurchaseOrderSpec.hasProductId(dto.getProductId()));
		}

		Page<PurchaseOrder> page = purchaseOrderRepository.findAll(spec, pageable);

		return PageUtil.toPageDTO(page, order -> {
			PurchaseOrderDTO orderDTO = modelMapper.map(order, PurchaseOrderDTO.class);
			orderDTO.setDetailQty(order.getDetailCount());
			return orderDTO;
		});
	}

	// 建立 草稿 進貨單
	@Override
	public PurchaseOrderDTO createNewOrder(CreateNewOrderDTO dto) {
		// 1. 建立總單
		PurchaseOrder order = createOrder(dto);

		// 2. 建立明細 並關連
		List<PurchaseDetail> details = dto.getDetails().stream()
				.map(newDetailDTO -> createDetail(newDetailDTO, order))
				.toList();
		order.setPurchaseDetails(new HashSet<>(details));

		// 3. 計算摘要
		order.updateSummary();

		// 4. 一次性儲存
		PurchaseOrder savedOrder = purchaseOrderRepository.saveAndFlush(order);

		// 5. 轉成 DTO 回傳
		return toDTO(savedOrder);
	}

	// 更新 草稿訂單 的 明細
	@Override
	public PurchaseOrderDTO updateOrder(UpdateOrderDTO dto) {
		// 0. 檢測 DTO 中 是否有 同樣的商品ID
		long distinctCount = dto.getDetails().stream()
				.map(UpdateDetialDTO::getProductId)
				.distinct()
				.count();
		// 要是去重後的數量 小於 原本的數量 = 有重複的商品ID
		if (distinctCount < dto.getDetails().size()) {
			throw new PurchaseOrderErrorException("清單中包含有重複性的商品, 請檢查後再提交");
		}

		// 1. 找出該訂單
		PurchaseOrder purchaseOrder = entityFetcher.getPurchaseOrder(dto.getOrderId());
		if (!purchaseOrder.getStatus().equals(PurchaseStatus.DRAFT)) {
			throw new PurchaseOrderErrorException("訂單狀態錯誤 僅有草稿狀態可以修正明細");
		}
		Long orderId = purchaseOrder.getPurchaseOrderId();	// 後續使用

		// 2. 將有 detailId, 當作 KEY 放入 MAP 
		// 將沒有 detailId, 用 productId 當作 KEY 放入 MAP
		Map<Long, UpdateDetialDTO> detailDTOMapDId = dto.getDetails().stream()
				.filter(d -> d.getDetailId() != null)
				.collect(Collectors.toMap(UpdateDetialDTO::getDetailId, d -> d));

		Map<Long, UpdateDetialDTO> detailDTOMapPId = dto.getDetails().stream()
				.filter(d -> d.getDetailId() == null)
				.collect(Collectors.toMap(UpdateDetialDTO::getProductId, d -> d));

		// 3. 進行處理
		purchaseOrder.getPurchaseDetails().forEach(detail -> {
			// 有 detailId
			UpdateDetialDTO updateDetialDTODId = detailDTOMapDId.get(detail.getPurchaseDetailId());
			if (updateDetialDTODId != null) {
				updateDetail(detail, updateDetialDTODId);
				return;
			} 

			// 沒有 detailId 但是有同樣的 productId
			UpdateDetialDTO updateDetialDTOPId = detailDTOMapPId.get(detail.getProduct().getProductId());
			if (updateDetialDTOPId != null) {
				updateDetail(detail, updateDetialDTOPId);

				detailDTOMapPId.remove(updateDetialDTOPId.getProductId());	// 刪除 那一項 MAP
				return;
			}

			// 沒有 detailId 也沒有 productId
			detail.softDelete();
			return;
		});

		// 4. 將 有商品ID 沒有 detailId 也不存在有 商品ID 在 原本的 detail = 表示新增的商品
		detailDTOMapPId.values().stream().forEach(detailDTO -> {
			PurchaseDetail detail = purchaseDetailRepository.findByProductIdAndIsDelete(orderId, detailDTO.getProductId())
					.orElse(null);
			// 有已經被軟刪除 同商品ID 的 細項
			if (detail != null) {
				detail.setDeleteAt(null);

				detail.setPurchaseOrder(purchaseOrder);
				updateDetail(detail, detailDTO);

				purchaseOrder.getPurchaseDetails().add(detail);
			} else {
				// 全新的 細項
				Product product = entityFetcher.getProductById(detailDTO.getProductId());

				PurchaseDetail newDetail = new PurchaseDetail();
				newDetail.setPurchaseOrder(purchaseOrder);
				newDetail.setProduct(product);
				updateDetail(newDetail, detailDTO);

				purchaseOrder.getPurchaseDetails().add(newDetail);
			}
		});

		// 5. 回存
		purchaseOrder.updateSummary();
		PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);

		return toDTO(savedOrder);
	}

	@Override
	public PurchaseOrderDTO receivedOrder(ReceivedOrderDTO dto) {
		// 1. 找出該訂單
		PurchaseOrder purchaseOrder = entityFetcher.getPurchaseOrder(dto.getOrderId());
		if (!purchaseOrder.getStatus().equals(PurchaseStatus.DRAFT)) {
			throw new PurchaseOrderErrorException("訂單狀態錯誤 僅有草稿狀態可以簽收入庫");
		}

		// 2. 簽收入庫 同時 紀錄 庫存LOG
		purchaseOrder.getPurchaseDetails().stream()
		.filter(detail -> detail.getDeleteAt() == null)		// 預防萬一 
		.forEach(detail -> inventoryLogService.createInventoryLog(detail, dto.getName()));

		// 3. 變更狀態
		purchaseOrder.setStatus(PurchaseStatus.RECEIVED);

		// 4. 回存
		PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);
		return toDTO(savedOrder);
	}

	// 建立 草稿 進貨主單
	private PurchaseOrder createOrder(CreateNewOrderDTO dto) {
		Supplier supplier = entityFetcher.getSupplierById(dto.getSupplierId());		
		String number = numberUtil.generatePurchaseOrderNumber();

		PurchaseOrder order = new PurchaseOrder();
		order.setSupplier(supplier);
		order.setPurchaseOrderNumber(number);
		order.setPurchaseOrderDate(dto.getDate());
		order.setStatus(PurchaseStatus.DRAFT);

		return order;
	}

	// 建立 草稿 進貨細項
	private PurchaseDetail createDetail(CreateNewDetialDTO dto, PurchaseOrder order) {
		Product product = entityFetcher.getProductById(dto.getProductId());

		PurchaseDetail detail = new PurchaseDetail();		
		detail.setPurchaseOrder(order);
		detail.setProduct(product);
		detail.setQuantity(dto.getQuantity());
		detail.setCost(dto.getCost());
		detail.calculateSubtotal();	// 預先計算 小計 方便後續處理

		return detail;
	}

	// 更新 草搞 進貨細項
	private PurchaseDetail updateDetail(PurchaseDetail detail, UpdateDetialDTO dto) {
		detail.setCost(dto.getCost());
		detail.setQuantity(dto.getQuantity());
		detail.calculateSubtotal();
		return detail;
	}

	// 訂貨單 轉成 DTO 的 方法
	private PurchaseOrderDTO toDTO(PurchaseOrder order) {
		// 1. 建立 PurchaseOrderDTO
		PurchaseOrderDTO orderDTO = modelMapper.map(order, PurchaseOrderDTO.class);

		// 2. 建立 Set<PurchaseDetailDTO>
		Set<PurchaseDetailDTO> detailDTOs = order.getPurchaseDetails().stream()
				.map(detail -> modelMapper.map(detail, PurchaseDetailDTO.class))
				.collect(Collectors.toSet());

		// 3. 組合後回傳
		orderDTO.setDetails(detailDTOs);
		return orderDTO;
	}


}
