package com.krishagni.catissueplus.core.common.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ReportSettingErrorCode implements ErrorCode {
	DATA_SOURCE_REQ,

	METRIC_SOURCE_REQ,

	INVALID_SOURCE;

	@Override
	public String code() {
		return "RPT_" + this.name();
	}
}
