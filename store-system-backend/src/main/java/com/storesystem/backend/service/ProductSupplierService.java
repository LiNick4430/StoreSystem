package com.storesystem.backend.service;

import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.productSupplier.ProductSupplierDTO;

public interface ProductSupplierService {

	// 搜尋 商品/供應商 旗下對應的 報價
	PageDTO<ProductSupplierDTO> findBySupplierIdAndPage();
	PageDTO<ProductSupplierDTO> findBySupplierTaxIdAndPage();
	
	PageDTO<ProductSupplierDTO> findByProductIdAndPage();
	PageDTO<ProductSupplierDTO> findByProductBarcodeAndPage();
	PageDTO<ProductSupplierDTO> findByProductNameAndPage();
	
	// 商品與供應商 新增關聯 並 報價
	ProductSupplierDTO linkProductAndSupplier();
	
	// 更新 報價
	ProductSupplierDTO updateDefaultCost();
	
	// 刪除 商品與供應商 關聯
	void unlinkProductAndSupplier();
	
}
