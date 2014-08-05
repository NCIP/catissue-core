
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForCreatedOn implements LabelToken<Specimen> {

	private static String EMPTY_CREATED_ON = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getCreatedOn() != null) {
			return specimen.getCreatedOn().toString();
		}
		return EMPTY_CREATED_ON;
	}

}
