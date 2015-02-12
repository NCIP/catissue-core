package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum CprErrorCode implements ErrorCode {
	NOT_FOUND,
	
	INVALID_CP_AND_PPID,
	
	INVALID_CPE,
	
	DUP_PPID,
	
	DUP_BARCODE,
	
	DUP_REGISTRATION,
	
	REG_DATE_REQUIRED,
	
	CP_REQUIRED,
	
	PPID_REQUIRED,
	
	CONSENT_WITNESS_NOT_FOUND,
	
	PARTICIPANT_DETAIL_REQUIRED;
	
	@Override
	public String code() {
		return "CPR_" + this.name();
	}
}
