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

import com.storesystem.backend.model.entity.ProductSupplier;

@Repository
public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, Long>, JpaSpecificationExecutor<ProductSupplier>{

	@Override
    @EntityGraph(attributePaths = {"product", "supplier"}) // 一次性抓取，避免 N+1
	Page<ProductSupplier> findAll(Specification<ProductSupplier> spec, Pageable pageable);
	
	/** 自訂 檢查 供應商 + 商品 是否 已經關聯 */
	@Query(value = "SELECT COUNT(*) FROM product_supplier "
			+ "WHERE product_id = :productId "
			+ "AND supplier_id = :supplierId "
			+ "AND delete_at IS NULL ",
			nativeQuery = true)
	long existsByProductIdAndSupplierId(@Param("productId") Long productId,
										@Param("supplierId") Long supplierId);
	
	
	@Query(value = "SELECT * FROM product_supplier "
			+ "WHERE supplier_id = :supplierId "
			+ "AND product_id = :productId "
			+ "AND delete_at IS NULL ",
			nativeQuery = true)
	Optional<ProductSupplier> findByProductAndSupplier(@Param("productId") Long productId,
														@Param("supplierId") Long supplierId);
	
	@Query(value = "SELECT * FROM product_supplier "
			+ "WHERE supplier_id = :supplierId "
			+ "AND product_id = :productId "
			+ "AND delete_at IS NOT NULL ",
			nativeQuery = true)
	Optional<ProductSupplier> findByProductAndSupplierIsDelete(@Param("productId") Long productId,
																@Param("supplierId") Long supplierId);
}
