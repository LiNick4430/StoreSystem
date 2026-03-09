package com.storesystem.backend.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.storesystem.backend.converter.PurchaseStatusToDescriptionConveter;
import com.storesystem.backend.model.dto.inventoryLog.InventoryLogDTO;
import com.storesystem.backend.model.dto.product.ProductDTO;
import com.storesystem.backend.model.dto.productSupplier.ProductSupplierDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseDetailDTO;
import com.storesystem.backend.model.dto.purchase.PurchaseOrderDTO;
import com.storesystem.backend.model.dto.supplier.SupplierDTO;
import com.storesystem.backend.model.entity.InventoryLog;
import com.storesystem.backend.model.entity.Product;
import com.storesystem.backend.model.entity.ProductSupplier;
import com.storesystem.backend.model.entity.PurchaseDetail;
import com.storesystem.backend.model.entity.PurchaseOrder;
import com.storesystem.backend.model.entity.Supplier;

@Configuration
public class ModelMapperConfig {

	@Autowired
	private PurchaseStatusToDescriptionConveter purchaseStatusToDescriptionConveter;
	
	@Bean
	ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		
		// 啟動 嚴格模式(名稱完全一致), 關閉 模糊猜測 功能
		modelMapper.getConfiguration()
					.setMatchingStrategy(MatchingStrategies.STRICT);
		
		// Entity -> DTO
		// TODO 放入 加入轉換邏輯
		modelMapper.typeMap(Product.class, ProductDTO.class).addMappings(mapper -> {
			mapper.map(Product::getProductId, ProductDTO::setId);
			mapper.map(Product::getProductName, ProductDTO::setName);
			mapper.map(Product::getStockQuantity, ProductDTO::setStock);
			mapper.map(Product::getIsForSale, ProductDTO::setForSale);
		});
		
		modelMapper.typeMap(Supplier.class, SupplierDTO.class).addMappings(mapper -> {
			mapper.map(Supplier::getSupplierId, SupplierDTO::setId);
			mapper.map(Supplier::getSupplierName, SupplierDTO::setName);
			
			mapper.skip(SupplierDTO::setProductQty);	// 只有 大量搜尋 才會手動填入
		});
		
		modelMapper.typeMap(ProductSupplier.class, ProductSupplierDTO.class).addMappings(mapper -> {
			mapper.map(src -> src.getProduct().getProductId(), ProductSupplierDTO::setProductId);
			mapper.map(src -> src.getProduct().getBarcode(), ProductSupplierDTO::setBarcode);
			mapper.map(src -> src.getProduct().getProductName(), ProductSupplierDTO::setProductName);
			mapper.map(src -> src.getProduct().getSpec(), ProductSupplierDTO::setSpec);
			
			mapper.map(src -> src.getSupplier().getSupplierId(), ProductSupplierDTO::setSupplierId);
			mapper.map(src -> src.getSupplier().getTaxId(), ProductSupplierDTO::setSupplierTaxID);
			mapper.map(src -> src.getSupplier().getSupplierName(), ProductSupplierDTO::setSupplierName);
		});
		
		modelMapper.typeMap(PurchaseOrder.class, PurchaseOrderDTO.class).addMappings(mapper -> {
			mapper.map(PurchaseOrder::getPurchaseOrderId, PurchaseOrderDTO::setId);
			mapper.map(PurchaseOrder::getPurchaseOrderNumber, PurchaseOrderDTO::setNumber);
			mapper.map(src -> src.getSupplier().getSupplierId(), PurchaseOrderDTO::setSupplierId);
			mapper.map(src -> src.getSupplier().getSupplierName(), PurchaseOrderDTO::setSupplierName);
			mapper.map(PurchaseOrder::getPurchaseOrderDate, PurchaseOrderDTO::setDate);
			mapper.using(purchaseStatusToDescriptionConveter)
					.map(PurchaseOrder::getStatus, PurchaseOrderDTO::setStatus);
			mapper.map(PurchaseOrder::getTotalAmount, PurchaseOrderDTO::setTotal);
			
			mapper.skip(PurchaseOrderDTO::setDetails);	// 使用時 後續手動放入 內文
			mapper.skip(PurchaseOrderDTO::setDetailQty);
		});

		modelMapper.typeMap(PurchaseDetail.class, PurchaseDetailDTO.class).addMappings(mapper -> {
			mapper.map(PurchaseDetail::getPurchaseDetailId, PurchaseDetailDTO::setDetailId);
			mapper.map(src -> src.getPurchaseOrder().getPurchaseOrderId(), PurchaseDetailDTO::setOrderId);
			mapper.map(src -> src.getProduct().getProductId(), PurchaseDetailDTO::setProductId);
			mapper.map(src -> src.getProduct().getProductName(), PurchaseDetailDTO::setProductName);
			mapper.map(src -> src.getProduct().getSpec(), PurchaseDetailDTO::setProductSpec);
			mapper.map(PurchaseDetail::getCost, PurchaseDetailDTO::setCost);
			mapper.map(PurchaseDetail::getQuantity, PurchaseDetailDTO::setQuantity);
			mapper.map(PurchaseDetail::getSubtotal, PurchaseDetailDTO::setSubtotal);
		});
		
		modelMapper.typeMap(InventoryLog.class, InventoryLogDTO.class).addMappings(mapper -> {
			mapper.map(src -> src.getPurchaseDetail().getPurchaseDetailId(), InventoryLogDTO::setPurchaseDetailId);
			mapper.map(src -> src.getProduct().getProductId(), InventoryLogDTO::setProductId);
		});
		
		// ------------------------------------------------------------------------------------
		// DTO -> Entity
		// TODO 放入 加入轉換邏輯
		
		// ------------------------------------------------------------------------------------
		// DTO -> DTO
		// TODO 放入 加入轉換邏輯
		
		modelMapper.validate();	// 用於檢查 哪一個出錯
		
		return modelMapper;
	}
}
