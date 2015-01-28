package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.biospecimen.events.ReqCpeListEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AddCpeEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateCpeEvent;
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
	
	public static ReqCpeListEvent getCpeList() {
		ReqCpeListEvent req = new ReqCpeListEvent();
		req.setCpId(1L);
		return req;
	}
	
	public static AddCpeEvent getAddCpeEvent() {
		AddCpeEvent req = new AddCpeEvent();
		req.setCpe(getcpEventDetail());
		return req;
	}
	
	public static UpdateCpeEvent getUpdateCpeEvent() {
		UpdateCpeEvent req = new UpdateCpeEvent();
		req.setCpe(getCpeUpdateDetail());
		return req;
	}
}