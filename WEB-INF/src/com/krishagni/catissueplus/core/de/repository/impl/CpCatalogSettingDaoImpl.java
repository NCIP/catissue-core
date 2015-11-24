package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.domain.CpCatalogSetting;
import com.krishagni.catissueplus.core.de.repository.CpCatalogSettingDao;

public class CpCatalogSettingDaoImpl extends AbstractDao<CpCatalogSetting> implements CpCatalogSettingDao {
	
	public Class<CpCatalogSetting> getType() {
		return CpCatalogSetting.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CpCatalogSetting getByCpId(Long cpId) {
		List<CpCatalogSetting> result = getCurrentSession() 
			.getNamedQuery(GET_BY_CP_ID)
			.setLong("cpId", cpId)
			.list();
		return result.isEmpty() ? null : result.iterator().next();
	}

	@SuppressWarnings("unchecked")
	@Override
	public CpCatalogSetting getByCpShortTitle(String shortTitle) {
		List<CpCatalogSetting> result = getCurrentSession()
			.getNamedQuery(GET_BY_CP_SHORT_TITLE)
			.setString("shortTitle", shortTitle)
			.list();
		return result.isEmpty() ? null : result.iterator().next();
	}
	
	private static final String FQN = CpCatalogSetting.class.getName();
	
	private static final String GET_BY_CP_ID = FQN + ".getSettingByCpId";
	
	private static final String GET_BY_CP_SHORT_TITLE = FQN + ".getSettingByCpShortTitle";
}