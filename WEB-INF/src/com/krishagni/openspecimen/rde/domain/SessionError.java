package com.krishagni.openspecimen.rde.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SessionError implements ErrorCode {
	NOT_FOUND, 
	
	ACCESS_NOT_ALLOWED;
	
	public String code() {
		return "RDE_SESSION_" + this.name();
	}
}
