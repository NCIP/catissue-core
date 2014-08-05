
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForSpecimenType implements LabelToken<Specimen> {

	private static final String EMPTY_SPECIMEN_TYPE = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getSpecimenType() != null) {
			return specimen.getSpecimenType();
		}
		return EMPTY_SPECIMEN_TYPE;
	}

}
