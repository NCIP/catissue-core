package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;


public enum VisitErrorCode implements ErrorCode {
	NOT_FOUND,
	
	INVALID_STATUS,
	
	INVALID_CLINICAL_DIAGNOSIS,
	
	INVALID_CLINICAL_STATUS,
	
	SITE_REQUIRED,
	
	REF_ENTITY_FOUND;
	
	public String code() {
		return "VISIT_" + this.name();
	}
}
