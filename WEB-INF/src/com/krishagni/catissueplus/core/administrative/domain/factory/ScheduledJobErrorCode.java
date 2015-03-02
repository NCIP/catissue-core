package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ScheduledJobErrorCode implements ErrorCode {
	NOT_FOUND,
	
	NAME_REQUIRED,
	
	END_DATE_BEFORE_START_DATE,
	
	INVALID_REPEAT_SCHEDULE,
	
	START_DATE_REQUIRED,
	
	DUP_JOB_NAME,
	
	INVALID_TYPE, 
	
	INVALID_STATUS,
	
	JOB_INSTANCE_NOT_FOUND,
	
	INVALID_RECIPIENT;
	
	@Override
	public String code() {
		return "SCHEDULED_JOB_" + this.name();
	}
}
