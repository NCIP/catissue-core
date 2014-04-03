package com.krishagni.catissueplus.core.auth.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;


public enum LdapRegistrationErrorCode implements CatissueErrorCode {

	MISSING_ATTR_VALUE(1300, "Required attribute is either empty or null"), 
	INVALID_ATTR_VALUE(1301,"Attribute value is invalid"),
	ERROR_WHILE_LDAP_CONFIGURATION(1302,"Ldap configuration failed"),
	ERROR_WHILE_LDAP_AUTHENTICATION(1303,"Ldap authentication failed"),
	DUPLICATE_LDAP_NAME(1305,"same LDAP Name already present in system"),
	NOT_FOUND(1306,"Attribute value not found in the system."),
	ERRORS(1311, "Please resolve the highlighted errors."),
	AUTHENTICATION_FAILURE(1312,"Authentication has been failed.");

	private int code;

	private String message;

	private LdapRegistrationErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int code() {
		return code;
	}

	public String message() {
		return message;
	}

}
