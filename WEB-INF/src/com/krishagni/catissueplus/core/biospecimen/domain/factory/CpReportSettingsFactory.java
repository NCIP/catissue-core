package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.CpReportSettings;
import com.krishagni.catissueplus.core.biospecimen.events.CpReportSettingsDetail;

public interface CpReportSettingsFactory {
	public CpReportSettings createSettings(CpReportSettingsDetail detail);
}
