package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SpecimenPathologyStatusErrorCode implements ErrorCode {
	ABBR_NOT_FOUND;

	@Override
	public String code() {
		return "SPECIMEN_PATH_STATUS_" + this.name();
	}

}
