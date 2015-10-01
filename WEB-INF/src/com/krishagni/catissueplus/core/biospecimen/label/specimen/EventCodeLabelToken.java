package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class EventCodeLabelToken extends AbstractSpecimenLabelToken {

	public EventCodeLabelToken() {
		this.name = "EVENT_CODE";
	}

	@Override
	public String getLabel(Specimen specimen) {
		String code = specimen.getVisit().getCpEvent().getCode();
		if (StringUtils.isBlank(code)) {
			code = StringUtils.EMPTY;
		}

		return code;
	}
}
