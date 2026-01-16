package com.storesystem.backend.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.storesystem.backend.model.entity.ProductSupplier;

public class ProductSupplierSpec {
	
	/** 供應商ID */
	public static Specification<ProductSupplier> hasSupplierId(Long supplierId) {
		return (root, query, cb) ->
			cb.equal(root.get("supplier").get("supplierId"), supplierId);
	}
	
	/** 供應商統編 */
	public static Specification<ProductSupplier> hasSupplierTaxId(String taxId) {
		return (root, query, cb) ->
			cb.equal(root.get("supplier").get("taxId"), taxId);
	}
	
	/** 商品ID */
	public static Specification<ProductSupplier> hasProductId(Long productId) {
		return (root, query, cb) ->
			cb.equal(root.get("product").get("productId"), productId);
	}
	
	/** 商品條碼 */
	public static Specification<ProductSupplier> hasProductBarcode(String barcode) {
		return (root, query, cb) -> 
			cb.equal(root.get("product").get("barcode"), barcode);
	}
	
	/** 商品相似名稱 */
	public static Specification<ProductSupplier> hasProductNameLike(String productName) {
		return (root, query, cb) -> 
			cb.like(root.get("product").get("productName"), "%" + productName + "%");
	}
}
