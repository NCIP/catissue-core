package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CpReportSettings;
import com.krishagni.catissueplus.core.biospecimen.repository.CpReportSettingsDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class CpReportSettingsDaoImpl extends AbstractDao<CpReportSettings> implements CpReportSettingsDao {

	@Override
	public Class<CpReportSettings> getType() {
		return CpReportSettings.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public CpReportSettings getByCp(Long cpId) {
		List<CpReportSettings> settings = getCurrentSession()
			.getNamedQuery(GET_BY_CP_ID)
			.setLong("cpId", cpId)
			.list();
		return CollectionUtils.isNotEmpty(settings) ? settings.iterator().next() : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public CpReportSettings getByCp(String cpShortTitle) {
		List<CpReportSettings> settings = getCurrentSession()
			.getNamedQuery(GET_BY_CP_SHORT_TITLE)
			.setString("shortTitle", cpShortTitle)
			.list();
		return CollectionUtils.isNotEmpty(settings) ? settings.iterator().next() : null;
	}

	private static final String FQN = CpReportSettings.class.getName();

	private static final String GET_BY_CP_ID = FQN + ".getByCpId";

	private static final String GET_BY_CP_SHORT_TITLE = FQN + ".getByCpShortTitle";
}
