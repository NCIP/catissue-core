package com.krishagni.core.tests.testdata;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ParticipantTestData {
	public static ParticipantDetail getParticipant() {
		ParticipantDetail participant = new ParticipantDetail();
		participant.setFirstName("firstName");
		participant.setLastName("lastName");
		
		return participant;
	}
	
	public static List<PmiDetail> getPmi(String siteName, String mrn) {
		PmiDetail pmi = new PmiDetail();
		pmi.setMrn(mrn);
		pmi.setSiteName(siteName);
		return Collections.singletonList(pmi);
	}
}
