package com.krishagni.core.tests.testdata;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class SpecimenListTestData {
	public static SpecimenListDetails getSpecimenListDetail() {
		SpecimenListDetails detail = new SpecimenListDetails();
		detail.setName("list-name");
		detail.setSpecimens(getSpecimens(3));
		detail.setSharedWith(getSharedWith(2));
		return detail;
	}
	
	public static SpecimenListDetails getUpdateSpecimenListDetail() {
		SpecimenListDetails detail = new SpecimenListDetails();
		detail.setId(1L);
		detail.setName("updated-name");
		detail.setSpecimens(getSpecimens(2));
		detail.setSharedWith(null);
		return detail;
	}
	
	public static List<SpecimenDetail> getSpecimens(int n) {
		List<SpecimenDetail> specimenList = new ArrayList<SpecimenDetail>();
		for(int i=0; i<n; i++) {
			SpecimenDetail specimen = new SpecimenDetail();
			specimen.setActivityStatus("Active");
			specimen.setAnatomicSite("Head");
			specimen.setAvailableQty(1.0);
			specimen.setEventId(1L);
			specimen.setInitialQty(0.5);
			specimen.setLabel("spm-"+(i+1));
			specimen.setStatus("Collected");
			specimen.setLaterality("Right");
			specimen.setPathology("Metastatic");
			specimen.setLineage("New");
			specimen.setReqId(1L);
			specimen.setReqLabel("req-"+(i+1));
			specimen.setSpecimenClass("Molecular");
			specimen.setStorageType("default-storage");
			specimen.setType("DNA");
			specimen.setVisitId(1L);
			specimen.setParentId(1L);
			specimen.setCreatedOn(CommonUtils.getDate(21, 1, 2012));
			specimenList.add(specimen);
		}
		return specimenList;
	}
	
	public static List<UserSummary> getSharedWith(int n) {
		List<UserSummary> userList = new ArrayList<UserSummary>();
		for(int i=0; i<n; i++) {
			UserSummary user = new UserSummary();
			user.setId(new Long(i+1));
			user.setFirstName("ADMIN"+(i+1));
			user.setLastName("ADMIN"+(i+1));
			user.setLoginName("admin"+(i+1)+"@admin.com");
			userList.add(user);
		}
		return userList;
	}
}