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
@Table(name = "purchase_detail",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"purchase_order_id", "product_id"})
		})
@SQLRestriction("delete_at IS NULL")
public class PurchaseDetail extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchase_detail_id")
	private Long purchaseDetailId;			// 進貨單明細 ID
	
	@ManyToOne
	@JoinColumn(name = "purchase_order_id")
	private PurchaseOrder purchaseOrder;	// 對應的 進貨單ID
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;				// 商品 ID
	
	@Column(name = "cost", nullable = false, precision = 10, scale = 2)
	private BigDecimal cost;				// 進貨 價格
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;				// 進貨 數量
	
	@Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
	private BigDecimal subtotal;			// 單項 小計
}
