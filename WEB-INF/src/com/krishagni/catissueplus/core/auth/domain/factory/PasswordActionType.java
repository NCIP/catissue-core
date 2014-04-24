
package com.krishagni.catissueplus.core.auth.domain.factory;

public enum PasswordActionType {
	CHANGE("change"), SET("set");

	private String value;

	private PasswordActionType(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}

}
