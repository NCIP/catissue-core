package com.krishagni.catissueplus.core.de.services;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;

public enum SavedQueryErrorCode implements CatissueErrorCode {
	QUERY_NOT_FOUND(1300, "Mentioned query-id not found!"),
	
	QUERY_ID_FOUND(1301, "Query-id should not be passed in create query event"),
	
	INVALID_PAGINATION_FILTER(1302, "Invalid pagination filter"),
	
	QUERY_ID_REQUIRED(1303,"Query-id is required for this operaiton."),
	
	QUERY_TITLE_NULL(1304, "Query title cannot be null for save/update"),
	
	FOLDER_ID_FOUND(1305, "Folderid should not be passed in create folder request"),
	
	INVALID_FOLDER_NAME(1306, "Folder-name provided in reqeust is either null or empty"),
	
	FOLDER_ID_REQUIRED(1307, "Folder id is required for this folder operation"),
	
	USER_NOT_AUTHORISED(1308, "Current user is not authorised to perform this action"),
	
	USER_ID_REQUIRED(1310, "Userid not found in request"),
	
	QUERIES_NOT_ACCESSIBLE(1311, "Some of the queries in request doesn't belong to current user."), 
	
	INVALID_USER_ID(1312, "Invalid userid provided in request!"),
	
	INVALID_SOURCE_DESTINATION_FOLDERS(1313, "Source/Destination folders provided are invalid."),
	
	INVALID_SHARE_ACCESS_DETAILS(1314, "Invalid share access details provided."), 
	
	USERS_NOT_ACTIVE(1315, "Provided users are not in active state"),
	
	SRC_DEST_FOLDERS_REQUIRED(1316, "Destination and Source folders are required for this operation."),
	
	FOLDER_DOESNT_EXISTS(1317, "Given folder doesn't exist!"),
	
	QUERIES_NOT_PART_OF_FOLDER(1318, "Some of the queires do not belong to given folder"),
	
	INVALID_SOURCE_FOLDER(1319, "Source folder is invalid."),
	
	INVALID_DESTINATION_FOLDER(1320, "Destination folder is invalid.");
	
	private int code;

	private String message;

	private SavedQueryErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	@Override
	public int code() {
		return code;
	}

	@Override
	public String message() {
		return message;
	}
}
