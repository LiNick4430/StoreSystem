package com.storesystem.backend.service.impl;

import java.util.HashSet;
import java.util.List;
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
import com.storesystem.backend.model.dto.purchase.UpdateOrderDTO;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.model.entity.PurchaseDetail;
import com.storesystem.backend.model.entity.PurchaseOrder;
import com.storesystem.backend.model.entity.Supplier;
import com.storesystem.backend.model.enums.PurchaseStatus;
import com.storesystem.backend.repository.PurchaseOrderRepository;
import com.storesystem.backend.repository.spec.PurchaseOrderSpec;
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
	private PurchaseOrderRepository purchaseOrderRepository;

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
		// 1. 找出該訂單
		PurchaseOrder purchaseOrder = entityFetcher.getPurchaseOrder(dto.getOrderId());
		if (!purchaseOrder.getStatus().equals(PurchaseStatus.DRAFT)) {
			throw new PurchaseOrderErrorException("訂單狀態錯誤 僅有草稿狀態可以修正明細");
		}
		
		// TODO 後續 針對 明細 做細節調整
		
		return null;
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

	// 草稿訂貨單 轉成 DTO 的 方法
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
