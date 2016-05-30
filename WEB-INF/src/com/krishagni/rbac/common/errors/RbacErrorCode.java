package com.krishagni.rbac.common.errors;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;


public enum RbacErrorCode implements ErrorCode {
	RESOURCE_NAME_REQUIRED,
	
	RESOURCE_NOT_FOUND,
	
	OPERATION_NAME_REQUIRED,
	
	OPERATION_NOT_FOUND,
	
	DUPLICATE_PERMISSION,
	
	PERMISSION_NOT_FOUND,
	
	ROLE_NAME_REQUIRED,
	
	ROLE_NOT_FOUND, 
	
	DUP_ROLE_NAME,
	
	SUBJECT_ID_REQUIRED, 
	
	GROUP_ID_REQUIRED, 
	
	SUBJECT_NOT_FOUND,
	
	GROUP_NOT_FOUND,
	
	CYCLE_DETECTED_IN_HIERARCHY, 
	
	ACCESS_DENIED,
	
	ADMIN_RIGHTS_REQUIRED,

	SUBJECT_ROLE_NOT_FOUND;
	
	@Override
	public String code() {
		return "RBAC_" + this.name();
	}
}
