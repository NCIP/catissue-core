package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.util.HashMap; 
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.label.AbbreviationErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.CsvMapReader;

public class SpecimenTypeLabelToken extends AbstractSpecimenLabelToken implements InitializingBean, ConfigChangeListener {

	private String defaultAbbrFile;

	private ConfigurationService cfgSvc;

	private Map<String, String> spmntypeAbbrMap = new HashMap<String, String>();

	public SpecimenTypeLabelToken() {
		this.name = "SP_TYPE";
	}

	public void setDefaultAbbrFile(String defaultAbbrFile) {
		this.defaultAbbrFile = defaultAbbrFile;
	}

	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	@Override
	public void onConfigChange(String name, String value) {
		if (!name.equals(ConfigParams.SP_TYPE_ABBREVIATION_MAP)) {
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
		String label = spmntypeAbbrMap.get(specimen.getSpecimenType());

		if(label == null) {
			throw OpenSpecimenException.userError(AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
					specimen.getSpecimenType(),
					SPECIMEN_TYPE);
		}

		return label;
	}

	private void setAbbreviationMap() {
		spmntypeAbbrMap = CsvMapReader.getMap(defaultAbbrFile);

		String customMappingFile = cfgSvc.getStrSetting(
				ConfigParams.MODULE,
				ConfigParams.SP_TYPE_ABBREVIATION_MAP,
				"");

		spmntypeAbbrMap.putAll(CsvMapReader.getMap(customMappingFile));
	}

	private static final String SPECIMEN_TYPE = "Specimen Type";

}
