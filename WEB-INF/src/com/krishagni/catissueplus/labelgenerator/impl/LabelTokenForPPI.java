
package com.krishagni.catissueplus.labelgenerator.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.labelgenerator.LabelToken;

public class LabelTokenForPPI implements LabelToken {

	public String getTokenValue(Object object) {
		Specimen objSpecimen = (Specimen) object;
		return objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
				.getProtocolParticipantIdentifier();
	}
}
