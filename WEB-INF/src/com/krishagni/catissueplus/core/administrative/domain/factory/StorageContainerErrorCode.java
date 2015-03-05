
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum StorageContainerErrorCode implements ErrorCode {
	NOT_FOUND,
	
	NAME_REQUIRED,
	
	DUP_NAME,
	
	DUP_BARCODE,
	
	INVALID_DIMENSION_CAPACITY,
	
	INVALID_DIMENSION_LABELING_SCHEME,
	
	REQUIRED_SITE_OR_PARENT_CONT,
	
	INVALID_SITE_AND_PARENT_CONT,
	
	NO_FREE_SPACE,
	
	CANNOT_SHRINK_CONTAINER,
	
	PARENT_CONT_NOT_FOUND,
	
	RESTRICTIVE_SPECIMEN_CLASS_AND_TYPE,
	
	RESTRICTIVE_CP,
	
	INVALID_NUMBER_POSITION,
	
	INVALID_ALPHA_POSITION,
	
	INVALID_ROMAN_POSITION,
	
	REF_ENTITY_FOUND,
	
	HIERARCHY_CONTAINS_CYCLE,
	
	CANNOT_HOLD_SPECIMEN,
	
	INVALID_POSITIONS;

	@Override
	public String code() {
		return "STORAGE_CONTAINER_" + this.name();
	}

}
