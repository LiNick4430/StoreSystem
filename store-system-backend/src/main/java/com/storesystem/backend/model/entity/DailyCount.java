package com.storesystem.backend.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "daily_count")
public class DailyCount {	// 計算數量 用的 表格 用於各種唯一 的 number

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "daily_count_id")
	private Long id;				// 計數用 ID
	
	@Column(name = "daily_count_name", nullable = false, unique = true, length = 10)
	private String name;			// 用在哪一個 工具
	
	@Column(name = "daily_count_current_date", nullable = false)
	private LocalDate currentDate;	// 目標日期
	
	@Column(name = "daily_count_current_count", nullable = false)
	private Integer currentCount;	// 計數
}
