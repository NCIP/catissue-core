package com.krishagni.catissueplus.core.biospecimen.label.visit;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.PvAttributes;
import com.krishagni.catissueplus.core.common.util.PvUtil;

public class VisitClinicalStatusAbbrToken extends AbstractVisitLabelToken {

	public VisitClinicalStatusAbbrToken() {
		this.name = "CLINICAL_STATUS_ABBR";
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		return PvUtil.getInstance().getAbbr(PvAttributes.CLINICAL_STATUS, visit.getClinicalStatus(), StringUtils.EMPTY);
	}
}
