package com.storesystem.backend.service;

import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.productSupplier.PSLinkDTO;
import com.storesystem.backend.model.dto.productSupplier.PSSearchAllDTO;
import com.storesystem.backend.model.dto.productSupplier.PSUnlinkAfterDTO;
import com.storesystem.backend.model.dto.productSupplier.PSUnlinkDTO;
import com.storesystem.backend.model.dto.productSupplier.PSUpdateCostDTO;
import com.storesystem.backend.model.dto.productSupplier.ProductSupplierDTO;

public interface ProductSupplierService {

	// 搜尋 商品/供應商 旗下對應的 報價
	/** 包含 無條件, 供應商ID, 供應商統編, 商品ID, 商品條碼, 商品名稱 */
	PageDTO<ProductSupplierDTO> searchAllPS(PSSearchAllDTO dto);
	
	/*
	PageDTO<ProductSupplierDTO> findBySupplierIdAndPage();
	PageDTO<ProductSupplierDTO> findBySupplierTaxIdAndPage();
	
	PageDTO<ProductSupplierDTO> findByProductIdAndPage();
	PageDTO<ProductSupplierDTO> findByProductBarcodeAndPage();
	PageDTO<ProductSupplierDTO> findByProductNameAndPage();
	*/
	
	// 商品與供應商 新增關聯 並 報價
	ProductSupplierDTO linkProductAndSupplier(PSLinkDTO dto);
	
	// 更新 報價
	ProductSupplierDTO updateDefaultCost(PSUpdateCostDTO dto);
	
	// 刪除 商品與供應商 關聯
	PSUnlinkAfterDTO unlinkProductAndSupplier(PSUnlinkDTO dto);
	
}
