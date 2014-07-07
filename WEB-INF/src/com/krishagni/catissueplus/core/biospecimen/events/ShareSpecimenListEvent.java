package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ShareSpecimenListEvent extends RequestEvent {
	public enum Operation {
		ADD,
		UPDATE,
		REMOVE
	};
	
	private Long listId;
	
	private List<Long> userIds;
	
	private Operation op;
	
	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
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
