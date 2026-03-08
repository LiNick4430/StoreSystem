package com.storesystem.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storesystem.backend.model.entity.PurchaseDetail;

@Repository
public interface PurchaseDetailRepository extends JpaRepository<PurchaseDetail, Long>{

	/** 找出 已經被軟刪除的 細項 用於 資源回收 */
	@Query(value = "SELECT * FROM purchase_detail "
			+ "WHERE purchase_order_id = :orderId "
			+ "AND product_id = :productId "
			+ "AND delete_at IS NOT NULL "
			,nativeQuery = true)
	Optional<PurchaseDetail> findByProductIdAndIsDelete(@Param("orderId") Long orderId,
														@Param("productId") Long productId);
}
