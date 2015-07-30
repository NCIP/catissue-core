package com.krishagni.openspecimen.custom.sgh;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SghErrorCode implements ErrorCode{
	
	INVALID_PARTICIPANT_COUNT,
	
	INVALID_TRID_COUNT;

	@Override
	public String code() {
		return "SGH_" + this.name();
	}
}
