package com.krishagni.rbac.common.errors;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum RbacErrorCode implements CatissueErrorCode {
	DUPLICATE_RESOURCE_NAME(1400, "Resource with the provided name already exists!"),
	
	DUPLICATE_OPERATION_NAME(1401,"Operation with the provided name already exists!"),
	
	INVALID_RESOURCE_NAME(1402, "Invalid resource name"),
	
	INVALID_ROLE_NAME(1403,"Invalid role name"),
	
	PERMISSIONS_NOT_FOUND(1404, "Provided permissions not found!"),
	
	USER_ID_NOT_FOUND(1405, "Provided userid not found."),
	
	DUPLICATE_ROLE_NAME(1406, "Duplicate role name"),
	
	INVALID_ACTIVITY_STATUS(1407,"Invalid activity status"),
	
	ROLE_NOT_FOUND(1408, "Role not found!"),
	
	DSO_ID_NOT_FOUND(1409, "DsoId not found!"),
	
	INVALID_OPERATION_NAME(1410, "Operation name is invalid!"),
	
	OPERATION_NOT_FOUND(1411, "Operation not found!"),
	
	RESOURCE_NOT_FOUND(1412, "Resource not found!"),
	
	INVALID_ACTION_SPECIFIED( 1413, "Action specified is not valid!"),
	
	DUPLICATE_PERMISSION( 1414, "Specified permission already exists!"),
	
	USER_ROLE_NOT_FOUND( 1414, "Specified user and role combination doesn't exists!"),
	
	INVALID_SUBJECT_DETAILS( 1415 , "Invalid subject details!"),
	
	SUBJECT_NOT_FOUND( 1416 , "Specified Subject Not Found!"),
	
	INVALID_GROUP_DETAILS( 1417 , "Invalid group details!"),
	
	GROUP_NOT_FOUND( 1418 , "Specified Subject Not Found!"),
	
	INVALID_USER_ACCESS_DETAILS( 1419 , "User access details specified are invalid!"),
	
	INVALID_ROLE_DETAILS(1420, "Invalid role details");

	private int code;

	private String message;
	
	private RbacErrorCode(int code, String message) {
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
