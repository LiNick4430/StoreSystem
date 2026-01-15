package com.storesystem.backend.service;

import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.FindAllDTO;
import com.storesystem.backend.model.dto.product.ProductCreateDTO;
import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.dto.product.ProductDeleteDTO;
import com.storesystem.backend.model.dto.product.ProductIsForSaleDTO;
import com.storesystem.backend.model.dto.product.ProductFindByNameDTO;
import com.storesystem.backend.model.dto.product.ProductFindBySupplierDTO;
import com.storesystem.backend.model.dto.product.ProductFindByBarcodeDTO;
import com.storesystem.backend.model.dto.product.ProductFindByIdDTO;
import com.storesystem.backend.model.dto.product.ProductUpdateDTO;

public interface ProductService {
	
	// 內部系統 使用
	// 搜尋商品
	PageDTO<ProductDTO> findAllProductsByPage(FindAllDTO dto);
	PageDTO<ProductDTO> findAllProductsByProductNameAndPage(ProductFindByNameDTO dto);
	PageDTO<ProductDTO> findAllProductsBySupplierAndPage(ProductFindBySupplierDTO dto);
	
	ProductDTO findProductById(ProductFindByIdDTO dto);
	ProductDTO findProductByBarcode(ProductFindByBarcodeDTO dto);
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
