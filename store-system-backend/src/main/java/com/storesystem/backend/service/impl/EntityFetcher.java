package com.storesystem.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storesystem.backend.exception.ProductNotFoundException;
import com.storesystem.backend.exception.ProductSupplierLinkException;
import com.storesystem.backend.exception.SupplierNotFoundException;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.model.entity.ProductSupplier;
import com.storesystem.backend.model.entity.Supplier;
import com.storesystem.backend.repository.ProductRepository;
import com.storesystem.backend.repository.ProductSupplierRepository;
import com.storesystem.backend.repository.SupplierRepository;

/**
 * 負責處理 各種獲取 Entity 的方法 (含 拋出錯誤)
 * */
@Service
public class EntityFetcher {

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductSupplierRepository productSupplierRepository;
	
	/**
	 * 使用 供應商ID 找尋供應商
	 * */
	public Supplier getSupplierById(Long supplierId) {
		return supplierRepository.findById(supplierId)
				.orElseThrow(() -> new SupplierNotFoundException("找不到該供應商"));
	}
	
	/**
	 * 使用 供應商統編 找尋供應商
	 * */
	public Supplier getSupplierByTaxId(String taxId) {
		return supplierRepository.findByTaxId(taxId)
				.orElseThrow(() -> new SupplierNotFoundException("找不到該供應商"));
	}
	
	/**
	 * 使用 商品ID 找尋商品
	 * */
	public Product getProductById(Long productId) {
		return productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("找不到該商品"));
	}
	
	/**
	 * 使用 商品條碼 找尋商品
	 * */
	public Product getProductByBarcode(String barcode) {
		return productRepository.findByBarcode(barcode)
				.orElseThrow(() -> new ProductNotFoundException("找不到該商品"));
	}
	
	/**
	 * 使用 商品條碼 找尋 已經配軟刪除的 商品(找不到 回傳空)
	 * */
	public Product getProductByBarcodeIsDelete(String barcode) {
		return productRepository.findByBarcodeIsDelete(barcode)
				.orElse(null);
	}
	
	/**
	 * 使用 商品 和 供應商 找出 關聯表
	 * */
	public ProductSupplier getProductSupplier(Product product, Supplier supplier) {
		return productSupplierRepository.findByProductAndSupplier(product.getProductId(), supplier.getSupplierId())
				.orElseThrow(() -> new ProductSupplierLinkException("此商品和供應商無關連"));
	}
	
	/**
	 * 使用 商品 和 供應商 找出 已經配軟刪除的 關聯表(找不到 回傳空)
	 * */
	public ProductSupplier getProductSupplierIsDelete(Product product, Supplier supplier) {
		return productSupplierRepository.findByProductAndSupplierIsDelete(product.getProductId(), supplier.getSupplierId())
				.orElse(null);
	}
}
