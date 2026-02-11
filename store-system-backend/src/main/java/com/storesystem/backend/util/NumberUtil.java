package com.storesystem.backend.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.model.entity.DailyCount;
import com.storesystem.backend.repository.DailyCountRepository;

@Service
public class NumberUtil {

	@Autowired
	private DailyCountRepository dailyCountRepository;

	private static final String PURCHASE_ORDER_NUMBER = "PON";

	private static final Integer MAX_DAILY_COUNT = 9999;

	@Transactional
	public String generatePurchaseOrderNumber() {
		return generateNumber(PURCHASE_ORDER_NUMBER);
	}
	
	// 產生 數字 基本方法
	private String generateNumber(String name) {

		LocalDate today = LocalDate.now();

		// 1. 嘗試取出 + 鎖, 不存在 則會 新建立
		DailyCount dailyCount = dailyCountRepository
				.findByNameForUpdate(name)
				.orElseGet(() -> {
					DailyCount d = new DailyCount(null, name, today, 0);
					return dailyCountRepository.saveAndFlush(d);
				});

		// 2. 計算換日
		if (!dailyCount.getCurrentDate().equals(today)) {
			// 2-1. 換日的時候
			dailyCount.setCurrentDate(today);
			dailyCount.setCurrentCount(1);

		} else {
			// 2-2 不換日的時候 計算預防上限(9999)
			Integer newCount = dailyCount.getCurrentCount() + 1;	    	
			if (newCount > MAX_DAILY_COUNT) {
				throw new RuntimeException("單日 " + name + " 流量爆滿 請通知管理人員");
			}

			dailyCount.setCurrentCount(newCount);
		}

		dailyCount = dailyCountRepository.save(dailyCount);

		return formatNumber(dailyCount);
	}

	// 將 dailyCount 轉變為 唯一的 String 
	// EX PON_20260211_0001
	private String formatNumber(DailyCount dailyCount) {
		return dailyCount.getName() + "_"
				+ dailyCount.getCurrentDate().format(DateTimeFormatter.BASIC_ISO_DATE) + "_"
				+ String.format("%04d", dailyCount.getCurrentCount());
	}
}
