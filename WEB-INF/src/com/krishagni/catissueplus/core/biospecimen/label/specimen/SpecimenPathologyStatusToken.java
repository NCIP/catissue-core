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
public class SpecimenPathologyStatusToken extends AbstractSpecimenLabelToken implements InitializingBean, ConfigChangeListener {

    @Autowired
    private ConfigurationService cfgSvc;

    private Map<String, String> abbreviationMap = AbbreviationMap.abbreviationMap;

    public void setCfgSvc(ConfigurationService cfgSvc) {
        this.cfgSvc = cfgSvc;
    }

    public SpecimenPathologyStatusToken() {
        this.name = "SP_PATH_STATUS";
    }

    @Override
    public String getLabel(Specimen specimen) {

        if (!abbreviationMap.containsKey(specimen.getPathologicalStatus())) {
            throw OpenSpecimenException.userError(AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
                specimen.getPathologicalStatus(),
                SPECIMEN_PATH_STATUS.replace("_", " "));
        } else {
            return abbreviationMap.get(specimen.getPathologicalStatus());
        }
    }

    @Override
    public void onConfigChange(String name, String value) {
        if(!name.equals(ConfigParams.ABBREVIATION_MAP)) {
            return;
        }

        String customMappingFile = cfgSvc.getStrSetting(
            MODULE,
            ConfigParams.ABBREVIATION_MAP,
            "");

        AbbreviationMap.loadAbbreviationMap(customMappingFile, SPECIMEN_PATH_STATUS);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String customMappingFile = cfgSvc.getStrSetting(
            MODULE,
            ConfigParams.ABBREVIATION_MAP,
            "");

        AbbreviationMap.loadAbbreviationMap(customMappingFile, SPECIMEN_PATH_STATUS);
        cfgSvc.registerChangeListener(MODULE, this);
    }

    private static final String SPECIMEN_PATH_STATUS = "Specimen_Pathology_Status";

    private static final String MODULE = "common";
}
