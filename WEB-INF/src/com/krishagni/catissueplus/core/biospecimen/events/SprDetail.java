package com.krishagni.catissueplus.core.biospecimen.events;

import java.io.InputStream;

public class SprDetail {
	
	private Long visitId;

	private String sprName;
	
	private InputStream spr;
	
	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public String getSprName() {
		return sprName;
	}

	public void setSprName(String sprName) {
		this.sprName = sprName;
	}

	public InputStream getSpr() {
		return spr;
	}

	public void setSpr(InputStream spr) {
		this.spr = spr;
	}
	
}
