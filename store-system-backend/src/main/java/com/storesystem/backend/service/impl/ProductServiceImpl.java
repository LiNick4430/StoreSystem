package com.storesystem.backend.service.impl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.exception.ProductExistsException;
import com.storesystem.backend.exception.ProductHasSupplierException;
import com.storesystem.backend.exception.ProductNotFoundException;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.product.ProductCreateDTO;
import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.dto.product.ProductDeleteDTO;
import com.storesystem.backend.model.dto.product.ProductIsForSaleDTO;
import com.storesystem.backend.model.dto.product.ProductSearchAllDTO;
import com.storesystem.backend.model.dto.product.ProductSearchDTO;
import com.storesystem.backend.model.dto.product.ProductUpdateDTO;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.repository.ProductRepository;
import com.storesystem.backend.repository.ProductSupplierRepository;
import com.storesystem.backend.repository.spec.ProductSpec;
import com.storesystem.backend.service.ProductService;
import com.storesystem.backend.util.PageUtil;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EntityFetcher entityFetcher;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductSupplierRepository productSupplierRepository;

	@Override
	public PageDTO<ProductDTO> searchAllProduct(ProductSearchAllDTO dto) {
		// 1. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(dto.getPage(), dto.getSize());
		
		// 2. 分類執行
		Specification<Product> spec = Specification.unrestricted();
		
		if (dto.getProductName() != null) {
			spec = spec.and(ProductSpec.hasProductNameLike(dto.getProductName()));
		}
		
		if (dto.getSupplierId() != null) {
			spec = spec.and(ProductSpec.hasSupplierId(dto.getSupplierId()));
		}
		
		Page<Product> page = productRepository.findAll(spec, pageable);
		
		// 3. 轉成 DTO
		return PageUtil.toPageDTO(page, product -> modelMapper.map(product, ProductDTO.class));
	}

	@Override
	public ProductDTO searchProduct(ProductSearchDTO dto) {
		// 1. 分類執行
		Product product = null;
		if (dto.getProductId() != null) {
			// 用 商品ID 搜尋
			product = entityFetcher.getProductById(dto.getProductId());
		} else if (dto.getBarcode() != null) {
			// 用 商品條碼 搜尋
			product = entityFetcher.getProductByBarcode(dto.getBarcode());
		} else {
			// 防呆用 正常流程 不可能到達
			throw new ProductNotFoundException("找不到該商品");
		}
		
		// 2. 轉成 DTO
		return modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public ProductDTO createProdcut(ProductCreateDTO dto) {
		// 1. 前處理
		String barcode = dto.getBarcode();
		productRepository.existsByBarcode(barcode)
			.ifPresent(found -> {
				throw new ProductExistsException("商品條碼已經被使用");
			});

		// 2. 尋找 是否已經存在 同條碼的商品(被軟刪除)
		Product product = entityFetcher.getProductByBarcodeIsDelete(barcode);
		
		if (product != null) {
			// 2-1. 資源回收的狀態 把 資料 改成 預設值
			product.setDeleteAt(null);
			product.setIsForSale(false);
			product.setStockQuantity(0);
		} else {
			// 2-2. 建立新的場合 先放入 條碼
			product = new Product();
			product.setBarcode(barcode);
		}
		
		// 3. 補上 剩餘資料
		product.setProductName(dto.getName());
		product.setSpec(dto.getSpec());
		product.setPrice(dto.getPrice());

		// 4. 儲存商品
		product = productRepository.save(product);

		// 5. 轉成 DTO
		return modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public ProductDTO updateProduct(ProductUpdateDTO dto) {
		// 1. 尋找商品
		Product product = entityFetcher.getProductById(dto.getId());

		// 2. 更新資料
		product.setProductName(dto.getName());
		product.setSpec(dto.getSpec());
		product.setPrice(dto.getPrice());

		// 3. 儲存商品
		product = productRepository.save(product);

		// 4. 轉成 DTO
		return modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public ProductDTO setProductSaleStatus(ProductIsForSaleDTO dto) {
		// 1. 尋找商品
		Product product = entityFetcher.getProductById(dto.getId());

		// 2. 更新資料
		product.setIsForSale(dto.getIsForSale());
		
		// 3. 儲存商品
		product = productRepository.save(product);
		
		// 4. 轉成 DTO
		return modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public void deleteProduct(ProductDeleteDTO dto) {
		// 1. 尋找商品
		Product product = entityFetcher.getProductById(dto.getId());
		
		// 2. 檢查條件
		productSupplierRepository.existsProductHasSupplier(dto.getId())
			.ifPresent(found -> {
				throw new ProductHasSupplierException("該商品還有供應商供應 無法刪除");
			});
		
		// 3. 執行刪除 並且回存
		product.setDeleteAt(LocalDateTime.now());
		productRepository.save(product);
	}

}
