package com.krishagni.catissueplus.core.administrative.events;

public class DeleteInstituteOp {
	
	private Long id;
	
	private Boolean isClosed;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}
	
}
