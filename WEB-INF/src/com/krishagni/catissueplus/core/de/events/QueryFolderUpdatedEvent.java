package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryFolderUpdatedEvent extends ResponseEvent {

	private QueryFolderDetails folderDetails;

	public QueryFolderDetails getFolderDetails() {
		return folderDetails;
	}

	public void setFolderDetails(QueryFolderDetails folderDetails) {
		this.folderDetails = folderDetails;
	}
	
	public static QueryFolderUpdatedEvent ok(QueryFolderDetails folderDetails) {
		QueryFolderUpdatedEvent resp = new QueryFolderUpdatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFolderDetails(folderDetails);
		return resp;
	}
	
	public static QueryFolderUpdatedEvent badRequest(CatissueErrorCode errorCode) {
		QueryFolderUpdatedEvent resp = new QueryFolderUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(errorCode.message());
		return resp;		
	}
	
	public static QueryFolderUpdatedEvent badRequest(ObjectCreationException oce) {
		QueryFolderUpdatedEvent resp = new QueryFolderUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setErroneousFields(oce.getErroneousFields());
		resp.setException(oce);
		resp.setMessage(oce.getMessage());
		return resp;		
	}	
	
	public static QueryFolderUpdatedEvent serverError(String message, Throwable t) {
		QueryFolderUpdatedEvent resp = new QueryFolderUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
