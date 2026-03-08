package com.storesystem.backend.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.SQLRestriction;

import com.storesystem.backend.model.enums.PurchaseStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase_order")
@SQLRestriction("delete_at IS NULL")
public class PurchaseOrder extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchase_order_id")
	private Long purchaseOrderId;		// 進貨單 ID

	@Column(name = "purchase_order_number", nullable = false, unique = true, length = 50)
	private String purchaseOrderNumber;	// 進貨單號

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;			// 進貨 廠商

	@Column(name = "order_date", nullable = false)
	private LocalDate purchaseOrderDate;	// 進貨日期

	@Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalAmount;		// 進貨 總金額

	@Enumerated(EnumType.STRING)
	@Column(name = "purchase_order_status", nullable = false)
	private PurchaseStatus status;		// 進貨單狀態

	@OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
	private Set<PurchaseDetail> purchaseDetails;	// 對應進貨明細

	@Column(name = "detail_count", nullable = false)
	private Integer detailCount = 0;				// 對應明細 數量
	
	
	/**
	 * 計算 總金額 與 總筆數 的 私用方法
	 */
	public void updateSummary() {
		if (this.purchaseDetails == null || this.purchaseDetails.isEmpty()) {
			this.totalAmount = BigDecimal.ZERO;
			this.detailCount = 0;
		} else {
			// 過濾掉已軟刪除的項目
	        Set<PurchaseDetail> activeDetails = purchaseDetails.stream()
	                .filter(d -> d.getDeleteAt() == null)
	                .collect(Collectors.toSet());
			
			// 1. 計算 總金額
			this.totalAmount = activeDetails.stream()
					.map(detail -> detail.getSubtotal())
					.filter(subtotal -> subtotal != null)			// 預防萬一 過濾掉 空的 小計
					.reduce(BigDecimal.ZERO, BigDecimal::add);		// BigDecimal.ZERO 起始參數 , BigDecimal::add 後續累加起來
			
			// 2. 計算 總筆數(方便後續計算)
			this.detailCount = activeDetails.size();
		}
	}

	// Entity 第一次被存入資料庫前 的 執行方法
	@PrePersist
	protected void onPurchaseOrderCreate() {
		updateSummary();
	}

	// Entity 更新/修改 前 的 執行方法
	@PreUpdate
	protected void onPurchaseOrderUpdate() {
		updateSummary();
	}
}
