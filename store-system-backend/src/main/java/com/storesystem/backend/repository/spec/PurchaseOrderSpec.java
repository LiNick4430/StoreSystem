package com.storesystem.backend.repository.spec;

import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.data.jpa.domain.Specification;

import com.storesystem.backend.model.entity.PurchaseOrder;
import com.storesystem.backend.model.enums.PurchaseStatus;

public class PurchaseOrderSpec {

	/** 供應商ID */
	public static Specification<PurchaseOrder> hasSupplierId(Long supplierId) {
		return (root, query, cb) -> {
			if (supplierId == null) return null;
			
			return cb.equal(root.get("supplier").get("supplierId"), supplierId);
		};
	}
	
	/** 進貨單狀態 */
	public static Specification<PurchaseOrder> hasPurchaseStatus(PurchaseStatus purchaseStatus) {
		return (root, query, cb) -> {
			if (purchaseStatus == null) return null;
			
			return cb.equal(root.get("status"), purchaseStatus);
		};
	}
	
	/** 訂單時間 */
	public static Specification<PurchaseOrder> inMonth(YearMonth yearMonth) {
		return (root, query, cb) -> {
			if (yearMonth == null) return null;
			
			LocalDate start = yearMonth.atDay(1);
			LocalDate end = yearMonth.atEndOfMonth();
			
			return cb.between(root.get("purchaseOrderDate"), start, end);
		};
	}
	
	/** 尋找 包含 特定商品 的 訂單 */
	public static Specification<PurchaseOrder> hasProductId(Long productId) {
		return (root, query, cb) -> {
			if (productId == null) return null;
			
			// 強制使用 distinct 避免因為 Join 導致回傳重複的 PurchaseOrder
	        query.distinct(true);
			
			return cb.equal(root.join("purchaseDetails").get("product").get("productId"), productId);
		};
	}
}
