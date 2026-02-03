package com.storesystem.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.storesystem.backend.model.dto.FindAllDTO;
import com.storesystem.backend.model.dto.PageDTO;
import com.storesystem.backend.model.dto.productSupplier.PSFindByProductBarcode;
import com.storesystem.backend.model.dto.productSupplier.PSFindByProductId;
import com.storesystem.backend.model.dto.productSupplier.PSFindByProductName;
import com.storesystem.backend.model.dto.productSupplier.PSFindBySupplierId;
import com.storesystem.backend.model.dto.productSupplier.PSFindBySupplierTaxId;
import com.storesystem.backend.model.dto.productSupplier.PSLinkDTO;
import com.storesystem.backend.model.dto.productSupplier.PSSearchAllDTO;
import com.storesystem.backend.model.dto.productSupplier.PSUnlinkAfterDTO;
import com.storesystem.backend.model.dto.productSupplier.PSUnlinkDTO;
import com.storesystem.backend.model.dto.productSupplier.PSUpdateCostDTO;
import com.storesystem.backend.model.dto.productSupplier.ProductSupplierDTO;
import com.storesystem.backend.response.ApiResponse;
import com.storesystem.backend.service.ProductSupplierService;

import jakarta.validation.Valid;

/**
 * PSController		@RequestMapping("/ps")
 * POST	findAllPS							-> "/find/all/page"
 * POST	findAllPSBySuppierId				-> "/find/all/supplier/id/page"
 * POST	findAllPSBySuppierTaxId				-> "/find/all/supplier/tax/id/page"
 * POST	findAllPSByProductId				-> "/find/all/product/id/page"
 * POST	findAllPSByProductName				-> "/find/all/product/name/page"
 * POST	findAllPSByProductBarcode			-> "/find/all/product/barcode/page"
 * POST	linkPS								-> "/link"
 * POST	undatePSCost						-> "/update"
 * POST	unLink							-> "/unlink"
 * */

@RestController
@RequestMapping("/ps")
public class PSController {

	@Autowired
	private ProductSupplierService productSupplierService;
	
	@PostMapping("/find/all/page")
	public ApiResponse<PageDTO<ProductSupplierDTO>> findAllPS(@Valid @RequestBody FindAllDTO dto) {
		PageDTO<ProductSupplierDTO> pageDTO = productSupplierService.searchAllPS(PSSearchAllDTO.from(dto));
		return ApiResponse.success("搜尋全部關聯 成功", pageDTO);
	}
	
	@PostMapping("/find/all/supplier/id/page")
	public ApiResponse<PageDTO<ProductSupplierDTO>> findAllPSBySuppierId(@Valid @RequestBody PSFindBySupplierId dto) {
		PageDTO<ProductSupplierDTO> pageDTO = productSupplierService.searchAllPS(PSSearchAllDTO.from(dto));
		return ApiResponse.success("搜尋供應商關聯 成功", pageDTO);
	}
	
	@PostMapping("/find/all/supplier/tax/id/page")
	public ApiResponse<PageDTO<ProductSupplierDTO>> findAllPSBySuppierTaxId(@Valid @RequestBody PSFindBySupplierTaxId dto) {
		PageDTO<ProductSupplierDTO> pageDTO = productSupplierService.searchAllPS(PSSearchAllDTO.from(dto));
		return ApiResponse.success("搜尋供應商關聯 成功", pageDTO);
	}
	
	@PostMapping("/find/all/product/id/page")
	public ApiResponse<PageDTO<ProductSupplierDTO>> findAllPSByProductId(@Valid @RequestBody PSFindByProductId dto) {
		PageDTO<ProductSupplierDTO> pageDTO = productSupplierService.searchAllPS(PSSearchAllDTO.from(dto));
		return ApiResponse.success("搜尋商品關聯 成功", pageDTO);
	}
	
	@PostMapping("/find/all/product/name/page")
	public ApiResponse<PageDTO<ProductSupplierDTO>> findAllPSByProductName(@Valid @RequestBody PSFindByProductName dto) {
		PageDTO<ProductSupplierDTO> pageDTO = productSupplierService.searchAllPS(PSSearchAllDTO.from(dto));
		return ApiResponse.success("搜尋商品關聯 成功", pageDTO);
	}
	
	@PostMapping("/find/all/product/barcode/page")
	public ApiResponse<PageDTO<ProductSupplierDTO>> findAllPSByProductBarcode(@Valid @RequestBody PSFindByProductBarcode dto) {
		PageDTO<ProductSupplierDTO> pageDTO = productSupplierService.searchAllPS(PSSearchAllDTO.from(dto));
		return ApiResponse.success("搜尋商品關聯 成功", pageDTO);
	}
	
	@PostMapping("/link")
	public ApiResponse<ProductSupplierDTO> linkPS(@Valid @RequestBody PSLinkDTO dto) {
		ProductSupplierDTO psDTO = productSupplierService.linkProductAndSupplier(dto);
		return ApiResponse.success("商品與供應商 關聯成功", psDTO);
	}
	
	@PostMapping("/update")
	public ApiResponse<ProductSupplierDTO> undatePSCost(@Valid @RequestBody PSUpdateCostDTO dto) {
		ProductSupplierDTO psDTO = productSupplierService.updateDefaultCost(dto);
		return ApiResponse.success("關聯報價 修改成功", psDTO);
	}
	
	@PostMapping("/unlink")
	public ApiResponse<PSUnlinkAfterDTO> unLink(@Valid @RequestBody PSUnlinkDTO dto) {
		PSUnlinkAfterDTO psUnlinkAfterDTO = productSupplierService.unlinkProductAndSupplier(dto);
		return ApiResponse.success(String.format("供應商：%s, 商品：%s 關聯取消 成功", psUnlinkAfterDTO.getSupplierName(), psUnlinkAfterDTO.getProductName()), null);
	}
}
