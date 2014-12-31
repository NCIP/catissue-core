package com.krishagni.core.tests.testdata;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierResponseDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMedicalIdentifierNumberDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;

import edu.wustl.common.beans.SessionDataBean;

public class CprTestData {
	public static ParticipantDetail getParticipant() {
		ParticipantDetail p = new ParticipantDetail();
		p.setFirstName("default_first_name");
		p.setLastName("default_last_name");
		p.setMiddleName("default_middle_name");
		p.setBirthDate(getDate(21,10,2012));
		p.setGender("MALE");
		p.setRace(new HashSet<String>());
		p.getRace().add("Asian");
		p.setActivityStatus("Active");
		p.setPmis(populatePmis());
		p.setVitalStatus("Alive");
		p.setSexGenotype("XX");
		p.setSsn("333-22-4444");
		p.setEthnicity("Canadian");
		p.setEmpi("default-empi-id");
		return p;
	}
	
	private static List<ParticipantMedicalIdentifierNumberDetail> populatePmis() {
		List<ParticipantMedicalIdentifierNumberDetail> pmis = new ArrayList<ParticipantMedicalIdentifierNumberDetail>();
		
		ParticipantMedicalIdentifierNumberDetail pmi1 = new ParticipantMedicalIdentifierNumberDetail();
		pmi1.setSiteName("SITE1");
		pmi1.setMrn("PMI1");
		pmis.add(pmi1);
		
		ParticipantMedicalIdentifierNumberDetail pmi2 = new ParticipantMedicalIdentifierNumberDetail();
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
		cpr.setRegistrationDate(getDate(31,1,2001));
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
		detail.setConsentSignatureDate(getDate(31,1,2001));
		
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
	
	public static Date getDate(int day, int month, int year) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static SessionDataBean getSessionDataBean() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}

	public static ReqVisitsEvent getReqVisitsEvent() {
		ReqVisitsEvent req = new ReqVisitsEvent();
		req.setSessionDataBean(getSessionDataBean());
		req.setCprId(1L);
		req.setIncludeStats(true);
		return req;
	}
	
	public static ReqRegistrationEvent getReqRegistrationEvent() {
		ReqRegistrationEvent req = new ReqRegistrationEvent();
		req.setSessionDataBean(getSessionDataBean());
		return req;
	}
	
	public static VisitDetail getVisitDetail() {
		VisitDetail visit = new VisitDetail();
		visit.setCprId(1L);
		visit.setClinicalDiagnosis("test-daiagnosis");
		visit.setClinicalStatus("test-status");
		visit.setCpeId(1L);
		visit.setSurgicalPathologyNumber("test-pathology");
		visit.setVisitDate(getDate(21, 1, 2012));
		visit.setVisitSite("SITE1");
		visit.setVisitStatus("Completed");
		return visit;
	}
	
	public static AddVisitEvent getAddVisitEvent() {
		AddVisitEvent req = new AddVisitEvent();
		req.setSessionDataBean(getSessionDataBean());
		req.setCprId(1L);
		req.setVisit(getVisitDetail());
		return req;
	}
}
