package com.storesystem.backend.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PageDTO<T> {

	private List<T> content;		// 當前頁 資料
	private long totalElements;		// 總筆數
	private int totalPages;			// 總頁數
	private int page;				// 當前頁碼(1-base)
	private int size;				// 每頁筆數
	
}
