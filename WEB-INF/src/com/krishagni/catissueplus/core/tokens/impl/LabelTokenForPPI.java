
package com.krishagni.catissueplus.core.tokens.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class LabelTokenForPPI implements LabelToken<Specimen> {

	public String getTokenValue(Specimen specimen) {
		return specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
	}
}
