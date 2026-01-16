package com.storesystem.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storesystem.backend.model.dto.FindAllDTO;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.supplier.SupplierCreateDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDeleteDTO;
import com.storesystem.backend.model.dto.supplier.SupplierFindByIdDTO;
import com.storesystem.backend.model.dto.supplier.SupplierFindByProductDTO;
import com.storesystem.backend.model.dto.supplier.SupplierFindByTaxIdDTO;
import com.storesystem.backend.model.dto.supplier.SupplierSearchAllDTO;
import com.storesystem.backend.model.dto.supplier.SupplierSearchDTO;
import com.storesystem.backend.model.dto.supplier.SupplierUpdateDTO;
import com.storesystem.backend.response.ApiResponse;
import com.storesystem.backend.service.SupplierService;

import jakarta.validation.Valid;

/**
 * SupplierController	@RequestMapping("/supplier")
 * POST		findAllSuppierByPage				-> "/find/all/page"
 * POST		findAllSuppierByProductAndPage		-> "/find/all/product/page"
 * POST		findById							-> "/find/id"
 * POST		findByTaxId							-> "/find/tax/id"
 * POST		createSupplier						-> "/create"
 * POST		updateSupplier						-> "/update"
 * DELETE	deleteSupplier						-> "/delete"
 * */

@RestController
@RequestMapping("/supplier")
public class SupplierController {

	@Autowired
	private SupplierService supplierService;
	
	@PostMapping("/find/all/page")
	public ApiResponse<PageDTO<SupplierDTO>> findAllSuppierByPage(@Valid @RequestBody FindAllDTO dto) {
		PageDTO<SupplierDTO> pageDTO = supplierService.searchAllSupplier(SupplierSearchAllDTO.from(dto));
		return ApiResponse.success("搜尋供應商成功", pageDTO);
	}
	
	@PostMapping("/find/all/product/page")
	public ApiResponse<PageDTO<SupplierDTO>> findAllSuppierByProductAndPage(@Valid @RequestBody SupplierFindByProductDTO dto) {
		PageDTO<SupplierDTO> pageDTO = supplierService.searchAllSupplier(SupplierSearchAllDTO.from(dto));
		return ApiResponse.success("搜尋供應商成功", pageDTO);
	}
	
	@PostMapping("/find/id")
	public ApiResponse<SupplierDTO> findById(@Valid @RequestBody SupplierFindByIdDTO dto) {
		SupplierDTO supplierDTO = supplierService.searchSupplier(SupplierSearchDTO.from(dto));
		return ApiResponse.success("搜尋供應商成功", supplierDTO);
	}
	
	@PostMapping("/find/tax/id")
	public ApiResponse<SupplierDTO> findByTaxId(@Valid @RequestBody SupplierFindByTaxIdDTO dto) {
		SupplierDTO supplierDTO = supplierService.searchSupplier(SupplierSearchDTO.from(dto));
		return ApiResponse.success("搜尋供應商成功", supplierDTO);
	}
	
	@PostMapping("/create")
	public ApiResponse<SupplierDTO> createSupplier(@Valid @RequestBody SupplierCreateDTO dto) {
		SupplierDTO supplierDTO = supplierService.createSupplier(dto);
		return ApiResponse.success("建立供應商成功", supplierDTO);
	}
	
	@PostMapping("/update")
	public ApiResponse<SupplierDTO> updateSupplier(@Valid @RequestBody SupplierUpdateDTO dto) {
		SupplierDTO supplierDTO = supplierService.updateSupplier(dto);
		return ApiResponse.success("供應商資訊 更新成功", supplierDTO);
	}
	
	@DeleteMapping("/update")
	public ApiResponse<Void> deleteSupplier(@Valid @RequestBody SupplierDeleteDTO dto) {
		supplierService.deleteSupplier(dto);
		return ApiResponse.success("供應商 刪除成功", null);
	}
}
