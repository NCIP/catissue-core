package com.krishagni.catissueplus.core.biospecimen.events;

import java.io.InputStream;

public class SprDetail {
	
	private Long visitId;

	private InputStream report;
	
	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public InputStream getReport() {
		return report;
	}

	public void setReport(InputStream report) {
		this.report = report;
	}

	
}
