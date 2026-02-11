package com.storesystem.backend.service;

import java.util.Set;

import com.storesystem.backend.model.dto.purchase.PurchaseOrderDTO;
import com.storesystem.backend.model.entity.PurchaseDetail;
import com.storesystem.backend.model.entity.PurchaseOrder;

// 進貨單
public interface PurchaseService {

	// 搜尋進貨單
	
	// 建立進貨單
	// 1. 建立總單
	PurchaseOrder createOrder();
	
	// 2. 建立明細
	PurchaseDetail createDetail();
	
	// 3. 統合起來回傳
	PurchaseOrderDTO createFinalOrder(PurchaseOrder purchaseOrder, Set<PurchaseDetail> purchaseDetails);
	
	// 更新進貨單狀態
	
	// 刪除進貨單
}
