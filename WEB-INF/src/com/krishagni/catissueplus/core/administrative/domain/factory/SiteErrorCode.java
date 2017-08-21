
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SiteErrorCode implements ErrorCode {
	NOT_FOUND,
	
	NAME_REQUIRED,
	
	INSTITUTE_REQUIRED,
	
	TYPE_REQUIRED,
	
	INVALID_TYPE,
	
	DUP_NAME,
	
	DUP_CODE,
	
	REF_ENTITY_FOUND,
	
	INVALID_SITE_INSTITUTE,

	INVALID_COORDINATOR;
	
	public String code() {
		return "SITE_" + this.name();
	}
}
