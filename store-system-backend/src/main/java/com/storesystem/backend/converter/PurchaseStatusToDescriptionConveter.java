package com.storesystem.backend.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import com.storesystem.backend.model.enums.PurchaseStatus;

/**
 * 用於 ModelMapper 從 PurchaseStatus 取出 Description 的 Conveter
 * */
@Component
public class PurchaseStatusToDescriptionConveter extends AbstractConverter<PurchaseStatus, String>{

	@Override
	protected String convert(PurchaseStatus source) {
		if (source != null) {
			return source.getDescription();
		}
		
		return null;
	}

}
