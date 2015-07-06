package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.LabelPrintRule;

public class VisitLabelPrintRule extends LabelPrintRule {
	private String cpShortTitle;
	
	public String getCpShortTitle() {
		return cpShortTitle;
	}
	
	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public boolean isApplicableFor(Visit visit, User user, String ipAddr) {
		if (!super.isApplicableFor(user, ipAddr)) {
			return false;
		}
		
		if (!isWildCard(cpShortTitle) && !visit.getCollectionProtocol().getShortTitle().equals(cpShortTitle)) {
			return false;
		}
		
		return true;
	}	
}
