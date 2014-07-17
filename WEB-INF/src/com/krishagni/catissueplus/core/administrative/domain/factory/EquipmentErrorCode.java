
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum EquipmentErrorCode implements CatissueErrorCode {
	
	DUPLICATE_DISPLAY_NAME(1800, "Equipment with the same display name is already present in the system."), 
	INVALID_ATTR_VALUE(1801,  "Attribute value is invalid."), 
	ERRORS(1802, "Please resolve the errors listed in error list."), 
	BAD_REQUEST(1803, "Bad Request"), 
	MISSING_ATTR_VALUE(1804, "Required attribute is either empty or null"), 
	ACTIVE_CHILDREN_FOUND(1805, "Cannot be deleted, Active childrens found."),
	DUPLICATE_DEVICE_NAME(1806, "Equipment with the same device name is already present in the system."),
	DUPLICATE_EQUIPMENT_ID(1807,"Equipment with the same equipment id is already present in the system.");
	
	private int code;

	private String message;

	private EquipmentErrorCode(int code, String message) {
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
