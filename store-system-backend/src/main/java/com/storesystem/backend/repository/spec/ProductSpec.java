package com.storesystem.backend.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.model.entity.ProductSupplier;

import jakarta.persistence.criteria.Join;

public class ProductSpec {

	/** 供應商ID */
	public static Specification<Product> hasSupplierId(Long supplierId) {
		return (root, query, cb) -> {
			Join<Product, ProductSupplier> ps = root.join("productSuppliers");
			return cb.equal(ps.get("supplier").get("supplierId"), supplierId);
		};
		
	}
	
	/** 商品相似名稱 */
	public static Specification<Product> hasProductNameLike(String productName) {
		return (root, query, cb) -> 
			cb.like(root.get("productName"), "%" + productName + "%");
	}
}
