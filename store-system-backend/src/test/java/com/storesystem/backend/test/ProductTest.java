package com.storesystem.backend.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.model.dto.FindAllDTO;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.dto.product.ProductFindByNameDTO;
import com.storesystem.backend.model.dto.product.ProductFindBySupplierDTO;
import com.storesystem.backend.model.dto.product.ProductSearchAllDTO;
import com.storesystem.backend.service.ProductService;

@SpringBootTest
@Transactional
public class ProductTest {

	enum ExpectResult {
		EMPTY, NOT_EMPTY
	}
	
	@Autowired
	private ProductService productService;
	
	@Test
	public void findAll() {
		FindAllDTO dto = new FindAllDTO();
		dto.setPage(1);
		dto.setSize(10);
		searchAllProduct(ProductSearchAllDTO.from(dto), ExpectResult.NOT_EMPTY);
	}
	
	@Test
	public void shouldProductFindByName() {
		ProductFindByNameDTO dto = new ProductFindByNameDTO();
		dto.setName("統一");
		dto.setPage(1);
		dto.setSize(10);
		searchAllProduct(ProductSearchAllDTO.from(dto), ExpectResult.NOT_EMPTY);
	}
	
	@Test
	public void shouldProductFindByName_isEmpty() {
		ProductFindByNameDTO dto = new ProductFindByNameDTO();
		dto.setName("味全");	// 目前不存在的商品
		dto.setPage(1);
		dto.setSize(10);
		searchAllProduct(ProductSearchAllDTO.from(dto), ExpectResult.EMPTY);
	}
	
	@Test
	public void shouldProductFindBySupplier() {
		ProductFindBySupplierDTO dto = new ProductFindBySupplierDTO();
		dto.setSupplierId(1L);
		dto.setPage(1);
		dto.setSize(10);
		searchAllProduct(ProductSearchAllDTO.from(dto), ExpectResult.NOT_EMPTY);
	}
	
	@Test
	public void shouldProductFindBySupplier_isEmpty() {
		ProductFindBySupplierDTO dto = new ProductFindBySupplierDTO();
		dto.setSupplierId(5L);	// 目前不存在的 供應商編號
		dto.setPage(1);
		dto.setSize(10);
		searchAllProduct(ProductSearchAllDTO.from(dto), ExpectResult.EMPTY);
	}
	
	private void searchAllProduct(ProductSearchAllDTO dto, ExpectResult result) {
		// 使用方法
		PageDTO<ProductDTO> pageDTO = productService.searchAllProduct(dto);
		
		// 使用斷言(Assertions) 進行 控制 驗證
		// 驗證回傳物件不能是空的
		Assertions.assertNotNull(pageDTO);
		
		// 驗證分頁邏輯是否正確
		Assertions.assertEquals(dto.getPage(), pageDTO.getPage(), "當前頁碼需一致");
		Assertions.assertTrue(pageDTO.getSize() <= 10, "每頁筆數不應超過 10");
		
		// 驗證 搜尋的結果 是不是 空的
		if (result == ExpectResult.EMPTY) {
			Assertions.assertTrue(pageDTO.getContent().isEmpty(), "搜尋結果 不是空的不正常");
		} 
		else if (result == ExpectResult.NOT_EMPTY) {
			Assertions.assertFalse(pageDTO.getContent().isEmpty(), "搜尋結果 不應該為空");
		}
		
		// 抽查 名字 是否包含在裡面
		if (dto.getProductName() != null && !pageDTO.getContent().isEmpty()) {
			String firstName = pageDTO.getContent().get(0).getName();
			Assertions.assertTrue(firstName.contains(dto.getProductName()), 
					"第一筆商品名稱 [" + firstName + "] 應包含關鍵字 [" + dto.getProductName() + "]");
		}
	}
}
