package com.storesystem.backend.model.dto.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** ProductService 專用中間 DTO*/
@Getter
@Setter
@NoArgsConstructor
public class ProductSearchDTO {

	private Long productId;
	private String barcode;
	
	public static ProductSearchDTO from(ProductFindByIdDTO dto) {
		ProductSearchDTO search = new ProductSearchDTO();
		search.setProductId(dto.getId());
		return search;
	}
	
	public static ProductSearchDTO from(ProductFindByBarcodeDTO dto) {
		ProductSearchDTO search = new ProductSearchDTO();
		search.setBarcode(dto.getBarcode());
		return search;
	}
}
