package com.storesystem.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.storesystem.backend.exception.ValueMissException;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.product.ProductCreateDTO;
import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.dto.product.ProductDeleteDTO;
import com.storesystem.backend.model.dto.product.ProductFindAllDTO;
import com.storesystem.backend.model.dto.product.ProductFindByNameDTO;
import com.storesystem.backend.model.dto.product.ProductFindBySupplierDTO;
import com.storesystem.backend.model.dto.product.ProductFindDTO;
import com.storesystem.backend.model.dto.product.ProductIsForSaleDTO;
import com.storesystem.backend.model.dto.product.ProductUpdateDTO;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.repository.ProductRepository;
import com.storesystem.backend.service.ProductService;
import com.storesystem.backend.util.PageUtil;

public class ProductServiceImpl implements ProductService{

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public PageDTO<ProductDTO> findAllProductsByPage(ProductFindAllDTO productFindAllDTO) {
		// 1. 前處理
		if (productFindAllDTO.getPage() == null || productFindAllDTO.getSize() == null) {
			throw new ValueMissException("缺少頁碼/大小");
		}

		// 2. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(productFindAllDTO.getPage(), productFindAllDTO.getSize());
		
		// 3. 搜尋資料
		Page<Product> page = productRepository.findAll(pageable);

		// 4. 轉成 DTO
		return PageUtil.toPageDTO(page, product -> modelMapper.map(product, ProductDTO.class));
	}

	@Override
	public PageDTO<ProductDTO> findAllProductsByProductNameAndPage(ProductFindByNameDTO productFindByNameDTO) {
		// 1. 前處理
		if (productFindByNameDTO.getPage() == null || productFindByNameDTO.getSize() == null|| 
				productFindByNameDTO.getName() == null || productFindByNameDTO.getName().isBlank()
				) {
			throw new ValueMissException("缺少頁碼/大小/商品名");
		}
		String nameLike = "%" + productFindByNameDTO.getName() + "%";
		
		// 2. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(productFindByNameDTO.getPage(), productFindByNameDTO.getSize());
		
		// 3. 搜尋資料
		Page<Product> page = productRepository.findAllByProductNameLike(nameLike, pageable);
		
		// 4. 轉成 DTO
		return PageUtil.toPageDTO(page, product -> modelMapper.map(product, ProductDTO.class));
	}

	@Override
	public PageDTO<ProductDTO> findAllProductsBySupplierAndPage(ProductFindBySupplierDTO productFindBySupplierDTO) {
		// 1. 前處理
		if (productFindBySupplierDTO.getPage() == null || productFindBySupplierDTO.getSize() == null|| 
				productFindBySupplierDTO.getSupplierId() == null
				) {
			throw new ValueMissException("缺少頁碼/大小/供應商ID");
		}

		// 2. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(productFindBySupplierDTO.getPage(), productFindBySupplierDTO.getSize());

		// 3. 搜尋資料
		Page<Product> page = productRepository.findAllBySupplier(productFindBySupplierDTO.getSupplierId(), pageable);

		// 4. 轉成 DTO
		return PageUtil.toPageDTO(page, product -> modelMapper.map(product, ProductDTO.class));
	}

	@Override
	public ProductDTO findProduct(ProductFindDTO productFindDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductDTO createProdcut(ProductCreateDTO productCreateDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductDTO updateProduct(ProductUpdateDTO productUpdateDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProductSaleStatus(ProductIsForSaleDTO productIsForSaleDTO) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteProduct(ProductDeleteDTO productDeleteDTO) {
		// TODO Auto-generated method stub

	}

}
