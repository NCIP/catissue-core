package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.ConfigSetting;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.ConfigSettingDao;

public class ConfigSettingDaoImpl extends AbstractDao<ConfigSetting> implements ConfigSettingDao {

	@Override
	public Class<ConfigSetting> getType() {
		return ConfigSetting.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSetting> getAllSettings() {
		return getCurrentSession().getNamedQuery(GET_ALL).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSetting> getAllSettingsByModule(String moduleName) {
		return getCurrentSession().getNamedQuery(GET_ALL_BY_MODULE)
			.setString("name", moduleName)
			.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConfigSetting> getSettingsLaterThan(Long settingId) {
		return getCurrentSession().getNamedQuery(GET_ALL_LATER_THAN)
			.setParameter("settingId", settingId)
			.list();
	}

	private static final String FQN = ConfigSetting.class.getName();
	
	private static final String GET_ALL = FQN + ".getAll";
	
	private static final String GET_ALL_BY_MODULE = FQN + ".getAllByModule";

	private static final String GET_ALL_LATER_THAN = FQN + ".getAllLaterThan";
}
