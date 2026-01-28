package com.storesystem.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.storesystem.backend.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{
	/** 標準 使用商品ID 搜尋特定商品 */
	Optional<Product> findByProductId(Long productId);
	
	/** 標準 使用條碼 搜尋特定商品 */
	Optional<Product> findByBarcode(String barcode);
	
	/** 自訂 使用 條碼 搜尋 已經刪除的商品 */
	@Query(value = "SELECT * FROM product "
			+ "WHERE barcode = :barcode "
			+ "AND delete_at IS NOT NULL ",
			nativeQuery = true)
	Optional<Product> findByBarcodeIsDelete(@Param("barcode") String barcode);
	
	/** 自訂 使用商品ID 尋找 特定商品 並且上 悲觀鎖 */
	@Query(value = "SELECT * FROM product "
			+ "WHERE product_id = :productId "
			+ "AND delete_at IS NULL "
			+ "FOR UPDATE ",
			nativeQuery = true)
	Optional<Product> findByProductIdForUpdate(@Param("productId") Long productId);
	
	/** 自訂 檢查 條碼是否 已經存在 */
	@Query(value = "SELECT 1 FROM product "
			+ "WHERE barcode = :barcode "
			+ "AND delete_at IS NULL "
			+ "LIMIT 1",
			nativeQuery = true)
	Optional<Integer> existsByBarcode(@Param("barcode") String barcode);
	
}
