package com.krishagni.catissueplus.core.common.events;

public class DeleteEntityOp {
	
	private Long id;
	
	private boolean close;
	
	public DeleteEntityOp(Long id, boolean close) {
		this.id = id;
		this.close = close;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isClose() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}

}
