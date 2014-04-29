package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqFolderQueriesEvent extends RequestEvent {
	
	private Long folderId;
	
	public ReqFolderQueriesEvent(Long folderId) {
		this.folderId = folderId;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
}
