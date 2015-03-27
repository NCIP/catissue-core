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
	
	INSUFFICIENT_USER_DETAILS, 
	
	GROUP_ID_REQUIRED, 
	
	SUBJECT_NOT_FOUND,
	
	GROUP_NOT_FOUND,
	
	CYCLE_DETECTED_IN_HIERARCHY, 
	
	CP_SITE_COMBI_NOT_FOUND,
	
	ACCESS_DENIED,
	
	ADMIN_PRIVILEGES_REQUIRED;
	
	@Override
	public String code() {
		return "RBAC_" + this.name();
	}
}
