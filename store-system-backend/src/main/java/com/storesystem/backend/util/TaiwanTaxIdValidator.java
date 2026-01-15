package com.storesystem.backend.util;

import org.springframework.stereotype.Component;

/** 驗證 台灣統一編號 相關的方法 */
@Component
public class TaiwanTaxIdValidator {

	/** 2023 年4 月 從 10 改成 5 */
	private static final int OLD_VALIDNUMBER = 10;
	private static final int NEW_VALIDNUMBER = 5;
	
	/** 檢查輸入的統編 是否符合標準 */
	public static boolean isValid(String taxid) {
		if (taxid == null || !taxid.matches("^\\d{8}$")) return false;	// 是不是八位數字
		int[] multipliers = {1, 2, 1, 2, 1, 2, 4, 1};	// 邏輯乘數
		int total = 0;
		for(int i = 0; i < 8; i++) {
			int num = Character.getNumericValue(taxid.charAt(i));
			int product = num * multipliers[i];			// 邏輯乘數 上下相乘
			total += (product / 10) + (product % 10);	// 乘積上下相加 總和
		}
		
		// 檢查 邏輯 (%10 OR %5) 是否符合
		if (total % OLD_VALIDNUMBER == 0 || total % NEW_VALIDNUMBER == 0) return true;
		
		// 檢查例外 當第 7 位數 是 7 的場合 總和有兩個數字 total 和 total+1
		if (taxid.charAt(6) == '7') {
			int totalWithSeven = total + 1;
			if (totalWithSeven % OLD_VALIDNUMBER == 0 || totalWithSeven % NEW_VALIDNUMBER == 0) return true;
		}
		
		// 都不是 的 場合
		return false;
	}
}
