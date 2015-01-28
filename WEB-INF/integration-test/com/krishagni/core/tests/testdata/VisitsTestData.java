package com.krishagni.core.tests.testdata;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AddVisitEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;

import edu.wustl.common.beans.SessionDataBean;

public class VisitsTestData {

	public static VisitDetail getVisitDetail() {
		VisitDetail visit = new VisitDetail();
		visit.setCprId(1L);
		visit.setClinicalDiagnosis("test-daiagnosis");
		visit.setClinicalStatus("test-status");
		visit.setEventId(1L);
		visit.setSurgicalPathologyNumber("test-pathology");
		visit.setVisitDate(CprTestData.getDate(21, 1, 2012));
		visit.setSite("SITE1");
		visit.setStatus("Completed");
		return visit;
	}
	
	
	public static ReqVisitsEvent getReqVisitsEvent() {
		ReqVisitsEvent req = new ReqVisitsEvent();
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		req.setCprId(1L);
		req.setIncludeStats(true);
		return req;
	}
	
	public static AddVisitEvent getAddVisitEvent() {
		AddVisitEvent req = new AddVisitEvent();
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		req.setCprId(1L);
		req.setVisit(getVisitDetail());
		return req;
	}
}