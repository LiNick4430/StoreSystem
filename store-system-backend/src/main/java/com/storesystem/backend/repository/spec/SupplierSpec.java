package com.storesystem.backend.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.storesystem.backend.model.entity.ProductSupplier;
import com.storesystem.backend.model.entity.Supplier;

import jakarta.persistence.criteria.Join;

public class SupplierSpec {

	/** 商品ID */
	public static Specification<Supplier> hasProductId(Long productId) {
		return (root, query, cb) -> {
			if (productId == null) return null;
			
			query.distinct(true);	// 確保不會重複供應商 (一對多 多對多 使用)
			
			Join<Supplier, ProductSupplier> ps = root.join("productSuppliers");
			return cb.equal(ps.get("product").get("productId"), productId);
		};
	}
}
