package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.domain.LabelPrintRule;

public class SpecimenLabelPrintRule extends LabelPrintRule {
	private String specimenClass;
	
	private String specimenType;
	
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
		
		if (!isWildCard(specimenClass) && !specimen.getSpecimenClass().equals(specimenClass)) {
			return false;
		}
		
		if (!isWildCard(specimenType) && !specimen.getSpecimenType().equals(specimenType)) {
			return false;
		}
		
		return true;
	}	
}