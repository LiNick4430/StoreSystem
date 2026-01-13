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

	/** 標準 搜尋全部 並且 分頁 */
	Page<Product> findAll(Pageable pageable);
	
	/** 標準 使用相似商品名稱 搜尋全部 並且 分頁 */
	Page<Product> findAllByProductNameLike(String namePattern, Pageable pageable);
	
	/** 自訂 使用關聯表 用 供應商ID 搜尋有進貨的 商品 並且 分頁 */
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
	Page<Product> findAllBySupplierId(@Param("supplierId") Long supplierId, Pageable pageable);
	
	/** 標準 使用商品ID 搜尋特定商品 */
	Optional<Product> findByProductId(Long productId);
	
	/** 標準 使用條碼 搜尋特定商品 */
	Optional<Product> findByBarcode(String barcode);
	
	/** 自訂 使用商品ID 尋找 特定商品 並且上 悲觀鎖 */
	@Query(value = "SELECT * FROM product "
			+ "WHERE product_id = :productId "
			+ "AND delete_at IS NULL "
			+ "FOR UPDATE ",
			nativeQuery = true)
	Optional<Product> findByProductIdForUpdate(@Param("productId") Long productId);
	
	/** 自訂 檢查 條碼是否 已經存在(包含已經軟刪除的) */
	@Query(value = "SELECT EXISTS (SELECT 1 FROM product WHERE barcode = :barcode)",
			nativeQuery = true
			)
	boolean existsByBarcodeIncludingDeleted(@Param("barcode") String barcode);
}
