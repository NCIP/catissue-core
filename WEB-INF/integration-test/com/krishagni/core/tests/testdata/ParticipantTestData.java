package com.krishagni.core.tests.testdata;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.MatchParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantDetailEvent;

import edu.emory.mathcs.backport.java.util.Collections;

public class ParticipantTestData {

	public static ParticipantDetail getParticipant() {
		ParticipantDetail participant = new ParticipantDetail();
		participant.setFirstName("firstName");
		participant.setLastName("lastName");
		
		return participant;
	}
	
	public static MatchParticipantEvent getMatchParticipantEvent() {
		MatchParticipantEvent req = new MatchParticipantEvent();
		req.setParticipantDetail(getParticipant());
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		return req;
	}
	
	public static List<ParticipantMedicalIdentifierNumberDetail> getPmi(String siteName, String mrn) {
		ParticipantMedicalIdentifierNumberDetail pmi = new ParticipantMedicalIdentifierNumberDetail();
		pmi.setMrn(mrn);
		pmi.setSiteName(siteName);
		return Collections.singletonList(pmi);
	}
	
	public static ReqParticipantDetailEvent getReqParticipantDetailEvent() {
		ReqParticipantDetailEvent req = new ReqParticipantDetailEvent();
		req.setParticipantId(1L);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		return req;
	}
}
