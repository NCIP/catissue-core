package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.label.AbbreviationErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.CsvMapReader;
public class SpecimenPathologyStatusToken extends AbstractSpecimenLabelToken implements InitializingBean, ConfigChangeListener {

	private String defaultAbbrFile;

	private ConfigurationService cfgSvc;

	private Map<String, String> spmnPathStatusAbbrMap = new HashMap<String, String>();

	public SpecimenPathologyStatusToken() {
		this.name = "SP_PATH_STATUS";
	}

	public void setDefaultAbbrFile(String defaultAbbrFile) {
		this.defaultAbbrFile = defaultAbbrFile;
	}

	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	@Override
	public void onConfigChange(String name, String value) {
		if (!name.equals(ConfigParams.SP_PATH_STATUS_ABBREVIATION_MAP)) {
			return;
		}

		setAbbreviationMap();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setAbbreviationMap();
		cfgSvc.registerChangeListener(ConfigParams.MODULE, this);
	}

	@Override
	public String getLabel(Specimen specimen) {
		String label = spmnPathStatusAbbrMap.get(specimen.getPathologicalStatus());
		if(label == null) {
			throw OpenSpecimenException.userError(AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
					specimen.getPathologicalStatus(),
					SPECIMEN_PATH_STATUS);
		}

		return label;
	}

	private void setAbbreviationMap() {
		spmnPathStatusAbbrMap = CsvMapReader.getMap(defaultAbbrFile);

		String customMappingFile = cfgSvc.getStrSetting(
				ConfigParams.MODULE,
				ConfigParams.SP_PATH_STATUS_ABBREVIATION_MAP,
				"");

		spmnPathStatusAbbrMap.putAll(CsvMapReader.getMap(customMappingFile));
	}

	private static final String SPECIMEN_PATH_STATUS = "Specimen Pathology Status";
}
