package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum CpErrorCode implements ErrorCode {
	DUP_TITLE,
	
	DUP_SHORT_TITLE,
	
	TITLE_REQUIRED,
	
	SHORT_TITLE_REQUIRED,
	
	PI_REQUIRED,
	
	PI_NOT_FOUND,
	
	INVALID_COORDINATORS,
	
	CONSENTS_WAIVED_REQUIRED,
	
	NOT_FOUND,
	
	CONSENT_TIER_NOT_FOUND,
	
	INVALID_SPECIMEN_LABEL_FMT,
	
	INVALID_ALIQUOT_LABEL_FMT,
	
	INVALID_DERIVATIVE_LABEL_FMT;

	@Override
	public String code() {
		return "CP_" + this.name();
	}
}
