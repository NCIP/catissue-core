package com.krishagni.core.tests.testdata;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CreateCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqClinicalDiagnosesEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqCollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegisteredParticipantsEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class CpTestData {

	public static ReqRegisteredParticipantsEvent getReqRegisteredParticipantsEvent() {
		ReqRegisteredParticipantsEvent req = new ReqRegisteredParticipantsEvent();
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		req.setCpId(1L);
		req.setIncludeStats(true);
		req.setStartAt(0);
		req.setMaxResults(100);
		return req;
	}
	
	public static ReqAllCollectionProtocolsEvent getReqAllCollectionProtocolsEvent() {
		ReqAllCollectionProtocolsEvent req = new ReqAllCollectionProtocolsEvent();
		req.setIncludePi(true);
		req.setIncludeStats(true);
		return req;
	}
	
	public static ReqCollectionProtocolEvent getReqCollectionProtocolEvent() {
		ReqCollectionProtocolEvent req = new ReqCollectionProtocolEvent();
		req.setCpId(1L);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		return req;
	}
	
	public static ReqClinicalDiagnosesEvent getReqClinicalDiagnosesEvent() {
		ReqClinicalDiagnosesEvent req = new ReqClinicalDiagnosesEvent();
		req.setCpId(1L);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		return req;
	}
	
	public static UserSummary getUser(Long id, String firstName, String lastName, String loginName) {
		UserSummary user = new UserSummary();
		user.setId(id);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setLoginName(loginName);
		return user;
	}
	
	public static List<UserSummary> getCoordinators(Integer... ids) {
		List<UserSummary> users = new ArrayList<UserSummary>();
		for (Integer i : ids) {
			String firstName = "firstName" + i;
			String lastName = "lastName" + i;
			String loginName = "login" + i + "@login.com";
			users.add(getUser(i.longValue(),firstName, lastName, loginName ));
		}
		
		return users;
	}
	
	public static List<String> getClinicalDiagnoses() {
		List<String> diagnoses = new ArrayList<String>();
		diagnoses.add("One");
		diagnoses.add("Two");
		diagnoses.add("Three");
		return diagnoses;
	}
	
	public static CollectionProtocolDetail getCp() {
		CollectionProtocolDetail cp = new CollectionProtocolDetail();
		cp.setTitle("title");
		cp.setShortTitle("short-title");
		cp.setStartDate(CprTestData.getDate(31,1,2000));
		cp.setPrincipalInvestigator(getUser(1L, "firstName" , "lastName" , "login@login.com"));
		cp.setConsentsWaived(true);
		cp.setIrbId("ASDF-0000");
		cp.setPpidFmt("ppid-format");
		cp.setClinicalDiagnoses(getClinicalDiagnoses());
		cp.setAnticipatedParticipantsCount(100L);
		cp.setDescriptionUrl("www.example.com");
		cp.setSpecimenLabelFmt("specimen-label-format");
		cp.setDerivativeLabelFmt("derivative-format");
		cp.setAliquotLabelFmt("aliquot-format");
		cp.setAliquotsInSameContainer(true);
		cp.setActivityStatus("Active");
		cp.setCoordinators(getCoordinators(2,3));
		cp.setActivityStatus("Active");
		return cp;
	}
	
	public static CreateCollectionProtocolEvent getCreateCollectionProtocolEvent() {
		CreateCollectionProtocolEvent req = new CreateCollectionProtocolEvent();
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		req.setCp(getCp());
		return req;
	}

}
