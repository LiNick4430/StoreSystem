package com.storesystem.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storesystem.backend.model.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{

	/** 標準 搜尋全部 並且 分頁 */
	Page<Supplier> findAll(Pageable pageable);
	
	/** 自訂 使用關聯表 用 商品ID 搜尋有供貨的 供應商 並且 分頁 */
	// 使用 原生SQL + Pageable 需要加上 countQuery 計算總比數
	@Query(value = "SELECT s.* FROM suppliers s "
			+ "JOIN product_supplier ps ON ps.supplier_id = s.supplier_id "
			+ "WHERE ps.product_id = :productId "
			+ "AND s.delete_at IS NULL ",
			countQuery = "SELECT COUNT(*) FROM suppliers s "
					+ "JOIN product_supplier ps ON ps.supplier_id = s.supplier_id "
					+ "WHERE ps.product_id = :productId "
					+ "AND s.delete_at IS NULL ",
			nativeQuery = true)
	Page<Supplier> findAllByProductId(@Param("productId") Long productId, Pageable pageable);
	
	/** 標準 使用供應商ID 搜尋供應商 */
	Optional<Supplier> findBySupplierId(Long supplierId);
	
	/** 標準 使用供應商統編 搜尋供應商 */
	Optional<Supplier> findByTaxId(String taxId);
	
	/** 自訂 檢查 供應商統編 是否 已經存在(包含已經軟刪除的) */
	@Query(value = "SELECT COUNT(*) FROM suppliers "
			+ "WHERE tax_id = :taxId",
			nativeQuery = true)
	long existsByTaxIdIncludingDeleted(@Param("taxId") String taxId);
}
