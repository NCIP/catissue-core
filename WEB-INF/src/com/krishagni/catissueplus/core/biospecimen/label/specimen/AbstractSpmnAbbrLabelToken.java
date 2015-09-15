package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.CsvMapReader;

public abstract class AbstractSpmnAbbrLabelToken extends AbstractSpecimenLabelToken implements InitializingBean, ConfigChangeListener {
	
	private String defaultAbbrFile;

	private ConfigurationService cfgSvc;

	private Map<String, String> abbrMap = new HashMap<String, String>();

	public void setDefaultAbbrFile(String defaultAbbrFile) {
		this.defaultAbbrFile = defaultAbbrFile;
	}

	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}
	
	@Override
	public void onConfigChange(String name, String value) {
		if (!name.equals(getAbbrFileConfigParam())) {
			return;
		}

		loadAbbreviationMap();		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		loadAbbreviationMap();
		cfgSvc.registerChangeListener(ConfigParams.MODULE, this);
	}

	protected String getAbbr(String key) {
		return abbrMap.get(key);
	}
	
	protected String getLabel(String key, ErrorCode code) {
		String label = abbrMap.get(key);
		if (label == null) {
			throw OpenSpecimenException.userError(code, key);
		}
		
		return label;
	}
	
	protected abstract String getAbbrFileConfigParam();
	
	private void loadAbbreviationMap() {
		abbrMap = CsvMapReader.getMap(defaultAbbrFile);

		String file = cfgSvc.getStrSetting(
			ConfigParams.MODULE,
			getAbbrFileConfigParam(),
			"");
		abbrMap.putAll(CsvMapReader.getMap(file));
	}
}