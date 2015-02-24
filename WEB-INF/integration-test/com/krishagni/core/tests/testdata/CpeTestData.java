package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;

public class CpeTestData {
	public static CollectionProtocolEventDetail getcpEventDetail() {
		CollectionProtocolEventDetail detail = new CollectionProtocolEventDetail();
		detail.setEventLabel("default-event-label");
		detail.setEventPoint(1.0);
		detail.setCollectionProtocol("default-cp");
		detail.setDefaultSite("default-site");
		detail.setClinicalDiagnosis("default-clinical-diagnosis");
		detail.setClinicalStatus("default-clinical-status");
		detail.setActivityStatus("Active");
		return detail;
	}
	
	public static CollectionProtocolEventDetail getCpeUpdateDetail() {
		CollectionProtocolEventDetail detail = new CollectionProtocolEventDetail();
		detail.setId(1L);
		detail.setEventLabel("updated-event-label");
		detail.setEventPoint(2.0);
		detail.setCollectionProtocol("default-cp");
		detail.setDefaultSite("updated-site");
		detail.setClinicalDiagnosis("updated-clinical-diagnosis");
		detail.setClinicalStatus("updated-clinical-status");
		detail.setActivityStatus("Active");
		return detail;
	}
	
	public static CollectionProtocolEventDetail getCpeCopyDetail() {
		CollectionProtocolEventDetail detail = new CollectionProtocolEventDetail();
		detail.setEventLabel("copy-event-label");
		detail.setEventPoint(1.0);
		detail.setCollectionProtocol("default-cp");
		detail.setDefaultSite("default-site");
		detail.setClinicalDiagnosis("default-clinical-diagnosis");
		detail.setClinicalStatus("default-clinical-status");
		detail.setActivityStatus("Active");
		return detail;
	}

	public static CollectionProtocolEventDetail getCpeCopyDetailWithNullAndEmptyFields() {
		CollectionProtocolEventDetail detail = new CollectionProtocolEventDetail();
		detail.setEventLabel("copy-event-label");
		detail.setEventPoint(null);
		detail.setCollectionProtocol("");
		detail.setDefaultSite("");
		detail.setClinicalDiagnosis("");
		detail.setClinicalStatus("");
		detail.setActivityStatus("");
		return detail;
	}
}