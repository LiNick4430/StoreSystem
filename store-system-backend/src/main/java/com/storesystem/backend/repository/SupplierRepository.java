package com.storesystem.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storesystem.backend.model.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier>{
	
	@Override
	@EntityGraph(attributePaths = {"productSuppliers"})
	Page<Supplier> findAll(Specification<Supplier> spec, Pageable pageable);
	
	/** 標準 使用供應商ID 搜尋供應商 */
	Optional<Supplier> findBySupplierId(Long supplierId);
	
	/** 標準 使用供應商統編 搜尋供應商 */
	Optional<Supplier> findByTaxId(String taxId);
	
	/** 自訂 使用 供應商統編 搜尋 已經 軟刪除的 供應商 */
	@Query(value = "SELECT * FROM suppliers "
			+ "WHERE tax_id = :taxId "
			+ "AND delete_at IS NOT NULL ",
			nativeQuery = true)
	Optional<Supplier> findByTaxIdIsDelete(@Param("taxId") String taxId);
	
	/** 自訂 檢查 供應商統編 是否 已經存在 */
	@Query(value = "SELECT 1 FROM suppliers "
			+ "WHERE tax_id = :taxId "
			+ "AND delete_at IS NULL "
			+ "LIMIT 1 ",
			nativeQuery = true)
	Optional<Integer> existsByTaxId(@Param("taxId") String taxId);
}
