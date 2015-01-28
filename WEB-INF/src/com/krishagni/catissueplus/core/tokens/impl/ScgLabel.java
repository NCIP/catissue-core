
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class ScgLabel implements LabelToken<Visit> {

	private static final String EMPTY_SCG_LABEL = "";

	@Override
	public String getTokenValue(Visit scg) {
		if (scg.getName() != null) {
			return scg.getName();
		}
		return EMPTY_SCG_LABEL;
	}
}