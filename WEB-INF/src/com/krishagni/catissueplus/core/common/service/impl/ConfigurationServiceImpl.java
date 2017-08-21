package com.krishagni.catissueplus.core.common.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.event.ContextRefreshedEvent;

import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PluginManager;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
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

public class ConfigurationServiceImpl implements ConfigurationService, InitializingBean, ApplicationListener<ContextRefreshedEvent> {
	
	private Map<String, List<ConfigChangeListener>> changeListeners = new ConcurrentHashMap<>();
	
	private Map<String, Map<String, ConfigSetting>> configSettings;

	private Long maxSettingId;
	
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
	public ResponseEvent<ConfigSettingDetail> getSetting(RequestEvent<Pair<String, String>> req) {
		Pair<String, String> payload = req.getPayload();
		try {
			Map<String, ConfigSetting> moduleSettings = configSettings.get(payload.first());
			if (moduleSettings == null) {
				return ResponseEvent.userError(ConfigErrorCode.MODULE_NOT_FOUND);
			}

			ConfigSetting setting = moduleSettings.get(payload.second());
			if (setting == null) {
				return ResponseEvent.userError(ConfigErrorCode.SETTING_NOT_FOUND);
			}

			return ResponseEvent.response(ConfigSettingDetail.from(setting));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ConfigSettingDetail> saveSetting(RequestEvent<ConfigSettingDetail> req) {
		AccessCtrlMgr.getInstance().ensureUserIsAdmin();
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
			if (successful) {
				deleteOldSettingFile(existing);
			} else {
				existing.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
				moduleSettings.put(prop, existing);
			}
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<File> getSettingFile(RequestEvent<Pair<String, String>> req) {
		Pair<String, String> payload = req.getPayload();
		try {
			Map<String, ConfigSetting> moduleSettings = configSettings.get(payload.first());
			if (moduleSettings == null) {
				return ResponseEvent.userError(ConfigErrorCode.MODULE_NOT_FOUND);
			}

			ConfigSetting setting = moduleSettings.get(payload.second());
			if (setting == null) {
				return ResponseEvent.userError(ConfigErrorCode.SETTING_NOT_FOUND);
			}

			return ResponseEvent.response(getSettingFile(payload.first(), payload.second()));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	public ResponseEvent<String> uploadSettingFile(RequestEvent<FileDetail> req) {
		OutputStream out = null;

		try {
			FileDetail detail = req.getPayload();
			String filename = UUID.randomUUID() + "_" + detail.getFilename();

			out = new FileOutputStream(new File(getSettingFilesDir() + filename));
			IOUtils.copy(detail.getFileIn(), out);

			return ResponseEvent.response(filename);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		} finally {
			IOUtils.closeQuietly(out);
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

				if (setting.getProperty().isSecured() && value != null) {
					value = Utility.decrypt(value);
				}
			}
		}

		if (StringUtils.isBlank(value)) {
			value = defValue != null && defValue.length > 0 ? defValue[0] : null;
		} 
		
		return value;
	}
	
	@Override
	@PlusTransactional	
	public Character getCharSetting(String module, String name, Character... defValue) {
		String value = getStrSetting(module, name, (String)null);
		if (StringUtils.isBlank(value)) {
			return defValue != null && defValue.length > 0 ? defValue[0] : null;
		}
		
		return value.charAt(0);
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
	public File getSettingFile(String module, String name, File... defValue) {
		String value = getStrSetting(module, name, (String)null);
		if (StringUtils.isBlank(value)) {
			return defValue != null && defValue.length > 0 ? defValue[0] : null;
		}

		File file = new File(getSettingFilesDir() + value);
		if (!file.exists()) {
			value = value.split("_", 2)[1];
			throw OpenSpecimenException.userError(ConfigErrorCode.FILE_MOVED_OR_DELETED, value);
		}

		return file;
	}

	@Override
	public String getFileContent(String module, String name, File... defValue) {
		FileDetail fileDetail = null;
		try {
			fileDetail = getFileDetail(module, name, defValue);
			if (fileDetail != null && fileDetail.getFileIn() != null) {
				return IOUtils.toString(fileDetail.getFileIn());
			}

			return StringUtils.EMPTY;
		} catch (OpenSpecimenException ose) {
			throw ose;
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		} finally {
			if (fileDetail != null) {
				IOUtils.closeQuietly(fileDetail.getFileIn());
			}
		}
	}

	@Override
	public FileDetail getFileDetail(String module, String name, File... defValue) {
		try {
			InputStream in = null;
			String contentType = null;
			String filename = null;

			String value = getStrSetting(module, name, (String)null);
			if (StringUtils.isBlank(value)) {
				if (defValue != null && defValue.length > 0) {
					contentType = Utility.getContentType(defValue[0]);
					filename = defValue[0].getName();
					in = new FileInputStream(defValue[0]);
				}
			} else if (value.startsWith("classpath:")) {
				filename = value.substring(value.lastIndexOf(File.separator) + 1);
				in = this.getClass().getResourceAsStream(value.substring(10));
			} else {
				File file = new File(value); // taking a chance whether setting was a string based before
				if (!file.exists()) {
					file = new File(getSettingFilesDir() + value);
				}

				contentType = Utility.getContentType(file);
				filename = file.getName().substring(file.getName().indexOf("_") + 1);
				in = new FileInputStream(file);
			}

			if (in == null) {
				return null;
			}

			FileDetail fileDetail = new FileDetail();
			fileDetail.setContentType(contentType);
			fileDetail.setFilename(filename);
			fileDetail.setFileIn(in);
			return fileDetail;
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public void reload() {
		Map<String, Map<String, ConfigSetting>> settingsMap = new ConcurrentHashMap<>();
		
		List<ConfigSetting> settings = daoFactory.getConfigSettingDao().getAllSettings();
		addToSettingsMap(settings, settingsMap);
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
			listeners = new ArrayList<>();
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
		props.put("deploy_env",              getStrSetting("common", "deploy_env"));
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
	public Map<String, String> getPasswordSettings() {
		Map<String, ConfigSetting> moduleConfig = configSettings.get("auth");
		if (moduleConfig == null) {
			return Collections.emptyMap();
		}
		
		Map<String, String> result = new HashMap<>();
		
		ConfigSetting pattern = moduleConfig.get("password_pattern");
		if (pattern != null) {
			result.put("pattern", pattern.getValue());
		}
		
		ConfigSetting desc = moduleConfig.get("password_rule");
		if (desc != null) {
			result.put("desc", desc.getValue());
		}
		
		return result;
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

	@Override
	@PlusTransactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		List<ConfigSetting> settings = daoFactory.getConfigSettingDao().getSettingsLaterThan(maxSettingId);
		addToSettingsMap(settings, configSettings);
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
					
				case FILE:
					String path = getSettingFilesDir() + setting;
					if (!new File(path).exists()) {
						throw new FileNotFoundException("File at path " + path + " does not exists");
					}
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
		
		if (value != null && existing.getProperty().isSecured()) {
			value = Utility.encrypt(value);
		}
		
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

	private String getSettingFilesDir() {
		String dir = getDataDir() + File.separator + "config-setting-files";
		new File(dir).mkdirs();
		return dir + File.separator;
	}

	private void deleteOldSettingFile(ConfigSetting oldSetting) {
		if (!oldSetting.getProperty().isFile() || StringUtils.isBlank(oldSetting.getValue())) {
			return;
		}


		File file = new File(getSettingFilesDir() + oldSetting.getValue());
		if (file.exists()) {
			file.delete(); // Very dangerous to do! Should we just rename the file?
		}
	}

	private void addToSettingsMap(List<ConfigSetting> settings, Map<String, Map<String, ConfigSetting>> settingsMap) {
		for (ConfigSetting setting : settings) {
			if (maxSettingId == null || setting.getId() > maxSettingId) {
				maxSettingId = setting.getId();
			}

			ConfigProperty prop = setting.getProperty();
			Hibernate.initialize(prop.getAllowedValues()); // pre-init

			Module module = prop.getModule();

			Map<String, ConfigSetting> moduleSettings = settingsMap.get(module.getName());
			if (moduleSettings == null) {
				moduleSettings = new ConcurrentHashMap<>();
				settingsMap.put(module.getName(), moduleSettings);
			}

			moduleSettings.put(prop.getName(), setting);
		}
	}
}
