package com.storesystem.backend.util;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.storesystem.backend.exception.PageErrorException;
import com.storesystem.backend.model.dto.PageDTO;

/** 處理 Page 相關的方法 */
@Component
public class PageUtil {

	// 預設的 最大大小
	private static final int MAX_SIZE = 100;

	/** 建立 有排序功能的 Pageable 
	 * @param page -> 前端頁碼(1-BASE)
	 * @param size -> 單頁大小
	 * @param isAsc -> 是否為升序 (true: ASC, false: DESC)
	 * @param sortField -> 依照哪個欄位名稱排序 (例如 "createDate")
	 * */
	public static Pageable getPageable(Integer page, Integer size, Boolean isAsc, String sortField) {

		// 前制檢查 頁碼和大小 需要 大於等於 1 
		if (page == null || size == null || page < 1 || size < 1) {
			throw new PageErrorException("分頁參數錯誤：頁碼(page)與大小(size)皆必須為大於 0 的正整數。");
		}
		// 前端傳 1 開始的頁碼 → PageRequest 從 0 開始(轉成 0-BASE)
		int pageNumber = page -1;
		// 防止大小輸入過大 限制 最大值
		int finalSize = (size < MAX_SIZE) ? size : MAX_SIZE;
		
		// 調整排序功能
		Sort sort = (isAsc != null && sortField != null) // 當 兩者都不是 null 則 進入自訂排序
			? Sort.by( isAsc ? Sort.Direction.ASC : Sort.Direction.DESC , sortField )	// 自訂排序
			: Sort.by( Sort.Direction.DESC, "createDate");	// 預設排序 使用 建立時間 從 新 到 舊

		return PageRequest.of(pageNumber, finalSize, sort);
	}

	/** 建立 Pageable
	 * @param page -> 前端頁碼(1-BASE)
	 * @param size -> 單頁大小
	 * */
	public static Pageable getPageable(Integer page, Integer size) {
		return getPageable(page, size, null, null);
	}
	
	/** 將 Page<T> 轉成 PageDTO<R>, 使用DTO 映射 
	 * @param page -> Page<T>
	 * @param mapper -> T 映射成 R 的方法 
	 * */
	public static <T, R> PageDTO<R> toPageDTO(Page<T> page, Function<T, R> mapper) {
		List<R> content = page.getContent().stream().map(mapper).toList();

		return new PageDTO<>(
				content,
				page.getTotalElements(),
				page.getTotalPages(),
				page.getNumber() +1,	// 轉成 1- BASE
				page.getSize());
	}
}
