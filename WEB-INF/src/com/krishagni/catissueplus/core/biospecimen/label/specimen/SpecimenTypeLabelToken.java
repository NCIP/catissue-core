package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.AbbreviationMap;
import com.krishagni.catissueplus.core.common.errors.AbbreviationErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;

public class SpecimenTypeLabelToken extends AbstractSpecimenLabelToken implements InitializingBean, ConfigChangeListener {

    @Autowired
    private ConfigurationService cfgSvc;

    private Map<String, String> abbreviationMap = AbbreviationMap.abbreviationMap;

    public void setCfgSvc(ConfigurationService cfgSvc) {
        this.cfgSvc = cfgSvc;
    }

    public SpecimenTypeLabelToken() {
        this.name = "SP_TYPE";
    }

    @Override
    public String getLabel(Specimen specimen) {
        if (!abbreviationMap.containsKey(specimen.getSpecimenType())) {
            throw OpenSpecimenException.userError(
                AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
                specimen.getSpecimenType(), SPECIMEN_TYPE.replace("_", " "));
        } else {
            return abbreviationMap.get(specimen.getSpecimenType());
        }
    }

    @Override
    public void onConfigChange(String name, String value) {
        if (!name.equals(ConfigParams.ABBREVIATION_MAP)) {
            return;
        }

        String customMappingFile = cfgSvc.getStrSetting(
            MODULE,
            ConfigParams.ABBREVIATION_MAP,
            "");
        AbbreviationMap.loadAbbreviationMap(customMappingFile, SPECIMEN_TYPE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String customMappingFile = cfgSvc.getStrSetting(
            MODULE,
            ConfigParams.ABBREVIATION_MAP,
            "");

        AbbreviationMap.loadAbbreviationMap(customMappingFile, SPECIMEN_TYPE);
        cfgSvc.registerChangeListener(MODULE, this);
    }

    private static final String SPECIMEN_TYPE = "Specimen_Type";

    private static final String MODULE = "common";
}