package com.storesystem.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.exception.ProductSupplierLinkException;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.productSupplier.PSLinkDTO;
import com.storesystem.backend.model.dto.productSupplier.PSSearchAllDTO;
import com.storesystem.backend.model.dto.productSupplier.PSUnlinkDTO;
import com.storesystem.backend.model.dto.productSupplier.PSUpdateCostDTO;
import com.storesystem.backend.model.dto.productSupplier.ProductSupplierDTO;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.model.entity.ProductSupplier;
import com.storesystem.backend.model.entity.Supplier;
import com.storesystem.backend.repository.ProductSupplierRepository;
import com.storesystem.backend.repository.spec.ProductSupplierSpec;
import com.storesystem.backend.service.ProductSupplierService;
import com.storesystem.backend.util.PageUtil;

@Service
@Transactional
public class ProductSupplierServiceImpl implements ProductSupplierService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EntityFetcher entityFetcher;
	
	@Autowired
	private ProductSupplierRepository productSupplierRepository;
	
	@Override
	public PageDTO<ProductSupplierDTO> searchAllPS(PSSearchAllDTO dto) {
		// 1. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(dto.getPage(), dto.getSize());
		
		// 2. 分類執行
		Specification<ProductSupplier> spec = Specification.unrestricted();
		
		if (dto.getSupplierId() != null) {
			spec = spec.and(ProductSupplierSpec.hasSupplierId(dto.getSupplierId()));
		}
		
		if (dto.getSupplierTaxId() != null) {
			spec = spec.and(ProductSupplierSpec.hasSupplierTaxId(dto.getSupplierTaxId()));
		}
		
		if (dto.getProductId() != null) {
			spec = spec.and(ProductSupplierSpec.hasProductId(dto.getProductId()));
		}
		
		if (dto.getProductBarcode() != null) {
			spec = spec.and(ProductSupplierSpec.hasProductBarcode(dto.getProductBarcode()));
		}
		
		if (dto.getProductName() != null) {
			spec = spec.and(ProductSupplierSpec.hasProductNameLike(dto.getProductName()));
		}
		
		Page<ProductSupplier> page = productSupplierRepository.findAll(spec, pageable);
		
		// 3. 轉成 DTO
		return PageUtil.toPageDTO(page, ps -> modelMapper.map(ps, ProductSupplierDTO.class));
	}

	@Override
	public ProductSupplierDTO linkProductAndSupplier(PSLinkDTO dto) {
		// 1. 前處理
		if (productSupplierRepository.existsByProductIdAndSupplierId(dto.getProductId(), dto.getSupplierId()) > 0) {
			throw new ProductSupplierLinkException("供應商已經進貨該商品");
		}
		
		// 2. 抓取 資料
		Product product = entityFetcher.getProductById(dto.getProductId());
		Supplier supplier = entityFetcher.getSupplierById(dto.getSupplierId());
		
		// 3. 建立 ps
		ProductSupplier ps = new ProductSupplier();
		ps.setDefaultCost(dto.getDefaultCost());
		
		// 4. 雙向關聯
		ps.bindTo(product, supplier);
		
		// 5. 儲存關聯 並 回傳
		ps = productSupplierRepository.save(ps);
		
		// 6. 轉成 DTO
		return modelMapper.map(ps, ProductSupplierDTO.class);
	}

	@Override
	public ProductSupplierDTO updateDefaultCost(PSUpdateCostDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unlinkProductAndSupplier(PSUnlinkDTO dto) {
		// TODO Auto-generated method stub
		
	}

}
