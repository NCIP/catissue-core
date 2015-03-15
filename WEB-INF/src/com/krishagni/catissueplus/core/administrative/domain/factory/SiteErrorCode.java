
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SiteErrorCode implements ErrorCode {
	NOT_FOUND,
	
	NAME_REQUIRED,
	
	TYPE_REQUIRED,
	
	INVALID_TYPE,
	
	DUP_NAME,
	
	DUP_CODE,
	
	REF_ENTITY_FOUND,
	
	DEPENDENCIES_EXIST;
	
	public String code() {
		return "SITE_" + this.name();
	}
}
