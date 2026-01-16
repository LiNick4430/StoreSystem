package com.storesystem.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.exception.ProductExistsException;
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
		if (productRepository.existsByBarcodeIncludingDeleted(barcode) > 0) {
			throw new ProductExistsException("商品條碼已經被使用");
		}

		// 2. 建立商品
		Product product = new Product();
		product.setBarcode(barcode);
		product.setProductName(dto.getName());
		product.setSpec(dto.getSpec());
		product.setPrice(dto.getPrice());

		// 3. 儲存商品
		product = productRepository.save(product);

		// 4. 轉成 DTO
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
		// TODO Auto-generated method stub

	}

	

	

}
