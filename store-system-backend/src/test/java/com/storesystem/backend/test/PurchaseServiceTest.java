package com.storesystem.backend.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storesystem.backend.model.dto.purchase.CreateNewDetialDTO;
import com.storesystem.backend.model.dto.purchase.CreateNewOrderDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderDTO;
import com.storesystem.backend.model.entity.PurchaseOrder;
import com.storesystem.backend.repository.PurchaseOrderRepository;
import com.storesystem.backend.service.PurchaseService;

@SpringBootTest
public class PurchaseServiceTest {

	@Autowired
	private PurchaseService purchaseService;
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@Test
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
		
		Set<CreateNewDetialDTO> createNewDetialDTOs = new HashSet<CreateNewDetialDTO>(List.of(newDetialDTO1, newDetialDTO2));
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
        assertTrue(new BigDecimal("3000").compareTo(latestOrder.getTotalAmount()) == 0);
        
        // 4. 驗證 回傳DTO
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.writerWithDefaultPrettyPrinter();
        
        try {
        	/* 多行寫法
			String json = mapper
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(orderDTO);
			
			System.out.println("回傳 DTO JSON:");
		    System.out.println(json);
		    */
        	
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
        assertTrue(new BigDecimal("3000").compareTo(orderDTO.getTotal()) == 0);
        assertEquals(2, orderDTO.getDetails().size());
		
	}
	
}
