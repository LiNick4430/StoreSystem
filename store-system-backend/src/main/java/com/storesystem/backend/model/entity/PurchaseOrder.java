package com.storesystem.backend.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

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

	/**
	 * 計算 總金額 的 私用方法
	 * 邏輯： 明細的小計統合
	 */
	public void calculateTotalAmount() {
		if (this.purchaseDetails == null || this.purchaseDetails.isEmpty()) {
			this.totalAmount = BigDecimal.ZERO;
		} else {
			this.totalAmount = purchaseDetails.stream()
					.map(detail -> detail.getSubtotal())
					.filter(subtotal -> subtotal != null)	// 預防萬一 過濾掉 空的 小計
					.reduce(BigDecimal.ZERO, BigDecimal::add);	// BigDecimal.ZERO 起始參數 , BigDecimal::add 後續累加起來
		}
	}

	// Entity 第一次被存入資料庫前 的 執行方法
	@PrePersist
	protected void onPurchaseOrderCreate() {
		calculateTotalAmount();
	}

	// Entity 更新/修改 前 的 執行方法
	@PreUpdate
	protected void onPurchaseOrderUpdate() {
		calculateTotalAmount();
	}
}
