package com.storesystem.backend.model.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
@SQLRestriction("delete_at IS NULL")
public class Product extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long productId;			// 商品ID
	
	@Column(name = "barcode", nullable = false, unique = true, length = 50)
	private String barcode;			// 商品條碼
	
	@Column(name = "product_name", nullable = false, length = 100)
	private String productName;		// 商品名稱
	
	@Column(name = "spec", nullable = false, length = 50)
	private String spec;			// 商品規格
	
	@Column(name = "price", nullable = false, precision = 10, scale = 2)
	private BigDecimal price;		// 商品售價
	
	@Column(name = "is_for_sale", nullable = false)
	private Boolean isForSale = false;	// 是否販賣 預設 否
	
	@Column(name = "stock_quantity", nullable = false)
	private Integer stockQuantity = 0;	// 商品庫存量 預設 0
	
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
	private Set<ProductSupplier> productSuppliers = new HashSet<>();;
}
