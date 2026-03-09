package com.storesystem.backend.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.inventoryLog.InventoryLogDTO;
import com.storesystem.backend.model.dto.inventoryLog.InventoryLogSearchAllDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderDTO;
import com.storesystem.backend.model.dto.purchase.ReceivedOrderDTO;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.model.entity.PurchaseOrder;
import com.storesystem.backend.repository.InventoryLogRepository;
import com.storesystem.backend.repository.ProductRepository;
import com.storesystem.backend.repository.PurchaseOrderRepository;
import com.storesystem.backend.service.InventoryLogService;
import com.storesystem.backend.service.PurchaseService;

@SpringBootTest
@Transactional
public class ReceivedOrderTest {

	@Autowired
	private PurchaseService purchaseService;
	
	@Autowired
	private InventoryLogService inventoryLogService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private InventoryLogRepository inventoryLogRepository;
	
	@Autowired
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@Test
	public void ReceivedOrder() {
		// 0. 測試用 訂貨單 ID
		Long orderId = 5L;
		PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(orderId).orElse(null);
		assertNotNull(purchaseOrder);
		
		Map<Long, Integer> productStock = new HashMap<>();
		purchaseOrder.getPurchaseDetails().stream().forEach(detail -> {
			Product product = productRepository.findById(detail.getProduct().getProductId()).orElse(null);
			assertNotNull(product);
			productStock.put(product.getProductId(), product.getStockQuantity());
		});
		
		
		// 1. 建立 入庫用 DTO
		ReceivedOrderDTO receivedOrderDTO = new ReceivedOrderDTO();
		receivedOrderDTO.setOrderId(orderId);
		receivedOrderDTO.setName("入庫員A");
		
		// 2. 嘗試進行入庫
		PurchaseOrderDTO purchaseOrderDTO = purchaseService.receivedOrder(receivedOrderDTO);
		
		// 3. 檢測 DTO 紀錄
		try {
			// 單行寫法
			System.out.println("回傳 Order DTO JSON:");
			System.out.println(
					new ObjectMapper()
					.findAndRegisterModules()
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(purchaseOrderDTO)
					);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		// 4. 抓取商品庫存 是否正常
		PurchaseOrder afterOrder = purchaseOrderRepository.findById(orderId).orElse(null);
		assertNotNull(afterOrder);

		afterOrder.getPurchaseDetails().forEach(detail -> {
		    Product product = productRepository.findById(detail.getProduct().getProductId()).orElse(null);
		    assertNotNull(product);
		    
		    Integer beforeQty = productStock.get(product.getProductId());
		    Integer expectedQty = beforeQty + detail.getQuantity();
		    
		    assertEquals(expectedQty, product.getStockQuantity(), 
		        "商品 [" + product.getProductName() + "] 的庫存計算不正確！");
		});
		
		// 5. 檢測庫存 Log 是否正確生成
		long logCount = inventoryLogRepository.findAll().stream()
		    .filter(log -> log.getPurchaseDetail().getPurchaseOrder().getPurchaseOrderId().equals(orderId))
		    .count();
		assertEquals(afterOrder.getPurchaseDetails().size(), (int)logCount, "入庫 Log 筆數與明細不符");
		
		// 6. 獲取 庫存LOG DTO
		InventoryLogSearchAllDTO inventoryLogSearchAllDTO = new InventoryLogSearchAllDTO();
		inventoryLogSearchAllDTO.setOrderId(orderId);
		inventoryLogSearchAllDTO.setPage(1);
		inventoryLogSearchAllDTO.setSize(10);
		
		PageDTO<InventoryLogDTO> pageDTO = inventoryLogService.searchAllLog(inventoryLogSearchAllDTO);
		
		try {
			// 單行寫法
			System.out.println("回傳 InventoryLog DTO JSON:");
			System.out.println(
					new ObjectMapper()
					.findAndRegisterModules()
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(pageDTO)
					);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
}
