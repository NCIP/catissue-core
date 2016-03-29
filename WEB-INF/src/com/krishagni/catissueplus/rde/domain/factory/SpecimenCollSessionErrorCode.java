package com.krishagni.catissueplus.rde.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SpecimenCollSessionErrorCode implements ErrorCode {
	NOT_FOUND, 
	
	ACCESS_NOT_ALLOWED;
	
	public String code() {
		return "BDE_SESSION_" + this.name();
	}
}
