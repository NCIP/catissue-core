package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ShareQueryFolderEvent extends RequestEvent {
	
	private Long folderId;
	
	private List<Long> userIds;
	
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
}
