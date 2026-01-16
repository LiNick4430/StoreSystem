package com.storesystem.backend.model.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product_supplier",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"product_id", "supplier_id"})	// 業務用唯一鍵
		})
@SQLRestriction("delete_at IS NULL")
public class ProductSupplier extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_supplier_id")
	private Long productSupplierId;		// ID
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;			// 商品 ID
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id", nullable = false)
	private Supplier supplier;			// 供應商 ID
	
	@Column(name = "default_cost", nullable = false, precision = 10, scale = 2)
	private BigDecimal defaultCost;		// 供應商 報價
	
	// 建立關聯 同時掛載到 商品/供應商 之中
	public void bindTo(Product product, Supplier supplier) {
		this.setProduct(product);
	    this.setSupplier(supplier);
		
		product.getProductSuppliers().add(this);
		supplier.getProductSuppliers().add(this);
	}
}
