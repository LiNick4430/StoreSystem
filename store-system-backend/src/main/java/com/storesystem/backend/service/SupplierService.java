package com.storesystem.backend.service;

import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.supplier.SupplierCreateDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDeleteDTO;
import com.storesystem.backend.model.dto.supplier.SupplierSearchAllDTO;
import com.storesystem.backend.model.dto.supplier.SupplierSearchDTO;
import com.storesystem.backend.model.dto.supplier.SupplierUpdateDTO;

public interface SupplierService {

	// 搜尋供應商
	/** 包含 無條件, 商品ID */
	PageDTO<SupplierDTO> searchAllSupplier(SupplierSearchAllDTO dto);
	
	/** 包含 供應商ID, 供應商統編 */
	SupplierDTO searchSupplier(SupplierSearchDTO dto);
	
	// 建立 供應商
	SupplierDTO createSupplier(SupplierCreateDTO dto);
	
	// 更新 供應商資訊
	SupplierDTO updateSupplier(SupplierUpdateDTO dto);
	
	// 刪除 供應商
	void deleteSupplier(SupplierDeleteDTO dto);
	
}
