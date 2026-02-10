package com.storesystem.backend.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

			// 1. 建立 商品
			Map<String, Product> products = new HashMap<>();
			products.put("COKE_600", createProduct("4710018000201", "可口可樂", "600ml PET瓶", new BigDecimal("25")));
			products.put("COKE_CAN", createProduct("4710018000102", "可口可樂", "330ml 易開罐", new BigDecimal("20")));
			products.put("COKE_ZERO_600", createProduct("4710018007200", "可口可樂(ZERO)", "600ml PET瓶", new BigDecimal("28")));
			products.put("PURE_TEA", createProduct("4710088432014", "統一純喫茶(綠茶)", "650ml 盒裝", new BigDecimal("25")));
			products.put("UNI_NOODLE_5", createProduct("4710088410135", "統一麵(肉燥風味)", "5入裝/袋", new BigDecimal("85")));
			products.put("MILK", createProduct("4710088431116", "瑞穗鮮乳", "930ml 瓶裝", new BigDecimal("92")));
			products.put("HEAD_SHOULDER", createProduct("4902430886123", "海倫仙度絲去屑洗髮乳", "750ml", new BigDecimal("249")));
			products.put("ORALB", createProduct("8001090680000", "Oral-B 牙線", "50公尺", new BigDecimal("99")));
			products.put("COKE_600_NEW", createProduct("4710018028809", "可口可樂", "600ml PET瓶", new BigDecimal("28")));
			products.put("COKE_ZERO_NEW", createProduct("4710018101502", "可口可樂(ZERO)", "600ml PET瓶", new BigDecimal("28")));
			products.put("SARSAPARILLA_600", createProduct("4710174003698", "黑松沙士", "600ml PET瓶", new BigDecimal("25")));
			products.put("SARSAPARILLA_CAN", createProduct("4710174003681", "黑松沙士", "330ml 易開罐", new BigDecimal("20")));
			products.put("VITALON_600", createProduct("4710095951003", "維他露P", "600ml PET瓶", new BigDecimal("25")));
			products.put("VITALON_CAN", createProduct("4710095951010", "維他露P", "250ml 易開罐", new BigDecimal("18")));
			products.put("POMI", createProduct("4710063040012", "波蜜果菜汁", "580ml PET瓶", new BigDecimal("30")));
			products.put("DAILY_C", createProduct("4710126020012", "味全每日C", "900ml 瓶裝", new BigDecimal("42")));
			products.put("UNI_NOODLE_3", createProduct("4710088410203", "統一肉燥麵", "3入裝/袋", new BigDecimal("55")));
			products.put("SCIENCE_NOODLE", createProduct("4710088441016", "統一科學麵", "5入裝/袋", new BigDecimal("45")));
			products.put("KUAI_KUAI_A", createProduct("4710245001230", "乖乖(奶油椰子)", "52g", new BigDecimal("25")));
			products.put("KUAI_KUAI_B", createProduct("4710245001247", "乖乖(五香)", "52g", new BigDecimal("25")));

			// 2. 建立供應商
			Map<String, Supplier> suppliers = new HashMap<>();
			suppliers.put("TAIGU", createSupplier("(測試資料)太古可口可樂", "80033073", "桃園市龜山區興邦路46號", "03-364-8800"));
			suppliers.put("UNI", createSupplier("(測試資料)統一企業股份有限公司", "11033000", "台南市永康區中正路301號", "06-253-2121"));
			suppliers.put("PG", createSupplier("(測試資料)寶僑家品股份有限公司", "84323200", "台北市信義區信義路五段7號", "0800-011-001"));
			suppliers.put("PX", createSupplier("(測試資料)全聯實業股份有限公司", "23123456", "台北市中山區敬業四路33號", "02-2533-7700"));
			suppliers.put("HEISONG", createSupplier("(測試資料)黑松飲料經銷商", "12222567", "新北市三重區重新路五段609巷12號", "02-2999-1234"));
			suppliers.put("FOOD", createSupplier("(測試資料)宏達食品批發", "24567891", "台中市南屯區工業區一路88號", "04-2358-5678"));
			suppliers.put("MILK_TRADE", createSupplier("(測試資料)順發乳品飲料行", "27894561", "高雄市仁武區鳳仁路21號", "07-371-9988"));
			suppliers.put("WHOLESALE", createSupplier("(測試資料)豪泰生活百貨盤商", "31245678", "桃園市八德區和平路168號", "03-366-7788"));

			// 3. 開始串聯
			linkProductSupplier(products.get("COKE_600"), suppliers.get("TAIGU"), new BigDecimal("18"));
			linkProductSupplier(products.get("COKE_CAN"), suppliers.get("TAIGU"), new BigDecimal("12"));
			linkProductSupplier(products.get("COKE_ZERO_600"), suppliers.get("TAIGU"), new BigDecimal("20"));

			linkProductSupplier(products.get("PURE_TEA"), suppliers.get("UNI"), new BigDecimal("17"));
			linkProductSupplier(products.get("UNI_NOODLE_5"), suppliers.get("UNI"), new BigDecimal("65"));
			linkProductSupplier(products.get("MILK"), suppliers.get("UNI"), new BigDecimal("78"));

			linkProductSupplier(products.get("HEAD_SHOULDER"), suppliers.get("PG"), new BigDecimal("180"));
			linkProductSupplier(products.get("ORALB"), suppliers.get("PG"), new BigDecimal("60"));

			linkProductSupplier(products.get("COKE_600"), suppliers.get("PX"), new BigDecimal("22"));

			linkProductSupplier(products.get("SARSAPARILLA_600"), suppliers.get("HEISONG"), new BigDecimal("19"));
			linkProductSupplier(products.get("SARSAPARILLA_CAN"), suppliers.get("HEISONG"), new BigDecimal("15"));
			linkProductSupplier(products.get("VITALON_600"), suppliers.get("HEISONG"), new BigDecimal("18"));
			linkProductSupplier(products.get("VITALON_CAN"), suppliers.get("HEISONG"), new BigDecimal("12"));

			linkProductSupplier(products.get("UNI_NOODLE_5"), suppliers.get("FOOD"), new BigDecimal("60"));
			linkProductSupplier(products.get("UNI_NOODLE_3"), suppliers.get("FOOD"), new BigDecimal("40"));
			linkProductSupplier(products.get("SCIENCE_NOODLE"), suppliers.get("FOOD"), new BigDecimal("32"));
			linkProductSupplier(products.get("KUAI_KUAI_A"), suppliers.get("FOOD"), new BigDecimal("18"));
			linkProductSupplier(products.get("KUAI_KUAI_B"), suppliers.get("FOOD"), new BigDecimal("18"));

			linkProductSupplier(products.get("MILK"), suppliers.get("MILK_TRADE"), new BigDecimal("75"));
			linkProductSupplier(products.get("POMI"), suppliers.get("MILK_TRADE"), new BigDecimal("22"));
			linkProductSupplier(products.get("DAILY_C"), suppliers.get("MILK_TRADE"), new BigDecimal("30"));
			linkProductSupplier(products.get("COKE_600"), suppliers.get("MILK_TRADE"), new BigDecimal("17")); // 全場最低

			linkProductSupplier(products.get("COKE_600_NEW"), suppliers.get("WHOLESALE"), new BigDecimal("19"));
			linkProductSupplier(products.get("COKE_ZERO_NEW"), suppliers.get("WHOLESALE"), new BigDecimal("21"));
			linkProductSupplier(products.get("HEAD_SHOULDER"), suppliers.get("WHOLESALE"), new BigDecimal("170"));
			linkProductSupplier(products.get("ORALB"), suppliers.get("WHOLESALE"), new BigDecimal("55"));
			linkProductSupplier(products.get("KUAI_KUAI_A"), suppliers.get("WHOLESALE"), new BigDecimal("20"));

			System.out.println(">>> 擴充測試資料 初始化完畢 (包含 8 家主供應商與 20 樣商品)");
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
