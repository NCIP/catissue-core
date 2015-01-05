package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.biospecimen.events.AddCpeEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateCpeEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;

public class CpeTestData {
	public static CollectionProtocolEventDetail getcpEventDetail() {
		CollectionProtocolEventDetail e = new CollectionProtocolEventDetail();
		e.setEventLabel("default-event-label");
		e.setEventPoint(1.0);
		e.setCollectionProtocol("default-cp");
		e.setDefaultSite("default-site");
		e.setClinicalDiagnosis("default-clinical-diagnosis");
		e.setClinicalStatus("default-clinical-status");
		e.setActivityStatus("Active");
		return e;
	}	
	
	public static AddCpeEvent getAddCpeEvent() {
		AddCpeEvent req = new AddCpeEvent();
		req.setCpe(getcpEventDetail());
		return req;
	}	
}
