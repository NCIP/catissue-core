
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForCpShortTitle implements LabelToken<SpecimenCollectionGroup> {

	private static final int MAX_TITLE_SIZE_IN_LABEL = 30;

	@Override
	public String getTokenValue(SpecimenCollectionGroup scg) {
		String cpShortTitle = scg.getCollectionProtocolRegistration().getCollectionProtocol().getShortTitle();
		return cpShortTitle.length() > MAX_TITLE_SIZE_IN_LABEL
				? cpShortTitle.substring(0, (MAX_TITLE_SIZE_IN_LABEL - 1))
				: cpShortTitle;
	}

}
