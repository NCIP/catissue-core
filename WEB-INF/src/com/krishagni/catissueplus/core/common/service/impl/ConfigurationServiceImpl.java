package com.krishagni.catissueplus.core.common.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PluginManager;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.domain.ConfigErrorCode;
import com.krishagni.catissueplus.core.common.domain.ConfigProperty;
import com.krishagni.catissueplus.core.common.domain.ConfigSetting;
import com.krishagni.catissueplus.core.common.domain.Module;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.ConfigSettingDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

public class ConfigurationServiceImpl implements ConfigurationService, InitializingBean {
	
	private Map<String, List<ConfigChangeListener>> changeListeners = 
			new ConcurrentHashMap<String, List<ConfigChangeListener>>();
	
	private Map<String, Map<String, ConfigSetting>> configSettings;
	
	private DaoFactory daoFactory;
	
	private MessageSource messageSource;
	
	private Properties appProps = new Properties();
		
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public void setAppProps(Properties appProps) {
		this.appProps = appProps;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<ConfigSettingDetail>> getSettings(RequestEvent<String> req) {
		String module = req.getPayload();

		List<ConfigSetting> settings = new ArrayList<ConfigSetting>();
		if (StringUtils.isBlank(module)) {
			for (Map<String, ConfigSetting> moduleSettings : configSettings.values()) {
				settings.addAll(moduleSettings.values());
			}
		} else {
			Map<String, ConfigSetting> moduleSettings = configSettings.get(module);
			if (moduleSettings != null) {
				settings.addAll(moduleSettings.values());
			}
		}
		
		return ResponseEvent.response(ConfigSettingDetail.from(settings));
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ConfigSettingDetail> saveSetting(RequestEvent<ConfigSettingDetail> req) {
		ConfigSettingDetail detail = req.getPayload();
		
		String module = detail.getModule();
		Map<String, ConfigSetting> moduleSettings = configSettings.get(module);
		if (moduleSettings == null || moduleSettings.isEmpty()) {
			return ResponseEvent.userError(ConfigErrorCode.MODULE_NOT_FOUND);
		}
		
		String prop = detail.getName();
		ConfigSetting existing = moduleSettings.get(prop);
		if (existing == null) {
			return ResponseEvent.userError(ConfigErrorCode.SETTING_NOT_FOUND);
		}
		
		String setting = detail.getValue();
		if (!isValidSetting(existing.getProperty(), setting)) {
			return ResponseEvent.userError(ConfigErrorCode.INVALID_SETTING_VALUE);
		}
		
		boolean successful = false;
		try {
			ConfigSetting newSetting = createSetting(existing, setting);
			existing.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());

			daoFactory.getConfigSettingDao().saveOrUpdate(existing);
			daoFactory.getConfigSettingDao().saveOrUpdate(newSetting);		
			moduleSettings.put(prop, newSetting);
			
			notifyListeners(module, prop, setting);
			successful = true;
			return ResponseEvent.response(ConfigSettingDetail.from(newSetting));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		} finally {
			if (!successful) {
				existing.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
				moduleSettings.put(prop, existing);
			}
		}
	}
	
	@Override
	@PlusTransactional
	public Integer getIntSetting(String module, String name, Integer... defValue) {
		String value = getStrSetting(module, name, (String)null);
		if (StringUtils.isBlank(value)) {
			return defValue != null && defValue.length > 0 ? defValue[0] : null;
		}
		
		return Integer.parseInt(value);
	}

	@Override
	@PlusTransactional
	public Double getFloatSetting(String module, String name, Double... defValue) {
		String value = getStrSetting(module, name, (String)null);
		if (StringUtils.isBlank(value)) {
			return defValue != null && defValue.length > 0 ? defValue[0] : null;
		}
		
		return Double.parseDouble(value);
	}

	@Override
	@PlusTransactional
	public String getStrSetting(String module, String name,	String... defValue) {
		Map<String, ConfigSetting> moduleSettings = configSettings.get(module);
		
		String value = null;
		if (moduleSettings != null) {
			ConfigSetting setting = moduleSettings.get(name);
			if (setting != null) {
				value = setting.getValue();
			}
		}
		
		if (StringUtils.isBlank(value)) {
			value = defValue != null && defValue.length > 0 ? defValue[0] : null;
		} 
		
		return value;
	}

	@Override
	@PlusTransactional	
	public Boolean getBoolSetting(String module, String name, Boolean ... defValue) {
		String value = getStrSetting(module, name, (String)null);
		if (StringUtils.isBlank(value)) {
			return defValue != null && defValue.length > 0 ? defValue[0] : null;
		}
		
		return Boolean.parseBoolean(value);		
	}
	
	@Override
	@PlusTransactional
	public void reload() {
		Map<String, Map<String, ConfigSetting>> settingsMap = new ConcurrentHashMap<String, Map<String, ConfigSetting>>();
		
		List<ConfigSetting> settings = daoFactory.getConfigSettingDao().getAllSettings();
		for (ConfigSetting setting : settings) {
			ConfigProperty prop = setting.getProperty();			
			Hibernate.initialize(prop.getAllowedValues()); // pre-init
						
			Module module = prop.getModule();
			
			Map<String, ConfigSetting> moduleSettings = settingsMap.get(module.getName());
			if (moduleSettings == null) {
				moduleSettings = new ConcurrentHashMap<String, ConfigSetting>();
				settingsMap.put(module.getName(), moduleSettings);
			}
			
			moduleSettings.put(prop.getName(), setting);			
		}
		
		this.configSettings = settingsMap;
		
		for (List<ConfigChangeListener> listeners : changeListeners.values()) {
			for (ConfigChangeListener listener : listeners) {
				listener.onConfigChange(null, null);
			}			
		}
	}

	@Override
	public void registerChangeListener(String module, ConfigChangeListener callback) {
		List<ConfigChangeListener> listeners = changeListeners.get(module);
		if (listeners == null) {
			listeners = new ArrayList<ConfigChangeListener>();
			changeListeners.put(module, listeners);
		}
		
		listeners.add(callback);
	}
	
	@Override
	public Map<String, Object> getLocaleSettings() {
		Map<String, Object> result = new HashMap<String, Object>();

		Locale locale = Locale.getDefault();
		result.put("locale", locale.toString());
		result.put("dateFmt", messageSource.getMessage("common_date_fmt", null, locale));
		result.put("timeFmt", messageSource.getMessage("common_time_fmt", null, locale));
		result.put("deFeDateFmt", messageSource.getMessage("common_de_fe_date_fmt", null, locale));
		result.put("deBeDateFmt", messageSource.getMessage("common_de_be_date_fmt", null, locale));
		result.put("utcOffset", Utility.getTimezoneOffset());

		return result;
	}
		
	@Override
	public String getDateFormat() {
		return messageSource.getMessage("common_date_fmt", null, Locale.getDefault());
	}
	
	@Override
	public String getDeDateFormat() {		
		return messageSource.getMessage("common_de_be_date_fmt", null, Locale.getDefault());
	}

	@Override
	public String getTimeFormat() {
		return messageSource.getMessage("common_time_fmt", null, Locale.getDefault());
	}

	@Override
	public String getDeDateTimeFormat() {
		return getDeDateFormat() + " " + getTimeFormat();
	}
	
	@Override
	public Map<String, String> getWelcomeVideoSettings() {
		Map<String, ConfigSetting> moduleConfig = configSettings.get("common");
		if (moduleConfig == null) {
			return Collections.emptyMap();
		}
		
		Map<String, String> result = new HashMap<String, String>();
		
		ConfigSetting source = moduleConfig.get("welcome_video_source");
		if (source != null) {
			result.put("welcome_video_source", source.getValue());
		}
		
		ConfigSetting url = moduleConfig.get("welcome_video_url");
		if (url != null) {
			result.put("welcome_video_url", url.getValue());
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> getAppProps() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("plugins",                 PluginManager.getInstance().getPluginNames());
		props.put("build_version",           appProps.getProperty("buildinfo.version"));
		props.put("build_date",              appProps.getProperty("buildinfo.date"));
		props.put("build_commit_revision",   appProps.getProperty("buildinfo.commit_revision"));
		props.put("cp_coding_enabled",       getBoolSetting("biospecimen", "cp_coding_enabled", false));
		props.put("auto_empi_enabled",       isAutoEmpiEnabled());
		props.put("uid_mandatory",           getBoolSetting("biospecimen", "uid_mandatory", false));
		props.put("feedback_enabled",        getBoolSetting("common", "feedback_enabled", true));
		props.put("mrn_restriction_enabled", getBoolSetting("biospecimen", "mrn_restriction_enabled", false));
		return props;
	}

	@Override
	public String getDataDir() {		
		String dataDir = appProps.getProperty("app.data_dir");
		if (StringUtils.isBlank(dataDir)) {
			dataDir = ".";
		}
		
		return getStrSetting("common", "data_dir", dataDir);
	}
				
	@Override
	public void afterPropertiesSet() throws Exception {
		reload();
		
		setLocale();
		registerChangeListener("common", new ConfigChangeListener() {			
			@Override
			public void onConfigChange(String name, String value) {				
				if (name.equals("locale")) {
					setLocale();
				}
			}
		});
	}
	
	private boolean isValidSetting(ConfigProperty property, String setting) {
		if (StringUtils.isBlank(setting)) {
			return true;
		}
		
		Set<String> allowedValues = property.getAllowedValues();
		if (CollectionUtils.isNotEmpty(allowedValues) && !allowedValues.contains(setting)) {
			return false;
		}
				
		try {
			switch (property.getDataType()) {
				case BOOLEAN:
					Boolean.parseBoolean(setting);
					break;
					
				case FLOAT:
					Double.parseDouble(setting);
					break;
					
				case INT:
					Integer.parseInt(setting);
					break;
					
				default:
					break;
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private ConfigSetting createSetting(ConfigSetting existing, String value) {
		ConfigSetting newSetting = new ConfigSetting();
		newSetting.setProperty(existing.getProperty());
		newSetting.setActivatedBy(AuthUtil.getCurrentUser());
		newSetting.setActivationDate(Calendar.getInstance().getTime());
		newSetting.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		newSetting.setValue(value);

		return newSetting;
	}

	private void notifyListeners(String module, String property, String setting) {
		List<ConfigChangeListener> listeners = changeListeners.get(module);
		if (listeners == null) {
			return;
		}
		
		for (ConfigChangeListener listener : listeners) {
			listener.onConfigChange(property, setting);
		}
	}
	
	private void setLocale() {
		Locale existingLocale = Locale.getDefault();
		String setting = getStrSetting("common", "locale", existingLocale.toString());
		Locale newLocale = LocaleUtils.toLocale(setting);

		if (!existingLocale.equals(newLocale)) {
			Locale.setDefault(newLocale);
		}
	}
	
	private boolean isAutoEmpiEnabled() {
		return StringUtils.isNotBlank(getStrSetting("biospecimen", "mpi_format", "")) || 
				StringUtils.isNotBlank(getStrSetting("biospecimen", "mpi_generator", ""));
	}
}
