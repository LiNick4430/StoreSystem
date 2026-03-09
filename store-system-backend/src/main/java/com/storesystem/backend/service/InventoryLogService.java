package com.storesystem.backend.service;

import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.inventoryLog.InventoryLogDTO;
import com.storesystem.backend.model.dto.inventoryLog.InventoryLogSearchAllDTO;
import com.storesystem.backend.model.entity.PurchaseDetail;

public interface InventoryLogService {

	/** 搜尋 訂單相關 庫存紀錄 */
	PageDTO<InventoryLogDTO> searchAllLog(InventoryLogSearchAllDTO dto);
	
	/** 建立 入庫紀錄 */
	void createInventoryLog(PurchaseDetail detail, String warehouseName);
	
}
