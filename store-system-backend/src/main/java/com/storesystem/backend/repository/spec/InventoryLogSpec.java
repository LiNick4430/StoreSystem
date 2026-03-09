package com.storesystem.backend.repository.spec;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.storesystem.backend.model.entity.InventoryLog;

public class InventoryLogSpec {

	/** detail Ids */
	public static Specification<InventoryLog> hasDetailIds(List<Long> detailIds) {
		return (root, query, cb) -> {
			if (detailIds == null || detailIds.isEmpty()) {
				return cb.disjunction();
			}
			return root.get("purchaseDetail").get("purchaseDetailId").in(detailIds);
		};
	}
}
