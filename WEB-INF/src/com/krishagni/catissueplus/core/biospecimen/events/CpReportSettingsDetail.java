package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.biospecimen.domain.CpReportSettings;
import com.krishagni.catissueplus.core.common.events.ReportSettingsDetail;

public class CpReportSettingsDetail extends ReportSettingsDetail {
	private CollectionProtocolSummary cp;

	public CollectionProtocolSummary getCp() {
		return cp;
	}

	public void setCp(CollectionProtocolSummary cp) {
		this.cp = cp;
	}

	public static CpReportSettingsDetail from(CpReportSettings rptSettings) {
		CpReportSettingsDetail detail = new CpReportSettingsDetail();
		detail.setCp(CollectionProtocolSummary.from(rptSettings.getCp()));
		fromTo(rptSettings, detail);
		return detail;
	}
}
