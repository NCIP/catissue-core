package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum SpecimenListErrorCode implements CatissueErrorCode {
	INVALID_USER_ID(1400, "Invalid userid provided in request!"),
	
	INVALID_LIST_NAME(1401, "Invalid specimen list name"),
	
	INVALID_SPECIMEN_LABELS(1402, "Invalid specimen labels"),
	
	LIST_ID_REQUIRED(1403, "Specimen List ID is required"),
	
	LIST_NOT_FOUND(1404, "Specimen List Not Found"),
	
	NOT_UPDATE_AUTHORIZED(1405, "User not authorized to modify the list");
	
	private int code;
	
	private String message;
	
	private SpecimenListErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public int code() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String message() {
		// TODO Auto-generated method stub
		return null;
	}

}
