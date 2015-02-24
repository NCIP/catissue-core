package com.krishagni.catissueplus.core.administrative.events;

public class DeleteInstituteOp {
	
	private Long id;
	
	private boolean close;

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
