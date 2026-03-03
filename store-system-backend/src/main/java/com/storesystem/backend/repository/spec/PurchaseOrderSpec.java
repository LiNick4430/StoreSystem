package com.storesystem.backend.repository.spec;

import org.springframework.data.jpa.domain.Specification;

import com.storesystem.backend.model.entity.PurchaseOrder;

public class PurchaseOrderSpec {

	/** 供應商ID */
	public static Specification<PurchaseOrder> hasSupplierId(Long supplierId) {
		return (root, query, cb) -> {
			return cb.equal(root.get("supplier").get("supplierId"), supplierId);
		};
	}
}
