package com.storesystem.backend.service.impl;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.exception.SupplierExistsException;
import com.storesystem.backend.exception.SupplierHasProductException;
import com.storesystem.backend.exception.SupplierNotFoundException;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.supplier.SupplierCreateDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDeleteDTO;
import com.storesystem.backend.model.dto.supplier.SupplierSearchAllDTO;
import com.storesystem.backend.model.dto.supplier.SupplierSearchDTO;
import com.storesystem.backend.model.dto.supplier.SupplierUpdateDTO;
import com.storesystem.backend.model.entity.Supplier;
import com.storesystem.backend.repository.ProductSupplierRepository;
import com.storesystem.backend.repository.SupplierRepository;
import com.storesystem.backend.repository.spec.SupplierSpec;
import com.storesystem.backend.service.SupplierService;
import com.storesystem.backend.util.PageUtil;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService{

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private EntityFetcher entityFetcher;
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private ProductSupplierRepository productSupplierRepository;

	@Override
	public PageDTO<SupplierDTO> searchAllSupplier(SupplierSearchAllDTO dto) {
		// 1. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(dto.getPage(), dto.getSize());
		
		// 2. 分類執行
		Specification<Supplier> spec = Specification.unrestricted();
		
		if (dto.getProductId() != null) {
			spec = spec.and(SupplierSpec.hasProductId(dto.getProductId()));
		}
		
		Page<Supplier> page = supplierRepository.findAll(spec, pageable);
		
		// 3. 轉成 DTO
		return PageUtil.toPageDTO(page, supplier -> {
			SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);
			
			// 大量顯示時 快速計算 供應商提供的 商品總數
			supplierDTO.setProductQty(productSupplierRepository.countProductsBySupplier(supplier.getSupplierId()));		
			return supplierDTO;
		});
	}

	@Override
	public SupplierDTO searchSupplier(SupplierSearchDTO dto) {
		// 1. 分類執行
		Supplier supplier = null;
		if (dto.getSupplierId() != null) {
			// 供應商ID 查詢
			supplier = entityFetcher.getSupplierById(dto.getSupplierId());
		} else if (dto.getSupplierTaxId() != null) {
			// 供應商統編 查詢
			supplier = entityFetcher.getSupplierByTaxId(dto.getSupplierTaxId());
		} else {
			// 防呆用 正常流程 不可能到達
			throw new SupplierNotFoundException("找不到該供應商");
		}
		
		// 2. 轉成 DTO
		return modelMapper.map(supplier, SupplierDTO.class);
	}

	@Override
	public SupplierDTO createSupplier(SupplierCreateDTO dto) {
		// 1. 前處理
		String taxId = dto.getTaxId();
		supplierRepository.existsByTaxId(taxId)
			.ifPresent(found -> {
				throw new SupplierExistsException("供應商統編已經被使用");
			});
		
		// 2. 尋找 是否已經存在 同條碼的供應商(被軟刪除)
		Supplier supplier = entityFetcher.getSupplierByTaxIdIsDelete(taxId);
		
		if (supplier != null) {
			// 2-1. 資源回收的狀態 把 資料 改成 預設值
			supplier.setDeleteAt(null);
		} else {
			// 2-2. 建立新的場合 先放入 統編
			supplier = new Supplier();
			supplier.setTaxId(taxId);
		}
		
		// 3. 補上 剩餘資料
		supplier.setSupplierName(dto.getName());
		supplier.setAddress(dto.getAddress());
		supplier.setPhone(dto.getPhone());
		
		// 4. 儲存供應商
		supplier = supplierRepository.save(supplier);
		
		// 5. 轉成 DTO
		return modelMapper.map(supplier, SupplierDTO.class);
	}

	@Override
	public SupplierDTO updateSupplier(SupplierUpdateDTO dto) {
		// 1. 搜尋供應商
		Supplier supplier = entityFetcher.getSupplierById(dto.getId());

		// 2. 更新資料
		supplier.setSupplierName(dto.getName());
		supplier.setAddress(dto.getAddress());
		supplier.setPhone(dto.getPhone());
		
		// 3. 儲存供應商
		supplier = supplierRepository.save(supplier);
		
		// 4. 轉成 DTO
		return modelMapper.map(supplier, SupplierDTO.class);
	}

	@Override
	public void deleteSupplier(SupplierDeleteDTO dto) {
		// 1. 尋找供應商
		Supplier supplier = entityFetcher.getSupplierById(dto.getId());
		
		// 2. 檢查條件
		productSupplierRepository.existsSupplierHasProduct(dto.getId())
			.ifPresent(found -> {
				throw new SupplierHasProductException("該工應商還有供應商品 無法刪除");
			});
		
		// 3. 執行刪除 並且回存
		supplier.setDeleteAt(LocalDateTime.now());
		supplierRepository.save(supplier);
	}

}
