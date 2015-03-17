package com.krishagni.catissueplus.core.biospecimen.label.visit;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class PpidLabelToken extends AbstractVisitLabelToken {
	
	public PpidLabelToken() {
		this.name = "PPI";
	}

	@Override
	public String getLabel(Visit visit) {
		return visit.getRegistration().getPpid();
	}
}
