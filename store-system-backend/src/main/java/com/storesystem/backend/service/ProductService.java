package com.storesystem.backend.service;

import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.product.ProductCreateDTO;
import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.dto.product.ProductDeleteDTO;
import com.storesystem.backend.model.dto.product.ProductIsForSaleDTO;
import com.storesystem.backend.model.dto.product.ProductSearchAllDTO;
import com.storesystem.backend.model.dto.product.ProductSearchDTO;
import com.storesystem.backend.model.dto.product.ProductUpdateDTO;

public interface ProductService {
	
	// 內部系統 使用
	// 搜尋商品
	/** 包含 無條件, 商品名稱, 供應商ID  */
	PageDTO<ProductDTO> searchAllProduct(ProductSearchAllDTO dto);
	
	/** 包含 商品ID, 商品條碼  */
	ProductDTO searchProduct(ProductSearchDTO dto);
	// 建立 商品基本資料
	ProductDTO createProdcut(ProductCreateDTO dto);
	
	// 更新 商品基本資料(名稱/規格/售價)
	ProductDTO updateProduct(ProductUpdateDTO dto);
	
	// 控制 商品是否銷售(上下架)
	ProductDTO setProductSaleStatus(ProductIsForSaleDTO dto);
	
	// 刪除商品 (當供應商不再出貨/已停產的商品/條碼更新後原條碼的商品)
	void deleteProduct(ProductDeleteDTO dto);
	
	// 外部系統 使用
	// TODO
	
}
