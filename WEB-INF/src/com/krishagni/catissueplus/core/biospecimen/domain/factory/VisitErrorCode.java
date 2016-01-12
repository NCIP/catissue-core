package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;


public enum VisitErrorCode implements ErrorCode {
	NOT_FOUND,
	
	INVALID_STATUS,
	
	NAME_REQUIRED,
	
	DUP_NAME,
	
	INVALID_NAME,
	
	MANUAL_NAME_NOT_ALLOWED,
	
	INVALID_CLINICAL_DIAGNOSIS,
	
	INVALID_CLINICAL_STATUS,
	
	INVALID_MISSED_REASON,

	INVALID_MISSED_USER,
	
	SITE_REQUIRED,
	
	INVALID_VISIT_DATE,
	
	REF_ENTITY_FOUND,
	
	NO_SPR_UPLOADED,
	
	UNABLE_TO_LOCATE_SPR,
	
	LOCKED_SPR,
	
	NON_TEXT_SPR,
	
	INVALID_COHORT;
	
	public String code() {
		return "VISIT_" + this.name();
	}
}
