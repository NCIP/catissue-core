
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForTissueSite implements LabelToken<Specimen> {

	private static final String EMPTY_TISSUE_SITE = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getTissueSite() != null) {
			return specimen.getTissueSite();
		}
		return EMPTY_TISSUE_SITE;
	}

}
