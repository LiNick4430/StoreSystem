package com.storesystem.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.exception.ProductExistsException;
import com.storesystem.backend.exception.ProductNotFoundException;
import com.storesystem.backend.exception.SupplierNotFoundException;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.FindAllDTO;
import com.storesystem.backend.model.dto.product.ProductCreateDTO;
import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.dto.product.ProductDeleteDTO;
import com.storesystem.backend.model.dto.product.ProductFindByNameDTO;
import com.storesystem.backend.model.dto.product.ProductFindBySupplierDTO;
import com.storesystem.backend.model.dto.product.ProductFindByBarcodeDTO;
import com.storesystem.backend.model.dto.product.ProductFindByIdDTO;
import com.storesystem.backend.model.dto.product.ProductIsForSaleDTO;
import com.storesystem.backend.model.dto.product.ProductUpdateDTO;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.repository.ProductRepository;
import com.storesystem.backend.repository.SupplierRepository;
import com.storesystem.backend.service.ProductService;
import com.storesystem.backend.util.PageUtil;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SupplierRepository supplierRepository;

	@Override
	public PageDTO<ProductDTO> findAllProductsByPage(FindAllDTO dto) {
		// 1. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(dto.getPage(), dto.getSize());

		// 2. 搜尋資料
		Page<Product> page = productRepository.findAll(pageable);

		// 3. 轉成 DTO
		return PageUtil.toPageDTO(page, product -> modelMapper.map(product, ProductDTO.class));
	}

	@Override
	public PageDTO<ProductDTO> findAllProductsByProductNameAndPage(ProductFindByNameDTO dto) {
		// 1. 前處理
		String nameLike = "%" + dto.getName() + "%";

		// 2. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(dto.getPage(), dto.getSize());

		// 3. 搜尋資料
		Page<Product> page = productRepository.findAllByProductNameLike(nameLike, pageable);

		// 4. 轉成 DTO
		return PageUtil.toPageDTO(page, product -> modelMapper.map(product, ProductDTO.class));
	}

	@Override
	public PageDTO<ProductDTO> findAllProductsBySupplierAndPage(ProductFindBySupplierDTO dto) {
		// 1. 檢查供應商是否存在
		supplierRepository.findById(dto.getSupplierId()).orElseThrow(() -> new SupplierNotFoundException("找不到該供應商"));
		
		// 2. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(dto.getPage(), dto.getSize());
		
		// 3. 搜尋資料
		Page<Product> page = productRepository.findAllBySupplierId(dto.getSupplierId(), pageable);

		// 4. 轉成 DTO
		return PageUtil.toPageDTO(page, product -> modelMapper.map(product, ProductDTO.class));
	}

	@Override
	public ProductDTO findProductById(ProductFindByIdDTO dto) {
		// 1. 搜尋資料
		Product product = productRepository.findById(dto.getId())
				.orElseThrow(() -> new ProductNotFoundException("找不到該商品"));

		// 2. 轉成 DTO
		return modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public ProductDTO findProductByBarcode(ProductFindByBarcodeDTO dto) {
		// 1. 搜尋資料
		Product product = productRepository.findByBarcode(dto.getBarcode())
				.orElseThrow(() -> new ProductNotFoundException("找不到該商品"));

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
		Product product = productRepository.findById(dto.getId())
				.orElseThrow(() -> new ProductNotFoundException("找不到該商品"));

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
		Product product = productRepository.findById(dto.getId())
				.orElseThrow(() -> new ProductNotFoundException("找不到該商品"));

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
