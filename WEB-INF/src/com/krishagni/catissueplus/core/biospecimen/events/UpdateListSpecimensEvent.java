package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import java.util.*;

public class UpdateListSpecimensEvent extends RequestEvent {
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
