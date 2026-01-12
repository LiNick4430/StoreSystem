package com.storesystem.backend.model.enums;

import lombok.Getter;

/**
 * 定義 進貨單 的 狀態
 * */
@Getter
public enum PurchaseStatus implements BaseEnum{
	
	// 草稿
	DRAFT("草稿"),
	
	// 驗貨後 完成入庫
	RECEIVED("已驗貨入庫"),
	
	// 固定時間 完成銷帳
	SETTLED("已銷帳");

	private final String description;	// 中文敘述狀態
	
	PurchaseStatus(String description) {
		this.description = description;
	}
	
	/**
     * 根據代碼查找對應的 Enum 實例。
     */
	public static PurchaseStatus fromCode(String code) {
		return BaseEnum.fromCode(PurchaseStatus.class, code);
	}
	
}
