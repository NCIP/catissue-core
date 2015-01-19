package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.events.AddSpecimenRequirementEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateAliquotsRequirementEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateDerivedSpecimenReqEvent;
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
		req.setAnatomicSite("Head");
		req.setLaterality("Right");
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
	
	public static AliquotSpecimensRequirement getAliquotSpecimensRequirement() {
		AliquotSpecimensRequirement req = new AliquotSpecimensRequirement();
		req.setParentSrId(1L);
		req.setNoOfAliquots(4);
		req.setQtyPerAliquot(0.1D);
		req.setStorageType("Manual");
		req.setLabelFmt("aliquot-label-format");
		return req;
	}
	
	public static CreateAliquotsRequirementEvent getCreateAliquotsRequirementEvent() {
		CreateAliquotsRequirementEvent req = new CreateAliquotsRequirementEvent();
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		req.setRequirement(getAliquotSpecimensRequirement());
		return req;
	}
	
	public static DerivedSpecimenRequirement getDerivedSpecimenRequirement() {
		DerivedSpecimenRequirement req = new DerivedSpecimenRequirement();
		req.setParentSrId(1L);
		req.setSpecimenClass("Molecular");
		req.setType("Saliva");
		req.setQuantity(0.1D);
		req.setConcentration(0.1D);
		req.setLabelFmt("derived-label-format");
		req.setName("derived-label");
		req.setStorageType("Manual");
		return req;
	}
	
	public static CreateDerivedSpecimenReqEvent getCreateDerivedSpecimenReqEvent() {
		CreateDerivedSpecimenReqEvent req = new CreateDerivedSpecimenReqEvent();
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		req.setRequirement(getDerivedSpecimenRequirement());
		return req;
	}
}

