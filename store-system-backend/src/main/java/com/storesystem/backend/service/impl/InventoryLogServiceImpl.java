package com.storesystem.backend.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.inventoryLog.InventoryLogDTO;
import com.storesystem.backend.model.dto.inventoryLog.InventoryLogSearchAllDTO;
import com.storesystem.backend.model.entity.InventoryLog;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.model.entity.PurchaseDetail;
import com.storesystem.backend.model.entity.PurchaseOrder;
import com.storesystem.backend.repository.InventoryLogRepository;
import com.storesystem.backend.repository.ProductRepository;
import com.storesystem.backend.repository.spec.InventoryLogSpec;
import com.storesystem.backend.service.InventoryLogService;
import com.storesystem.backend.util.PageUtil;

@Service
@Transactional
public class InventoryLogServiceImpl implements InventoryLogService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EntityFetcher entityFetcher;
	
	@Autowired
	private InventoryLogRepository inventoryLogRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public PageDTO<InventoryLogDTO> searchAllLog(InventoryLogSearchAllDTO dto) {
		// 0. 抓取相關的 detail ID
		PurchaseOrder purchaseOrder = entityFetcher.getPurchaseOrder(dto.getOrderId());
		List<Long> detailIds = purchaseOrder.getPurchaseDetails().stream()
				.map(detail -> detail.getPurchaseDetailId())
				.toList();
		
		// 1. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(dto.getPage(), dto.getSize());

		// 2. 分類執行
		Specification<InventoryLog> spec = Specification.unrestricted();

		spec = spec.and(InventoryLogSpec.hasDetailIds(detailIds));
		
		Page<InventoryLog> page = inventoryLogRepository.findAll(spec, pageable);
		
		return PageUtil.toPageDTO(page, log -> modelMapper.map(log, InventoryLogDTO.class));
	}
	
	@Override
	public void createInventoryLog(PurchaseDetail detail, String warehouseName) {
		// 1. 抓取必要的資訊
		Product product = entityFetcher.getProductById(detail.getProduct().getProductId());
		Integer quantity = detail.getQuantity();				// 進貨數量
		Integer beforeQuantity = product.getStockQuantity();	// 入庫前數量
		Integer afterQuantity = beforeQuantity + quantity;		// 入庫後數量
		
		// 2. 修正 商品入庫後的 庫存量
		product.setStockQuantity(afterQuantity);
		Product savedProduct = productRepository.save(product);
		
		// 3. 建立新的 入庫LOG
		InventoryLog inventoryLog = new InventoryLog();
		inventoryLog.setPurchaseDetail(detail);
		inventoryLog.setProduct(savedProduct);
		inventoryLog.setQuantity(quantity);
		inventoryLog.setBeforeQuantity(beforeQuantity);
		inventoryLog.setAfterQuantity(afterQuantity);
		inventoryLog.setWarehouseName(warehouseName);
		inventoryLogRepository.save(inventoryLog);
	}

}
