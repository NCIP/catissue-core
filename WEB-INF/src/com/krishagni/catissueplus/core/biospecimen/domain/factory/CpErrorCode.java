package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum CpErrorCode implements ErrorCode {
	REPOSITORIES_REQUIRED,
	
	INVALID_REPOSITORIES,
	
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
	
	INVALID_DERIVATIVE_LABEL_FMT,
	
	INVALID_VISIT_NAME_FMT,
	
	CREATOR_DOES_NOT_BELONG_CP_REPOS,
	
	PI_DOES_NOT_BELONG_CP_REPOS,
	
	CO_ORD_DOES_NOT_BELONG_CP_REPOS,
	
	CONSENT_REF_ENTITY_FOUND,
	
	DUP_CONSENT;
	
	@Override
	public String code() {
		return "CP_" + this.name();
	}
}
