package com.storesystem.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storesystem.backend.model.entity.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

	Page<Product> findAll(Pageable pageable);
	
	Page<Product> findAllByProductNameLike(String namePattern, Pageable pageable);
	
	// 使用 原生SQL + Pageable 需要加上 countQuery 計算總比數
	@Query(value = "SELECT p.* FROM product p "
			+ "JOIN product_supplier ps ON ps.product_id = p.product_id "
			+ "WHERE ps.supplier_id = :supplierId "
			+ "AND p.delete_at IS NULL ",
			countQuery = "SELECT COUNT(*) FROM product p "
					+ "JOIN product_supplier ps ON ps.product_id = p.product_id "
					+ "WHERE ps.supplier_id = :supplierId "
					+ "AND p.delete_at IS NULL ",
			nativeQuery = true)
	Page<Product> findAllBySupplier(@Param("supplierId") Long supplierId, Pageable pageable);
	
	Optional<Product> findByProductId(Long productId);
	
	Optional<Product> findByBarcode(String barcode);
	
	@Query(value = "SELECT * FROM product "
			+ "WHERE product_id = :productId "
			+ "AND delete_at IS NULL "
			+ "FOR UPDATE ",
			nativeQuery = true)
	Optional<Product> findByProductIdForUpdate(@Param("productId") Long productId);
}
