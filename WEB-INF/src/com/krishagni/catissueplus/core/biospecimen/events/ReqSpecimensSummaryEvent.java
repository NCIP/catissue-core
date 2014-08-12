package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

public class ReqSpecimensSummaryEvent {
	
	private List<String> specimenLabels;

	public List<String> getSpecimenLabels() {
		return specimenLabels;
	}

	public void setSpecimenLabels(List<String> specimenLabels) {
		this.specimenLabels = specimenLabels;
	}
}
