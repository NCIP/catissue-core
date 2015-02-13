package com.krishagni.catissueplus.core.de.events;

import java.util.List;

public class ShareQueryFolderOp {
	public enum Operation {
		ADD,
		UPDATE,
		REMOVE
	};
	
	private Long folderId;
	
	private List<Long> userIds;
	
	private Operation op;
	
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

	public Operation getOp() {
		return op;
	}

	public void setOp(Operation op) {
		this.op = op;
	}
}
