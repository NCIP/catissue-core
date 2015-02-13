package com.krishagni.catissueplus.core.de.events;

import java.util.List;

public class UpdateFolderQueriesOp {
	public enum Operation {
		ADD,
		UPDATE,
		REMOVE
	}

	private Long folderId;
	
	private List<Long> queries;
	
	private Operation op;

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public List<Long> getQueries() {
		return queries;
	}

	public void setQueries(List<Long> queries) {
		this.queries = queries;
	}

	public Operation getOp() {
		return op;
	}

	public void setOp(Operation op) {
		this.op = op;
	}
}
