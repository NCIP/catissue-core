package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateQueryFolderEvent extends RequestEvent {

	private QueryFolderDetails folderDetails;

	public QueryFolderDetails getFolderDetails() {
		return folderDetails;
	}

	public void setFolderDetails(QueryFolderDetails folderDetails) {
		this.folderDetails = folderDetails;
	}
}
