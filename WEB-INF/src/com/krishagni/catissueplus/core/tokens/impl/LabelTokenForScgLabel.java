
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForScgLabel implements LabelToken<SpecimenCollectionGroup> {

	private static final String EMPTY_SCG_LABEL = "";

	@Override
	public String getTokenValue(SpecimenCollectionGroup scg) {
		if (scg.getName() != null) {
			return scg.getName();
		}
		return EMPTY_SCG_LABEL;
	}
}