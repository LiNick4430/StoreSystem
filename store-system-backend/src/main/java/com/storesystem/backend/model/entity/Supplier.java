package com.storesystem.backend.model.entity;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "suppliers")
@SQLRestriction("delete_at IS NULL")
public class Supplier extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "supplier_id")
	private Long supplierId;		// 供應商 ID
	
	@Column(name = "supplier_name", nullable = false, length = 50)
	private String supplierName;	// 供應商 名稱
	
	@Column(name = "tax_id", nullable = false, unique = true, length = 20)
	private String taxId;			// 供應商 統一編號
	
	@Column(name = "address", nullable = false, length = 200)
	private String address;			// 供應商 地址
	
	@Column(name = "phone", nullable = false, length = 30)
	private String phone;			// 供應商 電話
	
}
