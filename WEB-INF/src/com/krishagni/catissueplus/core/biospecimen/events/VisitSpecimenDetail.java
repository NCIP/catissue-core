package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class VisitSpecimenDetail {
	private VisitDetail visit; 
	
	private List<SpecimenDetail> specimens;

	public VisitSpecimenDetail() {

	}

	public VisitSpecimenDetail(Visit visit) {
		this.visit = VisitDetail.from(visit);
		this.specimens = SpecimenDetail.from(visit.getOrderedTopLevelSpecimens());
	}

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
