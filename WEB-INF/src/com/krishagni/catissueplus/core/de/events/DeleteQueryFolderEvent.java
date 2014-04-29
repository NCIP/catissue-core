package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class DeleteQueryFolderEvent extends RequestEvent {
	
	private Long folderId;
	
	public DeleteQueryFolderEvent(Long folderId) {
		this.folderId = folderId;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
}
