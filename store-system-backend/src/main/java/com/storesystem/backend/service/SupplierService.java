package com.storesystem.backend.service;

import com.storesystem.backend.model.dto.FindAllDTO;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.supplier.SupplierCreateDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDeleteDTO;
import com.storesystem.backend.model.dto.supplier.SupplierFindByIdDTO;
import com.storesystem.backend.model.dto.supplier.SupplierFindByProductDTO;
import com.storesystem.backend.model.dto.supplier.SupplierFindByTaxIdDTO;
import com.storesystem.backend.model.dto.supplier.SupplierUpdateDTO;

public interface SupplierService {

	// 搜尋供應商
	PageDTO<SupplierDTO> findAllSuppierByPage(FindAllDTO dto);
	PageDTO<SupplierDTO> findAllSuppierByProductAndPage(SupplierFindByProductDTO dto);
	
	SupplierDTO findById(SupplierFindByIdDTO dto);
	SupplierDTO findByTaxId(SupplierFindByTaxIdDTO dto);
	
	// 建立 供應商
	SupplierDTO createSupplier(SupplierCreateDTO dto);
	
	// 更新 供應商資訊
	SupplierDTO updateSupplier(SupplierUpdateDTO dto);
	
	// 刪除 供應商
	void deleteSupplier(SupplierDeleteDTO dto);
	
}
