package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;
import java.util.Map;

public class RequestListSpecimensDetail {
	private Long listId;

	private List<Map<String, Object>> requestForms;

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public List<Map<String, Object>> getRequestForms() {
		return requestForms;
	}

	public void setRequestForms(List<Map<String, Object>> requestForms) {
		this.requestForms = requestForms;
	}
}
