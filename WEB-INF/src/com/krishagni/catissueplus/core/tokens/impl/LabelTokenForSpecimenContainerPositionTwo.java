
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForSpecimenContainerPositionTwo implements LabelToken<Specimen> {

	private static final String EMPTY_POSITION = "";

	@Override
	public String getTokenValue(Specimen specimen) {
		if (specimen.getSpecimenPosition().getPositionDimensionTwoString() != null) {
			return specimen.getSpecimenPosition().getPositionDimensionTwoString();
		}
		return EMPTY_POSITION;
	}

}