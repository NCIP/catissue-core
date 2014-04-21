
package com.krishagni.catissueplus.core.notification.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum CPStudyMappingErrorCode implements CatissueErrorCode {

	STUDY_NOT_MAPPED_WITH_CP(1500, "Study is not associated with collection protocol in caTissue"),
	PPID_NULL(1501,"Protocol  participant Identifier is null"),
	ERRORS(1502, "Please resolve errors in list");

	private int code;

	private String message;

	private CPStudyMappingErrorCode(int code, String message) {

		this.code = code;
		this.message = message;
	}

	@Override
	public int code() {
		return code;
	}

	@Override
	public String message() {
		return message;
	}

}
