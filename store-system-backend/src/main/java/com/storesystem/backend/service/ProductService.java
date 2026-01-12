package com.storesystem.backend.service;

import java.util.List;

import com.storesystem.backend.model.dto.product.ProductDTO;

public interface ProductService {
	
	// 搜尋商品
	List<ProductDTO> findAllProducts();
	ProductDTO findProduct();
	
	// 建立商品
	
	// 更新商品
	
	// 刪除商品
	
}
