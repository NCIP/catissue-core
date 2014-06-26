package com.krishagni.catissueplus.core.printer.printRule.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;


public enum PrintRuleErrorCode implements CatissueErrorCode {
	MISSING_ATTR_VALUE(2500, "Required attribute is either empty or null."), 
	INVALID_ATTR_VALUE(2501,"Attribute value is invalid."), 
	DUPLICATE_PRINT_RULE(2502,"Print rule with the same name is already present in the system."),
	NOT_FOUND(2503,"Attribute value not found in the system."),
	ERRORS(2504, "Please resolve the highlighted errors."),
	BAD_REQUEST(2505, "Bad Request");

	private int code;

	private String message;

	private PrintRuleErrorCode(int code, String message) {
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
