
package com.krishagni.catissueplus.core.notification.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum CPStudyMappingErrorCode implements ErrorCode {

	STUDY_NOT_MAPPED_WITH_CP,
	
	PPID_NULL,
	
	ERRORS;

	@Override
	public String code() {
		return "CP_STUDY_MAPPING_" + this.name();
	}

}
