package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

public class UpdateListSpecimensOp {
	public enum Operation {
		ADD,
		UPDATE,
		REMOVE
	}

	private Long listId;
	
	private List<String> labels;
	
	private Operation op;

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public List<String> getSpecimens() {
		return labels;
	}

	public void setSpecimens(List<String> labels) {
		this.labels = labels;
	}

	public Operation getOp() {
		return op;
	}

	public void setOp(Operation op) {
		this.op = op;
	}
}
