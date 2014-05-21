
package com.krishagni.catissueplus.core.auth.domain.factory;

public enum AuthenticationType {
	CATISSUE("catissue"), LDAP("ldap"), CUSTOM("custom");

	public String value;

	private AuthenticationType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

	public static Boolean isValidAuthType (String authType) {
		for (AuthenticationType type : AuthenticationType.values()) {
			if (type.value().equalsIgnoreCase(authType)) {
				return true;
			}
		}
		return false;
	}
}
