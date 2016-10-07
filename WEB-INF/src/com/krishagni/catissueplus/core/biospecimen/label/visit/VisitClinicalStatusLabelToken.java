package com.krishagni.catissueplus.core.biospecimen.label.visit;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class VisitClinicalStatusLabelToken extends AbstractVisitLabelToken {

	public VisitClinicalStatusLabelToken() {
		this.name = "CLINICAL_STATUS";
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		String clinicalStatus = visit.getClinicalStatus();
		if (StringUtils.isBlank(clinicalStatus)) {
			clinicalStatus	 = StringUtils.EMPTY;
		}

		return clinicalStatus;
	}
}
