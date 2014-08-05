
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForQuantity implements LabelToken<Specimen> {

	@Override
	public String getTokenValue(Specimen specimen) {
		return specimen.getAvailableQuantity().toString();
	}

}
