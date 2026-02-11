package com.storesystem.backend.service;

import java.util.Set;

import com.storesystem.backend.model.dto.purchase.CreateNewDetialDTO;
import com.storesystem.backend.model.dto.purchase.CreateNewOrderDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderDTO;
import com.storesystem.backend.model.entity.PurchaseDetail;
import com.storesystem.backend.model.entity.PurchaseOrder;

// 進貨單
public interface PurchaseService {

	// 搜尋進貨單
	
	// 建立進貨單(草稿)
	// 整合版 (1+2+3)
	PurchaseOrderDTO createNewOrder(CreateNewOrderDTO dto);
	
	// 1. 建立總單(私人)
	PurchaseOrder createOrder(CreateNewOrderDTO dto);
	
	// 2. 建立明細(私人)
	PurchaseDetail createDetail(CreateNewDetialDTO dto);
	
	// 3. 統合起來回傳(私人)
	PurchaseOrderDTO createFinal(PurchaseOrder purchaseOrder, Set<PurchaseDetail> purchaseDetails);
	
	// 進貨單 狀態 與 盤點後的 正確進貨數量
	// 1. 更新 明細中 改成 正確的數量 同時連動改變 總金額
	
	// 2. 狀態 草稿 -> 入庫完成 
	
	// 進貨單 固定時間 銷帳
	
	// 刪除進貨單
}
