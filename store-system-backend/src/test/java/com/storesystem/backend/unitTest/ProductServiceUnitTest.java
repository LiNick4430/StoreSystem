package com.storesystem.backend.unitTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.storesystem.backend.exception.ProductExistsException;
import com.storesystem.backend.exception.ProductNotFoundException;
import com.storesystem.backend.model.dto.product.ProductCreateDTO;
import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.dto.product.ProductSearchDTO;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.repository.ProductRepository;
import com.storesystem.backend.service.impl.EntityFetcher;
import com.storesystem.backend.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)	// 使用 Mockito 擴展，不啟動 Spring 容器
public class ProductServiceUnitTest {

	// 在 ProductServiceImpl 中使用外部依賴的 都要 @Mock
	@Mock
	private ProductRepository productRepository;
	
	@Mock
	private ModelMapper modelMapper;
	
	@Mock
	private EntityFetcher entityFetcher;
	
	@InjectMocks	// 一定要用 impl 她才可以 new 一個實體來測試
	private ProductServiceImpl productService;	// 自動將上面三個 Mock 注入到 Service 中
	
	@Test
	void createProduct_Success() {
		// 1. Arrange (準備資料與模擬行為)
		ProductCreateDTO dto = new ProductCreateDTO();
		dto.setName("測試商品");
		dto.setBarcode("12345");
		dto.setSpec("測試規格");
		dto.setPrice(new BigDecimal("150.00"));
		
		// 模擬：當 Repository 檢查條碼時，回傳 0 (代表沒重複)
		when(productRepository.existsByBarcodeIncludingDeleted(dto.getBarcode())).thenReturn(0L);
		/* 對應 service：
		 * if (productRepository.existsByBarcodeIncludingDeleted(barcode) > 0) {
		 * 		throw new ProductExistsException();
		 * }
    	*/
		
		// 模擬：當 Repository 儲存時，回傳儲存後的物件
	    when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);
	    /* 對應 service：
	     * product = productRepository.save(product);
	     * */
		
	    // 模擬：ModelMapper 轉換
	    when(modelMapper.map(any(), eq(ProductDTO.class))).thenReturn(new ProductDTO());
	    /* 對應 service：
	     * return modelMapper.map(product, ProductDTO.class);
	     * */
	    
	    // 2. Act (執行)
	    ProductDTO result = productService.createProdcut(dto);
	    
	    // 3. Assert (驗證)
	    assertNotNull(result);
	    
	    // Verify 行為
	    verify(productRepository).existsByBarcodeIncludingDeleted("12345");	// 有先檢查條碼
	    verify(productRepository, times(1)).save(any(Product.class));		// 有成功存
	    verify(modelMapper).map(any(Product.class), eq(ProductDTO.class));	// 有轉 DTO
	}
	
	@Test
	void createProduct_ShouldThrowException_WhenBarcodeExists() {
	    // 1. Arrange
	    ProductCreateDTO dto = new ProductCreateDTO();
	    dto.setBarcode("EXIST_BARCODE");
	    
	    // 模擬：條碼已存在 (回傳 1)
	    when(productRepository.existsByBarcodeIncludingDeleted("EXIST_BARCODE")).thenReturn(1L);

	    // 2. Act & Assert (驗證是否拋出正確的異常)
	    assertThrows(ProductExistsException.class, () -> {
	        productService.createProdcut(dto);
	    });
	    
	    // 驗證：一旦拋出異常，後面的 save 絕對不會被執行
	    verify(productRepository, never()).save(any());
	}
	
	@Test
	void searchProduct_ByProductId_Success() {	// 「有沒有走到正確的查詢方式」
	    // Arrange
	    ProductSearchDTO dto = new ProductSearchDTO();
	    dto.setProductId(1L);
	    
	    Product mockProduct = new Product();
	    mockProduct.setProductId(1L);
	    
	    when(entityFetcher.getProductById(1L)).thenReturn(mockProduct);
	    when(modelMapper.map(mockProduct, ProductDTO.class)).thenReturn(new ProductDTO());

	    // Act
	    ProductDTO result = productService.searchProduct(dto);

	    // Assert
	    assertNotNull(result);
	    verify(entityFetcher).getProductById(1L); // 確保是呼叫 Id 搜尋而不是條碼搜尋
	}
	
	@Test
	void searchProduct_ByProductBarcode_Success() {	// 「有沒有走到正確的查詢方式」
	    // Arrange
	    ProductSearchDTO dto = new ProductSearchDTO();
	    dto.setBarcode("12345");
	    
	    Product mockProduct = new Product();
	    mockProduct.setBarcode("12345");
	    
	    when(entityFetcher.getProductByBarcode("12345")).thenReturn(mockProduct);
	    when(modelMapper.map(mockProduct, ProductDTO.class)).thenReturn(new ProductDTO());

	    // Act
	    ProductDTO result = productService.searchProduct(dto);

	    // Assert
	    assertNotNull(result);
	    verify(entityFetcher).getProductByBarcode("12345"); // 確保是呼叫條碼搜尋
	}
	
	@Test
	void searchProduct_ShouldThrowException_WhenNoConditionProvided() {
		// Arrange
	    ProductSearchDTO dto = new ProductSearchDTO();
	    
	    assertThrows(ProductNotFoundException.class, () -> 
	    		productService.searchProduct(dto));
	}
}
