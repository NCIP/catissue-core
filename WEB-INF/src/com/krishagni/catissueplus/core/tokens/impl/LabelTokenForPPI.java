
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForPPI implements LabelToken<Specimen> {

	private static final String EMPTY_PPI = "";

	public String getTokenValue(Specimen specimen) {
		if (specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getProtocolParticipantIdentifier() != null) {
			return specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
					.getProtocolParticipantIdentifier();
		}
		return EMPTY_PPI;
	}
}
