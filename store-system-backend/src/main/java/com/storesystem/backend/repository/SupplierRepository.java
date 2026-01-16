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
	
	/** 自訂 檢查 供應商統編 是否 已經存在(包含已經軟刪除的) */
	@Query(value = "SELECT COUNT(*) FROM suppliers "
			+ "WHERE tax_id = :taxId",
			nativeQuery = true)
	long existsByTaxIdIncludingDeleted(@Param("taxId") String taxId);
}
