package com.krishagni.catissueplus.rde.services;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ContainerErrorCode implements ErrorCode {
	INVALID_POS,
	
	POS_OCCUPIED,
	
	INVALID_CP,
	
	CANNOT_CONTAIN_SPECIMEN;
	
	@Override
	public String code() {		
		return "BDE_CONT_" + this.name();
	}

}
