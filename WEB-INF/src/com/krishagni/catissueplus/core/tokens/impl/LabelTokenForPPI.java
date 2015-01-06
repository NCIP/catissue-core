
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForPPI implements LabelToken<Specimen> {

	private static final String EMPTY_PPI = "";

	public String getTokenValue(Specimen specimen) {
		if (specimen.getSpecimenCollectionGroup().getRegistration().getProtocolParticipantIdentifier() != null) {
			return specimen.getSpecimenCollectionGroup().getRegistration()
					.getProtocolParticipantIdentifier();
		}
		return EMPTY_PPI;
	}
}
