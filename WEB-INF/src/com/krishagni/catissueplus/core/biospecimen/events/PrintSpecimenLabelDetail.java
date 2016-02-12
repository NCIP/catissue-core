package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

public class PrintSpecimenLabelDetail {
	private List<Long> specimenIds = new ArrayList<Long>();
	
	private List<String> specimenLabels = new ArrayList<String>();
	
	private Long visitId;
	
	private String visitName;
	
	private int numCopies = 1;

	public List<Long> getSpecimenIds() {
		return specimenIds;
	}

	public void setSpecimenIds(List<Long> specimenIds) {
		this.specimenIds = specimenIds;
	}

	public List<String> getSpecimenLabels() {
		return specimenLabels;
	}

	public void setSpecimenLabels(List<String> specimenLabels) {
		this.specimenLabels = specimenLabels;
	}

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

	public int getNumCopies() {
		return numCopies;
	}

	public void setNumCopies(int numCopies) {
		this.numCopies = numCopies;
	} 
}
