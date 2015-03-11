package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

public class VisitSpecimenDetail {
	private VisitDetail visit; 
	
	private List<SpecimenDetail> specimens;

	public VisitDetail getVisit() {
		return visit;
	}

	public void setVisit(VisitDetail visit) {
		this.visit = visit;
	}

	public List<SpecimenDetail> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenDetail> specimens) {
		this.specimens = specimens;
	}
}
