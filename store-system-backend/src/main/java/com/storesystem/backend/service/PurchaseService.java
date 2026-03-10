package com.storesystem.backend.service;

import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.purchase.CreateNewOrderDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderSearchAllDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderSearchDTO;
import com.storesystem.backend.model.dto.purchase.ReceivedOrderDTO;
import com.storesystem.backend.model.dto.purchase.UpdateOrderDTO;

// 進貨單
public interface PurchaseService {

	// 搜尋進貨單
	PageDTO<PurchaseOrderDTO> searchAllPurchaseOrder(PurchaseOrderSearchAllDTO dto);
	
	PurchaseOrderDTO searchPurchaseOrder(PurchaseOrderSearchDTO dto);
	
	// 建立進貨單(草稿)
	PurchaseOrderDTO createNewOrder(CreateNewOrderDTO dto);
	
	// 更新進貨單(草稿) 只要用於 草稿 最後的 明細修正 
	PurchaseOrderDTO updateOrder(UpdateOrderDTO dto);
	
	// 將 草稿 的 訂貨單 入庫 同時 並 紀錄 庫存LOG
	PurchaseOrderDTO receivedOrder(ReceivedOrderDTO dto);
	
	// 進貨單 固定時間 銷帳
	
	// 刪除進貨單
}
