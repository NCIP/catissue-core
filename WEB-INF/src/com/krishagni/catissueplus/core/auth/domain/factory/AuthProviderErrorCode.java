package com.krishagni.catissueplus.core.auth.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;


public enum AuthProviderErrorCode implements ErrorCode {
	TYPE_NOT_SPECIFIED,
	
	INVALID_TYPE,
	
	LDAP_NOT_FOUND,
	
	NOT_FOUND,
	
	DOMAIN_NOT_SPECIFIED,
	
	DUP_DOMAIN_NAME,
	
	IMPL_NOT_SPECIFIED,
	
	INVALID_AUTH_IMPL,
	
	LDAP_CONFIG_NOT_SPECIFIED,
	
	LDAP_BASE_DIR_NOT_SPECIFIED,
	
	LDAP_FILTER_NOT_SPECIFIED,
	
	LDAP_BIND_PASSWORD_NOT_SPECIFIED,
	
	LDAP_PORT_NOT_SPECIFIED,
	
	LDAP_HOST_NOT_SPECIFIED,
	
	LDAP_BIND_USER_NOT_SPECIFIED,
	
	LDAP_DIR_CTXT_NOT_SPECIFIED,
	
	LDAP_ID_NOT_SPECIFIED,
	
	LDAP_INVALID_CONFIG;

	@Override
	public String code() {
		return "AUTH_PROVIDER_" + this.name();
	}
}
