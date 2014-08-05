
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForCollectionStatus implements LabelToken<Specimen> {

	private static String EMPTY_COLLECTION_STATUS = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getCollectionStatus() == null) {
			return specimen.getCollectionStatus();
		}
		return EMPTY_COLLECTION_STATUS;
	}

}
