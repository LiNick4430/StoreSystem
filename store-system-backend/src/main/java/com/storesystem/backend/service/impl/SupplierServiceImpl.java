package com.storesystem.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.storesystem.backend.exception.ProductNotFoundException;
import com.storesystem.backend.exception.SupplierExistsException;
import com.storesystem.backend.exception.SupplierNotFoundException;
import com.storesystem.backend.exception.TaxIdErrorException;
import com.storesystem.backend.model.dto.FindAllDTO;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.supplier.SupplierCreateDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDeleteDTO;
import com.storesystem.backend.model.dto.supplier.SupplierFindByIdDTO;
import com.storesystem.backend.model.dto.supplier.SupplierFindByProductDTO;
import com.storesystem.backend.model.dto.supplier.SupplierFindByTaxIdDTO;
import com.storesystem.backend.model.dto.supplier.SupplierUpdateDTO;
import com.storesystem.backend.model.entity.Supplier;
import com.storesystem.backend.repository.ProductRepository;
import com.storesystem.backend.repository.SupplierRepository;
import com.storesystem.backend.service.SupplierService;
import com.storesystem.backend.util.PageUtil;
import com.storesystem.backend.util.TaiwanTaxIdValidator;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService{

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public PageDTO<SupplierDTO> findAllSuppierByPage(FindAllDTO dto) {
		// 1. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(dto.getPage(), dto.getSize());

		// 2. 搜尋資料
		Page<Supplier> page = supplierRepository.findAll(pageable);

		// 3. 轉成 DTO
		return PageUtil.toPageDTO(page, supplier -> modelMapper.map(supplier, SupplierDTO.class));			
	}

	@Override
	public PageDTO<SupplierDTO> findAllSuppierByProductAndPage(SupplierFindByProductDTO dto) {
		// 1. 檢查商品是否存在
		productRepository.findById(dto.getProductId()).orElseThrow(() -> new ProductNotFoundException("找不到該商品"));

		// 2. 建立頁碼資料
		Pageable pageable = PageUtil.getPageable(dto.getPage(), dto.getSize());

		// 3. 搜尋資料
		Page<Supplier> page = supplierRepository.findAllByProductId(dto.getProductId(), pageable);

		// 4. 轉成 DTO
		return PageUtil.toPageDTO(page, supplier -> modelMapper.map(supplier, SupplierDTO.class));			
	}

	@Override
	public SupplierDTO findById(SupplierFindByIdDTO dto) {
		// 1. 搜尋資料
		Supplier supplier = supplierRepository.findById(dto.getId())
				.orElseThrow(() -> new SupplierNotFoundException("找不到該供應商"));

		// 2. 轉成 DTO
		return modelMapper.map(supplier, SupplierDTO.class);
	}

	@Override
	public SupplierDTO findByTaxId(SupplierFindByTaxIdDTO dto) {
		// 1. 搜尋資料
		Supplier supplier = supplierRepository.findByTaxId(dto.getTaxId())
				.orElseThrow(() -> new SupplierNotFoundException("找不到該供應商"));

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
		if (!TaiwanTaxIdValidator.isValid(taxId)) {
			throw new TaxIdErrorException("統編規格 錯誤");
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
		Supplier supplier = supplierRepository.findById(dto.getId())
				.orElseThrow(() -> new SupplierNotFoundException("找不到該供應商"));

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
