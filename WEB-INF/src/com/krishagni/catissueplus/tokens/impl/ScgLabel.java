
package com.krishagni.catissueplus.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.tokens.LabelToken;

public class ScgLabel implements LabelToken<SpecimenCollectionGroup> {

	@Override
	public String getTokenValue(SpecimenCollectionGroup scg) {
		return scg.getName();
	}

}
