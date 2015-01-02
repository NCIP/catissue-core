package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.biospecimen.events.AddSpecimenRequirementEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenRequirementsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;

public class SpecimenRequirementTestData {

	public static AddSpecimenRequirementEvent getAddSpecimenRequirementEvent() {
		AddSpecimenRequirementEvent req = new AddSpecimenRequirementEvent();
		req.setRequirement(getSpecimenRequirementDetail());
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		return req;
	}
	
	public static SpecimenRequirementDetail getSpecimenRequirementDetail() {
		SpecimenRequirementDetail req = new SpecimenRequirementDetail();
		req.setName("default-label");
		req.setLineage("New");
		req.setSpecimenClass("Molecular");
		req.setType("Plasma");
		req.setAnatomicSite("Right");
		req.setLaterality("Head");
		req.setPathologyStatus("Malignant");
		req.setStorageType("default-storage");
		req.setInitialQty(1.1);
		req.setConcentration(0.5);
		req.setCollector(CpTestData.getUser(2L, "", "", ""));
		req.setCollectionProcedure("default-procedure");
		req.setCollectionContainer("default-container");
		req.setReceiver(CpTestData.getUser(3L, "", "", ""));
		req.setLabelFmt("default-label-format");
		req.setEventId(1L);
		return req;
	}
	
	public static ReqSpecimenRequirementsEvent getReqSpecimenRequirementsEvent() {
		ReqSpecimenRequirementsEvent req = new ReqSpecimenRequirementsEvent();
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		req.setCpeId(1L);
		return req;
	}
}

