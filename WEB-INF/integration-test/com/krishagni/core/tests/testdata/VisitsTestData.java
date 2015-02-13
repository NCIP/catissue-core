package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;

public class VisitsTestData {

	public static VisitDetail getVisitDetail() {
		VisitDetail visit = new VisitDetail();
		visit.setCprId(1L);
		visit.setClinicalDiagnosis("test-daiagnosis");
		visit.setClinicalStatus("test-status");
		visit.setEventId(1L);
		visit.setSurgicalPathologyNumber("test-pathology");
		visit.setVisitDate(CommonUtils.getDate(21, 1, 2012));
		visit.setSite("SITE1");
		visit.setStatus("Completed");
		return visit;
	}
}