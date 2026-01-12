package com.storesystem.backend.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

	// 新增時間
	@Column(name = "create_date", nullable = false, updatable = false)
	private LocalDateTime createDate;
	
	// 更新時間
	@Column(name = "update_date", nullable = false)
	private LocalDateTime updateDate;
	
	// 軟刪除時打時間 -> null = 未刪除 / 打時間 = 已軟刪除
	@Column(name = "delete_at")
	private LocalDateTime deleteAt;
	
	// Entity 第一次被存入資料庫前 的 執行方法
	@PrePersist
	protected void onCreate() {
		this.createDate = LocalDateTime.now();
		this.updateDate = LocalDateTime.now();
	}
	
	// Entity 更新/修改 前 的 執行方法
	@PreUpdate
	protected void onUpdate() {
		this.updateDate = LocalDateTime.now();
	}
	
	// 軟刪除 的 執行方法
	protected void softDelete() {
		this.deleteAt = LocalDateTime.now();
	}
}
