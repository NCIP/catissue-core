package com.krishagni.core.tests.testdata;

import java.util.List;
import java.util.ArrayList;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.core.tests.testdata.CommonUtils;

public class SpecimenTestData {
	public static SpecimenDetail getSpecimenInfo() {
		SpecimenDetail detail = new SpecimenDetail();
		detail.setActivityStatus("Active");
		detail.setAnatomicSite("Head");
		detail.setAvailableQty(0.1);
		detail.setEventId(1L);
		detail.setInitialQty(0.5);
		detail.setLabel("default-label");
		detail.setStatus("Collected");
		detail.setLaterality("Right");
		detail.setPathology("Metastatic");
		detail.setLineage("New");
		detail.setReqId(1L);
		detail.setReqLabel("default-req-label");
		detail.setSpecimenClass("Molecular");
		detail.setStorageType("default-storage");
		detail.setType("DNA");
		detail.setVisitId(1L);
		detail.setParentId(1L);
		detail.setCreatedOn(CommonUtils.getDate(21, 1, 2012));
		
		return detail;
	}
	
	public static List<SpecimenDetail> getSpecimenList() {
		List<SpecimenDetail> specimenList = new ArrayList<SpecimenDetail>();
		
		for(int i=0; i<2; i++) {
			SpecimenDetail obj = getSpecimenInfo();
			String label = "spm"+(i+1);
			String barcode = "barcode-"+(i+1);
			obj.setLabel(label);
			obj.setBarcode(barcode);
			obj.setChildren(getChildren(label));
			specimenList.add(obj);
		}
		
		return specimenList;
	}
	
	public static List<SpecimenDetail> getChildren(String parentLabel) {
		List<SpecimenDetail> childrens = new ArrayList<SpecimenDetail>();
		
		for (int i=0; i<2; i++) {
			SpecimenDetail obj = getSpecimenInfo();
			obj.setLabel(parentLabel + "Child-" + (i+1));
			obj.setBarcode(parentLabel + "-barcode-" + (i+1));
			obj.setParentLabel(parentLabel);
			obj.setLineage(Specimen.DERIVED);
			childrens.add(obj);
		}
		return childrens;
	}
}