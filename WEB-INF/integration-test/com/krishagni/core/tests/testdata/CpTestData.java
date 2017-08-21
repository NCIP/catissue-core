package com.krishagni.core.tests.testdata;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class CpTestData {
	public static List<UserSummary> getCoordinators(Integer... ids) {
		List<UserSummary> users = new ArrayList<UserSummary>();
		for (Integer i : ids) {
			String firstName = "firstName" + i;
			String lastName = "lastName" + i;
			String loginName = "login" + i + "@login.com";
			users.add(CommonUtils.getUser(i.longValue(),firstName, lastName, loginName ));
		}
		
		return users;
	}
	
	public static CollectionProtocolDetail getCp() {
		CollectionProtocolDetail cp = new CollectionProtocolDetail();
		cp.setTitle("title");
		cp.setShortTitle("short-title");
		cp.setStartDate(CommonUtils.getDate(31,1,2000));
		cp.setPrincipalInvestigator(CommonUtils.getUser(1L, "firstName" , "lastName" , "login@login.com"));
		cp.setConsentsWaived(true);
		cp.setIrbId("ASDF-0000");
		cp.setPpidFmt("ppid-format");
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
}
