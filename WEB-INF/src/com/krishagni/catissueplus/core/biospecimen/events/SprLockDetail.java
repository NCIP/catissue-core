package com.krishagni.catissueplus.core.biospecimen.events;

public class SprLockDetail {
	private Long visitId;
	
	private String visitName;
	
	private boolean lock;

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public String getVisitName() {
		return visitName;
	}

	public void setVisitName(String visitName) {
		this.visitName = visitName;
	}

	public boolean getLock() {
		return lock;
	}

	public void setLock(Boolean lock) {
		this.lock = lock;
	}
	
}
