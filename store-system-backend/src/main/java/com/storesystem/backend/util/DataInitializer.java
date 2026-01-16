package com.storesystem.backend.util;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.model.entity.ProductSupplier;
import com.storesystem.backend.model.entity.Supplier;
import com.storesystem.backend.repository.ProductRepository;
import com.storesystem.backend.repository.ProductSupplierRepository;
import com.storesystem.backend.repository.SupplierRepository;

@Service
@Transactional
public class DataInitializer implements CommandLineRunner{

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private ProductSupplierRepository productSupplierRepository;
	
	@Override
	public void run(String... args) throws Exception {
		if (productRepository.count() == 0 && 
				supplierRepository.count() == 0 && 
				productSupplierRepository.count() == 0) {
			
			// --- 供應商 A: 太古可口可樂 ---
	        Supplier s1 = createSupplier("(測試資料)太古可口可樂", "80033073", "桃園市龜山區興邦路46號", "03-364-8800");
	        
	        Product p1 = createProduct("4710018000201", "可口可樂", "600ml PET瓶", new BigDecimal("25"));
	        Product p2 = createProduct("4710018000102", "可口可樂", "330ml 易開罐", new BigDecimal("20"));
	        Product p3 = createProduct("4710018007200", "可口可樂(ZERO)", "600ml PET瓶", new BigDecimal("28"));
	        
	        linkProductSupplier(p1, s1, new BigDecimal("18"));
	        linkProductSupplier(p2, s1, new BigDecimal("12"));
	        linkProductSupplier(p3, s1, new BigDecimal("20"));
			
	        // --- 供應商 B: 統一企業 ---
	        Supplier s2 = createSupplier("(測試資料)統一企業股份有限公司", "11033000", "台南市永康區中正路301號", "06-253-2121");
	        
	        Product p4 = createProduct("4710088432014", "統一純喫茶(綠茶)", "650ml 盒裝", new BigDecimal("25"));
	        Product p5 = createProduct("4710088410135", "統一麵(肉燥風味)", "5入裝/袋", new BigDecimal("85"));
	        Product p6 = createProduct("4710088431116", "瑞穗鮮乳", "930ml 瓶裝", new BigDecimal("92"));
	        
	        linkProductSupplier(p4, s2, new BigDecimal("17"));
	        linkProductSupplier(p5, s2, new BigDecimal("65"));
	        linkProductSupplier(p6, s2, new BigDecimal("78"));
	        
	        // --- 供應商 C: 寶僑 P&G (日用品) ---
	        Supplier s3 = createSupplier("(測試資料)寶僑家品股份有限公司", "84323200", "台北市信義區信義路五段7號", "0800-011-001");
	        
	        Product p7 = createProduct("4902430886123", "海倫仙度絲去屑洗髮乳", "750ml", new BigDecimal("249"));
	        Product p8 = createProduct("8001090680000", "Oral-B 牙線", "50公尺", new BigDecimal("99"));
	        
	        linkProductSupplier(p7, s3, new BigDecimal("180"));
	        linkProductSupplier(p8, s3, new BigDecimal("60"));
	        
	        // --- 混合供應測試 (一個產品由兩個供應商供應) ---
	        // 假設全聯也供應可口可樂
	        Supplier s4 = createSupplier("(測試資料)全聯實業股份有限公司", "23123456", "台北市中山區敬業四路33號", "02-2533-7700");
	        linkProductSupplier(p1, s4, new BigDecimal("22")); // 全聯進貨價較貴
	        
	        System.out.println(">>> 擴充測試資料 初始化完畢 (包含 3 家主供應商與 8 樣商品)");
		}
	}
	
	private Product createProduct(String barcode,
									String name,
									String spec,
									BigDecimal price) {
		Product product = new Product();
		product.setBarcode(barcode);
		product.setProductName(name);
		product.setSpec(spec);
		product.setPrice(price);
		return productRepository.save(product);
	}
	
	private Supplier createSupplier(String name,
								String taxId,
								String address,
								String phone) {
		Supplier supplier = new Supplier();
		supplier.setSupplierName(name);
		supplier.setTaxId(taxId);
		supplier.setAddress(address);
		supplier.setPhone(phone);
		return supplierRepository.save(supplier);
	}
	
	private ProductSupplier linkProductSupplier(Product product, Supplier supplier, BigDecimal defaultCost) {
		ProductSupplier ps = new ProductSupplier();
		ps.setDefaultCost(defaultCost);
		ps.bindTo(product, supplier);
		return productSupplierRepository.save(ps);
	}
}
