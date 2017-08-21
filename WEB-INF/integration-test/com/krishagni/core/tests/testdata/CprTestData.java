package com.krishagni.core.tests.testdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierResponseDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;

public class CprTestData {
	public static ParticipantDetail getParticipant() {
		ParticipantDetail p = new ParticipantDetail();
		p.setFirstName("default_first_name");
		p.setLastName("default_last_name");
		p.setMiddleName("default_middle_name");
		p.setBirthDate(CommonUtils.getDate(21,10,2012));
		p.setGender("MALE");
		p.setRaces(new HashSet<String>());
		p.getRaces().add("Asian");
		p.setActivityStatus("Active");
		p.setPmis(populatePmis());
		p.setVitalStatus("Alive");
		p.setSexGenotype("XX");
		p.setSsn("333-22-4444");
		p.setEthnicity("Canadian");
		p.setEmpi("default-empi-id");
		return p;
	}
	
	private static List<PmiDetail> populatePmis() {
		List<PmiDetail> pmis = new ArrayList<PmiDetail>();
		
		PmiDetail pmi1 = new PmiDetail();
		pmi1.setSiteName("SITE1");
		pmi1.setMrn("PMI1");
		pmis.add(pmi1);
		
		PmiDetail pmi2 = new PmiDetail();
		pmi2.setSiteName("SITE2");
		pmi2.setMrn("PMI2");
		pmis.add(pmi2);
		
		return pmis;
	}
	
	public static CollectionProtocolRegistrationDetail getCprDetail() {
		CollectionProtocolRegistrationDetail cpr = new CollectionProtocolRegistrationDetail();
		cpr.setBarcode("test-barcode");
		cpr.setParticipant(new ParticipantDetail());
		cpr.getParticipant().setId(1L);
		cpr.setPpid("default-gen-ppid");
		cpr.setCpId(1L);
		cpr.setRegistrationDate(CommonUtils.getDate(31,1,2001));
		cpr.setConsentDetails(getConsentDetail());
		return cpr;
	}
	
	public static CollectionProtocolRegistrationDetail getCprDetailForCreateAndRegisterParticipantWithPmi() {
		CollectionProtocolRegistrationDetail cpr = getCprDetail();
		cpr.setParticipant(getParticipant());
		return cpr;
	}
	
	public static CollectionProtocolRegistrationDetail getCprDetailForCreateAndRegisterParticipant() {
		CollectionProtocolRegistrationDetail cpr = getCprDetail();
		cpr.setParticipant(getParticipant());
		cpr.getParticipant().getPmis().clear();
		return cpr;
	}
	
	private static ConsentDetail getConsentDetail() {
		ConsentDetail detail = new ConsentDetail();
		detail.setWitnessName("admin@admin.com");
		detail.setConsentDocumentUrl("www.exampleurl.com");
		detail.setConsentSignatureDate(CommonUtils.getDate(31,1,2001));
		
		ConsentTierResponseDetail ctd = new ConsentTierResponseDetail();
		ctd.setConsentStatment("CONSENT1");
		ctd.setParticipantResponse("yes");
		detail.getConsentTierResponses().add(ctd);
		
		ctd = new ConsentTierResponseDetail();
		ctd.setConsentStatment("CONSENT2");
		ctd.setParticipantResponse("no");
		detail.getConsentTierResponses().add(ctd);
		
		ctd = new ConsentTierResponseDetail();
		ctd.setConsentStatment("CONSENT3");
		ctd.setParticipantResponse("may be");
		detail.getConsentTierResponses().add(ctd);
		
		return detail;
	}
}
