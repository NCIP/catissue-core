package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

public class UpdateListSpecimensOp {
	public enum Operation {
		ADD,
		UPDATE,
		REMOVE
	}

	private Long listId;
	
	private List<Long> ids;
	
	private Operation op;

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public List<Long> getSpecimens() {
		return ids;
	}

	public void setSpecimens(List<Long> ids) {
		this.ids = ids;
	}

	public Operation getOp() {
		return op;
	}

	public void setOp(Operation op) {
		this.op = op;
	}
}
