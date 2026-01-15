package com.storesystem.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.FindAllDTO;
import com.storesystem.backend.model.dto.product.ProductCreateDTO;
import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.dto.product.ProductDeleteDTO;
import com.storesystem.backend.model.dto.product.ProductFindByBarcodeDTO;
import com.storesystem.backend.model.dto.product.ProductFindByIdDTO;
import com.storesystem.backend.model.dto.product.ProductFindByNameDTO;
import com.storesystem.backend.model.dto.product.ProductFindBySupplierDTO;
import com.storesystem.backend.model.dto.product.ProductIsForSaleDTO;
import com.storesystem.backend.model.dto.product.ProductUpdateDTO;
import com.storesystem.backend.response.ApiResponse;
import com.storesystem.backend.service.ProductService;

import jakarta.validation.Valid;

/**
 * ProductController	@RequestMapping("/product")
 * Inside
 * POST 	findAllProductsByPage 				->	"/find/all/page"
 * POST 	findAllProductsByProductNameAndPage ->	"/find/all/product/name/page"
 * POST 	findAllProductsBySupplierAndPage 	->	"/find/all/supplier/page"
 * POST 	findProductById 					->	"/find/id"
 * POST 	findProductByBarcode 				->	"/find/barcode"
 * POST 	createProdcut 						->	"/create"
 * POST 	updateProduct 						->	"/update"
 * POST 	setProductSaleStatus 				->	"/set/sale/status"
 * DELETE 	deleteProduct 						->	"/delete"
 * 
 * Outside
 * */

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping("/find/all/page")
	public ApiResponse<PageDTO<ProductDTO>> findAllProductsByPage(@Valid @RequestBody FindAllDTO dto) {
		PageDTO<ProductDTO> pageDTO = productService.findAllProductsByPage(dto);
		return ApiResponse.success("搜尋商品成功", pageDTO);
	}
	
	@PostMapping("/find/all/product/name/page")
	public ApiResponse<PageDTO<ProductDTO>> findAllProductsByProductNameAndPage(@Valid @RequestBody ProductFindByNameDTO dto) {
		PageDTO<ProductDTO> pageDTO = productService.findAllProductsByProductNameAndPage(dto);
		return ApiResponse.success("搜尋商品成功", pageDTO);
	}
	
	@PostMapping("/find/all/supplier/page")
	public ApiResponse<PageDTO<ProductDTO>> findAllProductsBySupplierAndPage(@Valid @RequestBody ProductFindBySupplierDTO dto) {
		PageDTO<ProductDTO> pageDTO = productService.findAllProductsBySupplierAndPage(dto);
		return ApiResponse.success("搜尋商品成功", pageDTO);
	}
	
	@PostMapping("/find/id")
	public ApiResponse<ProductDTO> findProductById(@Valid @RequestBody ProductFindByIdDTO dto) {
		ProductDTO productDTO = productService.findProductById(dto);
		return ApiResponse.success("搜尋商品成功", productDTO);
	}
	
	@PostMapping("/find/barcode")
	public ApiResponse<ProductDTO> findProductByBarcode(@Valid @RequestBody ProductFindByBarcodeDTO dto) {
		ProductDTO productDTO = productService.findProductByBarcode(dto);
		return ApiResponse.success("搜尋商品成功", productDTO);
	}
	
	@PostMapping("/create")
	public ApiResponse<ProductDTO> createProdcut(@Valid @RequestBody ProductCreateDTO dto) {
		ProductDTO productDTO = productService.createProdcut(dto);
		return ApiResponse.success("建立商品成功", productDTO);
	}
	
	@PostMapping("/update")
	public ApiResponse<ProductDTO> updateProduct(@Valid @RequestBody ProductUpdateDTO dto) {
		ProductDTO productDTO = productService.updateProduct(dto);
		return ApiResponse.success("商品狀態 更新成功", productDTO);
	}
	
	@PostMapping("/set/sale/status")
	public ApiResponse<ProductDTO> setProductSaleStatus(@Valid @RequestBody ProductIsForSaleDTO dto) {
		ProductDTO productDTO = productService.setProductSaleStatus(dto);
		String message = dto.getIsForSale() ? "正在銷售" : "停止銷售";
		return ApiResponse.success("商品狀態 更新為 " + message + " 成功", productDTO);
	}
	
	@DeleteMapping("/delete")
	public ApiResponse<Void> deleteProduct(@Valid @RequestBody ProductDeleteDTO dto) {
		productService.deleteProduct(dto);
		return ApiResponse.success("商品 刪除成功", null);
	}
}
