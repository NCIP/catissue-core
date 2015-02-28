
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForPPI implements LabelToken<Specimen> {

	private static final String EMPTY_PPI = "";

	public String getTokenValue(Specimen specimen) {
		if (specimen.getVisit().getRegistration().getPpid() != null) {
			return specimen.getVisit().getRegistration()
					.getPpid();
		}
		return EMPTY_PPI;
	}
}
