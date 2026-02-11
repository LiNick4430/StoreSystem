package com.storesystem.backend.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.storesystem.backend.util.NumberUtil;

@SpringBootTest
public class NumberUtilTest {

	@Autowired
	private NumberUtil numberUtil;
	
	@Test
	void testConcurrentNumberGeneration() throws Exception {
		// 1. 模擬 100 人 同時點擊
		int threadCount = 100;
		
		// 2. 使用 CompletableFuture 異步執行任務
		List<CompletableFuture<String>> futures = new ArrayList<CompletableFuture<String>>();
		for (int i = 0; i < threadCount; i++) {
			futures.add(CompletableFuture.supplyAsync(() -> numberUtil.generatePurchaseOrderNumber()));
		}
		
		// 3. 等待所有任務完成，並收集結果
		List<String> results = futures.stream()
				.map(CompletableFuture::join)
				.toList();
		
		// 4. 驗證結果
        // 將結果轉為 Set (Set 會自動過濾重複值)
        Set<String> distinctResults = new HashSet<>(results);

        System.out.println("成功產生單號數量: " + results.size());
        System.out.println("不重複單號數量: " + distinctResults.size());

        // 如果兩個 size 一樣，代表沒有重複產生單號
        assertEquals(results.size(), distinctResults.size(), "發現重複的單號！");
	}
}
