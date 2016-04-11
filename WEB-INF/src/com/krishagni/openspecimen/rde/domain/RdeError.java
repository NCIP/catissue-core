package com.krishagni.openspecimen.rde.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum RdeError implements ErrorCode {
	INVALID,

	INVALID_TOKEN,

	SPECIMENS_ALREADY_COLLECTED,

	INVALID_CONT_POS,

	CONT_POS_OCCUPIED,

	INVALID_CP,

	CANNOT_CONTAIN_SPECIMEN;

	@Override
	public String code() {
		return "RDE_" + this.name();
	}

}
