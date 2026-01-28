package com.storesystem.backend.service.impl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.exception.ProductSupplierLinkException;
import com.storesystem.backend.exception.ProductSupplierNotFoundException;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.productSupplier.PSLinkDTO;
import com.storesystem.backend.model.dto.productSupplier.PSSearchAllDTO;
import com.storesystem.backend.model.dto.productSupplier.PSUnlinkAfterDTO;
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
		
		// 3. 檢查是否有資料
		if (page.isEmpty()) {
			throw new ProductSupplierNotFoundException("無滿足此條件的結果");
		}
		
		// 4. 轉成 DTO
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
		
		// 3. 檢查 有 被刪除的 關連
		ProductSupplier existingDeleted = entityFetcher.getProductSupplierIsDelete(product, supplier);
		ProductSupplier ps = null;
		
		if (existingDeleted != null) {
			// 4-1. 假設存在 就 取消刪除 並 拿來回來用
			ps = existingDeleted;
			ps.setDeleteAt(null);
			ps.setDefaultCost(dto.getDefaultCost());
			
			// 4-2. 雙向關聯
			ps.bindTo(product, supplier);
		} else {
			// 4-1. 不存在 則 建立 新的 ps
			ps = new ProductSupplier();
			ps.setDefaultCost(dto.getDefaultCost());
			
			// 4-2. 雙向關聯
			ps.bindTo(product, supplier);
		}
		
		// 5. 儲存關聯 並 回傳
		ps = productSupplierRepository.save(ps);
		
		// 6. 轉成 DTO
		return modelMapper.map(ps, ProductSupplierDTO.class);
	}

	@Override
	public ProductSupplierDTO updateDefaultCost(PSUpdateCostDTO dto) {
		// 1. 抓取 資料
		Product product = entityFetcher.getProductById(dto.getProductId());
		Supplier supplier = entityFetcher.getSupplierById(dto.getSupplierId());
		ProductSupplier productSupplier = entityFetcher.getProductSupplier(product, supplier);
		
		// 2. 修改資料
		productSupplier.setDefaultCost(dto.getDefaultCost());
		
		// 3. 儲存資料
		productSupplier = productSupplierRepository.save(productSupplier);
		
		// 4. 轉成 DTO
		return modelMapper.map(productSupplier, ProductSupplierDTO.class);
	}

	@Override
	public PSUnlinkAfterDTO unlinkProductAndSupplier(PSUnlinkDTO dto) {
		// 1. 抓取 資料
		Product product = entityFetcher.getProductById(dto.getProductId());
		Supplier supplier = entityFetcher.getSupplierById(dto.getSupplierId());
		ProductSupplier productSupplier = entityFetcher.getProductSupplier(product, supplier);
		
		// 2. 解除關聯
		productSupplier.unBind();
		
		// 3. 軟刪除
		productSupplier.setDeleteAt(LocalDateTime.now());
		
		// 4. 回存
		productSupplier = productSupplierRepository.save(productSupplier);
		
		// 5. 返回資料
		return new PSUnlinkAfterDTO(productSupplier.getSupplier().getSupplierName(), 
									productSupplier.getProduct().getProductName());
	}

}
