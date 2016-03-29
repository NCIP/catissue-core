package com.krishagni.catissueplus.rde.services;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum BarcodeError implements ErrorCode {
	EMPTY,
	
	INVALID_CP_CODE,
	
	NO_CP_CODE,
	
	INVALID_PPID_CODE,
	
	NO_PPID_CODE,
	
	INVALID_CPE_CODE,
	
	NO_CPE_CODE,
	
	INVALID_SITE_CODE,
	
	NO_SITE_CODE,
	
	INVALID_CP_SITE,
	
	INVALID_COHORT_CODE,
	
	NO_COHORT_CODE,
	
	INVALID,
	
	MORE_PARTS,
	
	SPECIMENS_ALREADY_COLLECTED;
	@Override
	public String code() {
		return "BDE_BC_";
	}

}
