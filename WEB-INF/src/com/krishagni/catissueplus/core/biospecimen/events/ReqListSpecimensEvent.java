package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqListSpecimensEvent extends RequestEvent {	

	private Long listId;
	
	public ReqListSpecimensEvent(Long listId) {
		this.listId = listId;
	}

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}
}
