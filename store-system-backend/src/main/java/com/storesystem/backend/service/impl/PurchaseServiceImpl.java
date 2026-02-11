package com.storesystem.backend.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.model.dto.purchase.CreateNewDetialDTO;
import com.storesystem.backend.model.dto.purchase.CreateNewOrderDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderDTO;
import com.storesystem.backend.model.entity.PurchaseDetail;
import com.storesystem.backend.model.entity.PurchaseOrder;
import com.storesystem.backend.model.entity.Supplier;
import com.storesystem.backend.model.enums.PurchaseStatus;
import com.storesystem.backend.repository.PurchaseDetailRepository;
import com.storesystem.backend.repository.PurchaseOrderRepository;
import com.storesystem.backend.service.PurchaseService;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EntityFetcher entityFetcher;
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@Autowired
	private PurchaseDetailRepository purchaseDetailRepository;
	
	// 建立 草稿 進貨單
	@Override
	public PurchaseOrderDTO createNewOrder(CreateNewOrderDTO dto) {
		// 1. 建立總單
		PurchaseOrder order = createOrder(dto);
		
		// 2. 建立明細
		List<PurchaseDetail> details = dto.getDetails().stream()
				.map(newDetailDTO -> createDetail(newDetailDTO, order))
				.toList();
		details = purchaseDetailRepository.saveAll(details);
		
		// 3. 統合起來回傳
		PurchaseOrderDTO purchaseOrderDTO = createFinal(order, details);
		
		return purchaseOrderDTO;
	}

	private PurchaseOrder createOrder(CreateNewOrderDTO dto) {
		Supplier supplier = entityFetcher.getSupplierById(dto.getSupplierId());
		
		String number = dto.getDate() + "test";
		
		PurchaseOrder order = new PurchaseOrder();
		order.setSupplier(supplier);
		order.setPurchaseOrderDate(dto.getDate());
		order.setStatus(PurchaseStatus.DRAFT);
		order.setTotalAmount(new BigDecimal(0));
		
		return purchaseOrderRepository.save(order);
	}

	private PurchaseDetail createDetail(CreateNewDetialDTO dto, PurchaseOrder order) {
		PurchaseDetail detail = new PurchaseDetail();
		
		return detail;
	}

	private PurchaseOrderDTO createFinal(PurchaseOrder purchaseOrder, List<PurchaseDetail> purchaseDetails) {
		// TODO Auto-generated method stub
		return null;
	}

}
