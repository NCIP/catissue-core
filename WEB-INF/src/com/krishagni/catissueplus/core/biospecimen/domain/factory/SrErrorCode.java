package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SrErrorCode implements ErrorCode {
	NOT_FOUND,
	
	PARENT_NOT_FOUND,
	
	INVALID_ALIQUOT_CNT,
	
	INVALID_QTY,
	
	INSUFFICIENT_QTY,
	
	SPECIMEN_CLASS_REQUIRED,
	
	SPECIMEN_TYPE_REQUIRED,
	
	ANATOMIC_SITE_REQUIRED,
	
	LATERALITY_REQUIRED,
	
	PATHOLOGY_STATUS_REQUIRED,
	
	STORAGE_TYPE_REQUIRED,
	
	CONCENTRATION_REQUIRED,
	
	COLL_PROC_REQUIRED,
	
	COLL_CONT_REQUIRED,
	
	COLLECTOR_REQUIRED,
	
	RECEIVER_REQUIRED,
	
	COLLECTOR_NOT_FOUND,
	
	RECEIVER_NOT_FOUND,
	
	CPE_REQUIRED
	
	;

	@Override
	public String code() {
		return "SR_" + this.name();
	}

}
