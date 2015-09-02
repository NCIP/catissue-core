package com.krishagni.catissueplus.core.biospecimen.events;

public class SprRequestDetail {
	
	private Long visitId;
	
	private String visitName;
	
	private FileType type;

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

	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.type = type;
	}
}
