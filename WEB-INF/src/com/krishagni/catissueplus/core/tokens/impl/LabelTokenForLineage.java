
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForLineage implements LabelToken<Specimen> {

	private static final String EMPTY_LINEAGE = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getLineage() != null) {
			return specimen.getLineage();
		}
		return EMPTY_LINEAGE;
	}

}
