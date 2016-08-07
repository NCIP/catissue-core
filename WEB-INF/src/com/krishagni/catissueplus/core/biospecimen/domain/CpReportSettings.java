package com.krishagni.catissueplus.core.biospecimen.domain;

import com.krishagni.catissueplus.core.common.domain.ReportSettings;

public class CpReportSettings extends ReportSettings {
	private CollectionProtocol cp;

	public CollectionProtocol getCp() {
		return cp;
	}

	public void setCp(CollectionProtocol cp) {
		this.cp = cp;
	}
}
