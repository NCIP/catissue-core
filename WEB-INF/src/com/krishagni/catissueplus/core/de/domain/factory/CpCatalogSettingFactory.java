package com.krishagni.catissueplus.core.de.domain.factory;

import com.krishagni.catissueplus.core.de.domain.CpCatalogSetting;
import com.krishagni.catissueplus.core.de.events.CpCatalogSettingDetail;

public interface CpCatalogSettingFactory {
	public CpCatalogSetting createSetting(CpCatalogSettingDetail detail);
}
