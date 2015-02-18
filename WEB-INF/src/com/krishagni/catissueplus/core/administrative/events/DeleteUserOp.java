package com.krishagni.catissueplus.core.administrative.events;

public class DeleteUserOp {
	private Long id;
	
	private boolean close;
	
	public DeleteUserOp(Long id, boolean close) {
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
