package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class QueryFolderCreatedEvent extends ResponseEvent {

	private QueryFolderDetails folderDetails;
		
	public QueryFolderDetails getFolderDetails() {
		return folderDetails;
	}

	public void setFolderDetails(QueryFolderDetails folderDetails) {
		this.folderDetails = folderDetails;
	}

	public static QueryFolderCreatedEvent ok(QueryFolderDetails folderDetails) {
		QueryFolderCreatedEvent resp = new QueryFolderCreatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFolderDetails(folderDetails);
		return resp;
	}
	
	public static QueryFolderCreatedEvent badRequest(ObjectCreationException e) {
		return errorResp(EventStatus.BAD_REQUEST, e.getMessage(), e);
	}
	
	public static QueryFolderCreatedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static QueryFolderCreatedEvent errorResp(EventStatus status, String message, Throwable t) {
		QueryFolderCreatedEvent resp = new QueryFolderCreatedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		if (t instanceof ObjectCreationException) {
			ObjectCreationException oce = (ObjectCreationException)t;
			resp.setErroneousFields(oce.getErroneousFields());
		}
		
		return resp;		
	}
}
