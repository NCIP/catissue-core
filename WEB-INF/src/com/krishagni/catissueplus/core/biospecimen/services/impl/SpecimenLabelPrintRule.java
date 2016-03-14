package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.LabelPrintRule;

public class SpecimenLabelPrintRule extends LabelPrintRule {
	private String cpShortTitle;

	private String visitSite;

	private String specimenClass;
	
	private String specimenType;
	
	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public String getVisitSite() {
		return visitSite;
	}

	public void setVisitSite(String visitSite) {
		this.visitSite = visitSite;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public boolean isApplicableFor(Specimen specimen, User user, String ipAddr) {
		if (!super.isApplicableFor(user, ipAddr)) {
			return false;
		}
		
		if (!isWildCard(cpShortTitle) && !specimen.getCollectionProtocol().getShortTitle().equals(cpShortTitle)) {
			return false;
		}

		if (!isWildCard(visitSite) && !specimen.getVisit().getSite().getName().equals(visitSite)) {
			return false;
		}

		if (!isWildCard(specimenClass) && !specimen.getSpecimenClass().equals(specimenClass)) {
			return false;
		}
		
		if (!isWildCard(specimenType) && !specimen.getSpecimenType().equals(specimenType)) {
			return false;
		}
		
		return true;
	}

	public String toString() {
		return new StringBuilder(super.toString())
			.append(", cp = ").append(getCpShortTitle())
			.append(", visit site = ").append(getVisitSite())
			.append(", specimen class = ").append(getSpecimenClass())
			.append(", specimen type = ").append(getSpecimenType())
			.toString();
	}
}
