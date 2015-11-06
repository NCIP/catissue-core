package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ShippingOrderErrorCode implements ErrorCode {
	NOT_FOUND,
	
	NAME_REQUIRED,
	
	DUP_NAME,
	
	SITE_REQUIRED,
	
	DUPLICATE_SPECIMENS,
	
	SPECIMEN_QUALITY_REQUIRED,
	
	INVALID_SPECIMEN_QUALITY,
	
	NO_SPECIMENS_TO_SHIP,
	
	NO_SPECIMENS_TO_COLLECT,
	
	STATUS_REQUIRED,
	
	INVALID_STATUS,
	
	INVALID_SHIPPING_DATE,
	
	ORDER_ALREADY_SHIPPED,
	
	SPECIMEN_ALREADY_SHIPPED,
	
	ORDER_ALREADY_COLLECTED,
	
	INVALID_SPECIMENS,
	
	ORDER_NOT_SHIPPED,
	
	INVALID_COLLECT_SPECIMEN,
	
	QUALITY_REQUIRED,
	
	INVALID_QUALITY;
	
	@Override
	public String code() {
		return "SHIP_ORDER_" + this.name();
	}
}
