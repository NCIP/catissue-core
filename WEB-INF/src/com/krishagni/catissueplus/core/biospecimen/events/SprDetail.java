package com.krishagni.catissueplus.core.biospecimen.events;

import java.io.InputStream;

public class SprDetail {
	
	private Long visitId;

	private String sprName;
	
	private InputStream sprIn;
	
	private String sprContent;
	
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

	public InputStream getSprIn() {
		return sprIn;
	}

	public void setSprIn(InputStream spr) {
		this.sprIn = spr;
	}

	public String getSprContent() {
		return sprContent;
	}

	public void setSprContent(String sprContent) {
		this.sprContent = sprContent;
	}
	
}
