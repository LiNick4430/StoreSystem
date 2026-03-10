package com.storesystem.backend.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.purchase.CreateNewDetialDTO;
import com.storesystem.backend.model.dto.purchase.CreateNewOrderDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderFindByIdDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderFindByNumberDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderSearchAllDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderSearchDTO;
import com.storesystem.backend.model.dto.purchase.UpdateDetialDTO;
import com.storesystem.backend.model.dto.purchase.UpdateOrderDTO;
import com.storesystem.backend.model.entity.PurchaseOrder;
import com.storesystem.backend.repository.PurchaseOrderRepository;
import com.storesystem.backend.service.PurchaseService;

@SpringBootTest
@Transactional
public class PurchaseServiceTest {

	@Autowired
	private PurchaseService purchaseService;

	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;

	// @Test
	void searchAll() {
		// 1. 建立 搜尋DTO
		PurchaseOrderSearchAllDTO searchAllDTO = new PurchaseOrderSearchAllDTO();
		searchAllDTO.setSupplierId(1L);
		searchAllDTO.setPage(1);
		searchAllDTO.setSize(10);

		// 2. 執行方法
		PageDTO<PurchaseOrderDTO> page = purchaseService.searchAllPurchaseOrder(searchAllDTO);

		// 3. 測試顯示
		try {
			// 單行寫法
			System.out.println("回傳 DTO JSON:");
			System.out.println(
					new ObjectMapper()
					.findAndRegisterModules()
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(page)
					);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void search() {
		// 0. 基本資料
		Long id = 1L;
		String number = "PON_20260306_0002";
		
		// 1. 建立 搜尋DTO
		PurchaseOrderFindByIdDTO findByIdDTO = new PurchaseOrderFindByIdDTO();
		findByIdDTO.setId(id);
		
		PurchaseOrderFindByNumberDTO findByNumberDTO = new PurchaseOrderFindByNumberDTO();
		findByNumberDTO.setNumber(number);

		// 2. 執行方法
		PurchaseOrderDTO orderDTO = purchaseService.searchPurchaseOrder(PurchaseOrderSearchDTO.from(findByIdDTO));
		PurchaseOrderDTO orderDTO2 = purchaseService.searchPurchaseOrder(PurchaseOrderSearchDTO.from(findByNumberDTO));
		

		// 3. 測試顯示
		assertNotNull(orderDTO);
		assertNotNull(orderDTO2);
		
		try {
			// 單行寫法
			System.out.println("回傳 DTO JSON:");
			System.out.println(
					new ObjectMapper()
					.findAndRegisterModules()
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(orderDTO)
					);
			
			System.out.println("回傳 DTO2 JSON:");
			System.out.println(
					new ObjectMapper()
					.findAndRegisterModules()
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(orderDTO2)
					);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	// @Test
	void createNewOrder() {
		// 1. 建立 模擬資料 DTO
		CreateNewOrderDTO newOrderDTO = new CreateNewOrderDTO();
		newOrderDTO.setSupplierId(1L);
		newOrderDTO.setDate(LocalDate.now());

		CreateNewDetialDTO newDetialDTO1 = new CreateNewDetialDTO();
		newDetialDTO1.setProductId(1L);
		newDetialDTO1.setCost(new BigDecimal(18));
		newDetialDTO1.setQuantity(100);

		CreateNewDetialDTO newDetialDTO2 = new CreateNewDetialDTO();
		newDetialDTO2.setProductId(2L);
		newDetialDTO2.setCost(new BigDecimal(12));
		newDetialDTO2.setQuantity(100);
		
		CreateNewDetialDTO newDetialDTO3 = new CreateNewDetialDTO();
		newDetialDTO3.setProductId(3L);
		newDetialDTO3.setCost(new BigDecimal(20));
		newDetialDTO3.setQuantity(100);

		Set<CreateNewDetialDTO> createNewDetialDTOs = new LinkedHashSet<CreateNewDetialDTO>(List.of(newDetialDTO1, newDetialDTO2, newDetialDTO3));
		newOrderDTO.setDetails(createNewDetialDTOs);

		// 2. 執行 Service
		PurchaseOrderDTO orderDTO = purchaseService.createNewOrder(newOrderDTO);

		// 3. 驗證資料庫
		// 從資料庫中 找 最後一筆
		List<PurchaseOrder> orders = purchaseOrderRepository.findAll();
		PurchaseOrder latestOrder = orders.get(orders.size() - 1);

		System.out.println("單號：" + latestOrder.getPurchaseOrderNumber());
		System.out.println("總金額：" + latestOrder.getTotalAmount());
		System.out.println();

		assertNotNull(latestOrder.getPurchaseOrderNumber());
		assertTrue(new BigDecimal("5000").compareTo(latestOrder.getTotalAmount()) == 0);

		// 4. 驗證 回傳DTO
		try {
			// 單行寫法
			System.out.println("回傳 DTO JSON:");
			System.out.println(
					new ObjectMapper()
					.findAndRegisterModules()
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(orderDTO)
					);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		assertNotNull(orderDTO);
		assertNotNull(orderDTO.getNumber());
		assertTrue(new BigDecimal("5000").compareTo(orderDTO.getTotal()) == 0);
		assertEquals(3, orderDTO.getDetails().size());
	}

	// @Test
	void updateOrder() {
		// 1. 建立 模擬資料 DTO
		UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
		updateOrderDTO.setOrderId(5L);
		updateOrderDTO.setDetails(new HashSet<UpdateDetialDTO>());

		UpdateDetialDTO updateDetialDTO1 = new UpdateDetialDTO();
		updateDetialDTO1.setDetailId(9L);
		updateDetialDTO1.setOrderId(5L);
		updateDetialDTO1.setProductId(2L);
		updateDetialDTO1.setCost(new BigDecimal(13));
		updateDetialDTO1.setQuantity(100);
		updateOrderDTO.getDetails().add(updateDetialDTO1);

		UpdateDetialDTO updateDetialDTO2 = new UpdateDetialDTO();
		updateDetialDTO2.setDetailId(10L);
		updateDetialDTO2.setOrderId(5L);
		updateDetialDTO2.setProductId(1L);
		updateDetialDTO2.setCost(new BigDecimal(19));
		updateDetialDTO2.setQuantity(100);
		updateOrderDTO.getDetails().add(updateDetialDTO2);
		
		UpdateDetialDTO updateDetialDTO3 = new UpdateDetialDTO();
		updateDetialDTO3.setDetailId(11L);
		updateDetialDTO3.setOrderId(5L);
		updateDetialDTO3.setProductId(3L);
		updateDetialDTO3.setCost(new BigDecimal(25));
		updateDetialDTO3.setQuantity(100);
		updateOrderDTO.getDetails().add(updateDetialDTO3);

		// 2. 執行 執行 Service
		PurchaseOrderDTO orderDTO = purchaseService.updateOrder(updateOrderDTO);

		try {
			// 單行寫法
			System.out.println("回傳 DTO JSON:");
			System.out.println(
					new ObjectMapper()
					.findAndRegisterModules()
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(orderDTO)
					);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		// 3. 搜尋到該項 並 檢查是否修正
		PurchaseOrder order = purchaseOrderRepository.findById(5L)
				.orElse(null);
		
		assertNotNull(order);
		assertEquals(order.getDetailCount() , order.getPurchaseDetails().size());
	}
}
