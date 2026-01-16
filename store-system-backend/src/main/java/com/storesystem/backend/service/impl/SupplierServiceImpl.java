package com.storesystem.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.exception.SupplierExistsException;
import com.storesystem.backend.exception.SupplierNotFoundException;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.supplier.SupplierCreateDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDeleteDTO;
import com.storesystem.backend.model.dto.supplier.SupplierSearchAllDTO;
import com.storesystem.backend.model.dto.supplier.SupplierSearchDTO;
import com.storesystem.backend.model.dto.supplier.SupplierUpdateDTO;
import com.storesystem.backend.model.entity.Supplier;
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
		return PageUtil.toPageDTO(page, supplier -> modelMapper.map(supplier, SupplierDTO.class));
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
		if (supplierRepository.existsByTaxIdIncludingDeleted(taxId) > 0) {
			throw new SupplierExistsException("供應商統編已經被使用");
		}
		
		// 2. 建立供應商
		Supplier supplier = new Supplier();
		supplier.setSupplierName(dto.getName());
		supplier.setTaxId(taxId);
		supplier.setAddress(dto.getAddress());
		supplier.setPhone(dto.getPhone());
		
		// 3. 儲存供應商
		supplier = supplierRepository.save(supplier);
		
		// 4. 轉成 DTO
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
		// TODO Auto-generated method stub

	}

	

}
