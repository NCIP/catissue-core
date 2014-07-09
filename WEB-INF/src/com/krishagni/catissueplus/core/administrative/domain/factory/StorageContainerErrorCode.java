
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum StorageContainerErrorCode implements CatissueErrorCode {

	MISSING_ATTR_VALUE(1800, "Required attribute is either empty or null."), INVALID_ATTR_VALUE(1801,
			"Attribute value is invalid."), AUTO_GENERATED_LABEL(1802, "Please do not specify label,it is auto generated."), AUTO_GENERATED_BARCODE(
			1803, "Please do not specify barcode,it is auto generated."), ERRORS(1811,
			"Please resolve the highlighted errors."), BAD_REQUEST(1815, "Bad Request"), DUPLICATE_CONTAINER_NAME(1818,
			"Container with the same name is already present in the system."), REFERENCED_ATTRIBUTE(1820,"Container having child containers");

	private int code;

	private String message;

	private StorageContainerErrorCode(int code, String message) {
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
