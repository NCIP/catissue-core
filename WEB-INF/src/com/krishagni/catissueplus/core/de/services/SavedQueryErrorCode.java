package com.krishagni.catissueplus.core.de.services;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum SavedQueryErrorCode implements ErrorCode {
	NOT_FOUND,
	
	INVALID_PAGINATION_FILTER,
	
	ID_REQUIRED,
	
	TITLE_REQUIRED,
	
	FOLDER_NAME_REQUIRED,
	
	FOLDER_NOT_FOUND,
	
	OP_NOT_ALLOWED,
	
	USER_NOT_FOUND,
	
	QUERIES_NOT_ACCESSIBLE, 
	
	INVALID_SOURCE_DESTINATION_FOLDERS,
	
	INVALID_SHARE_ACCESS_DETAILS, 
	
	USERS_NOT_ACTIVE,
	
	SRC_DEST_FOLDERS_REQUIRED,
	
	QUERIES_NOT_PART_OF_FOLDER,
	
	SOURCE_FOLDER_NOT_FOUND,
	
	DESTINATION_FOLDER_NOT_FOUND,
	
	MALFORMED,

	CYCLES_IN_QUERY,

	EXPORT_DATA_FILE_NOT_FOUND,

	TOO_BUSY,

	PHI_NOT_ALLOWED_IN_AGR;
	
	@Override
	public String code() {
		return "QUERY_" + this.name();
	}
}
