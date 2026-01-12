package com.storesystem.backend.model.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "inventory_log")
@SQLRestriction("delete_at IS NULL")
public class InventoryLog extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "inventory_log_id")
	private Long inventoryLogId;	// 入庫 ID
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_detail_id", nullable = false)
	private PurchaseDetail purchaseDetail;	// 對應的 進貨明細ID
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;			// 商品 ID
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;			// 進貨 數量
	
	@Column(name = "before_quantity", nullable = false)
	private Integer beforeQuantity;		// 商品 進貨前 數量
	
	@Column(name = "after_quantity", nullable = false)
	private Integer afterQuantity;		// 商品 進貨後 數量
	
}
