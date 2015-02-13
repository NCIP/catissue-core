package com.krishagni.core.tests.testdata;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;

import edu.emory.mathcs.backport.java.util.Collections;

public class ParticipantTestData {
	public static ParticipantDetail getParticipant() {
		ParticipantDetail participant = new ParticipantDetail();
		participant.setFirstName("firstName");
		participant.setLastName("lastName");
		
		return participant;
	}
	
	public static List<ParticipantMedicalIdentifierNumberDetail> getPmi(String siteName, String mrn) {
		ParticipantMedicalIdentifierNumberDetail pmi = new ParticipantMedicalIdentifierNumberDetail();
		pmi.setMrn(mrn);
		pmi.setSiteName(siteName);
		return Collections.singletonList(pmi);
	}
}
