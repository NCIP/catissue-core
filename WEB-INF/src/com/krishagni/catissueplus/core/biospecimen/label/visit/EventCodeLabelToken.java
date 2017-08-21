package com.krishagni.catissueplus.core.biospecimen.label.visit;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;

public class EventCodeLabelToken extends AbstractVisitLabelToken {

	public EventCodeLabelToken() {
		this.name = "EVENT_CODE";
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		String code = visit.getCpEvent().getCode();
		if (StringUtils.isBlank(code)) {
			code = StringUtils.EMPTY;
		}

		return code;
	}
}
