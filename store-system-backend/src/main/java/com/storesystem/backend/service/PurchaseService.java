package com.storesystem.backend.service;

import com.storesystem.backend.model.dto.purchase.CreateNewOrderDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderDTO;

// 進貨單
public interface PurchaseService {

	// 搜尋進貨單
	
	// 建立進貨單(草稿)
	PurchaseOrderDTO createNewOrder(CreateNewOrderDTO dto);
	// 1. 建立總單
	// 2. 建立明細
	// 3. 統合起來回傳
	
	// 進貨單 狀態 與 盤點後的 正確進貨數量
	// 1. 更新 明細中 改成 正確的數量 同時連動改變 總金額
	
	// 2. 狀態 草稿 -> 入庫完成 
	
	// 進貨單 固定時間 銷帳
	
	// 刪除進貨單
}
