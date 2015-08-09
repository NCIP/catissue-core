package com.krishagni.catissueplus.core.biospecimen.label.specimen;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.ConfigParams;
import com.krishagni.catissueplus.core.common.errors.AbbreviationErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.CsvMapReader;
public class SpecimenPathologyStatusToken extends AbstractSpecimenLabelToken implements InitializingBean, ConfigChangeListener {

    private String defaultAbbrFile;

    private ConfigurationService cfgSvc;

    private Map<String, String> spmnPathStatusAbbrMap = new HashMap<String, String>();

    public void setDefaultAbbrFile(String defaultAbbrFile) {
        this.defaultAbbrFile = defaultAbbrFile;
    }

    public void setCfgSvc(ConfigurationService cfgSvc) {
        this.cfgSvc = cfgSvc;
    }

    @Override
    public void onConfigChange(String name, String value) {
        spmnPathStatusAbbrMap = CsvMapReader.getMap(defaultAbbrFile);

        if (!name.equals(ConfigParams.SP_PATH_STATUS_ABBREVIATION_MAP)) {
            return;
        }

        String customMappingFile = cfgSvc.getStrSetting(
            ConfigParams.MODULE,
            ConfigParams.SP_PATH_STATUS_ABBREVIATION_MAP,
            "");

        spmnPathStatusAbbrMap.putAll(CsvMapReader.getMap(customMappingFile));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        spmnPathStatusAbbrMap = CsvMapReader.getMap(defaultAbbrFile);

        String customMappingFile = cfgSvc.getStrSetting(
            ConfigParams.MODULE,
            ConfigParams.SP_PATH_STATUS_ABBREVIATION_MAP,
            "");

        spmnPathStatusAbbrMap.putAll(CsvMapReader.getMap(customMappingFile));
        cfgSvc.registerChangeListener(ConfigParams.MODULE, this);
    }

    public SpecimenPathologyStatusToken() {
        this.name = "SP_PATH_STATUS";
    }

    @Override
    public String getLabel(Specimen specimen) {
        if(!spmnPathStatusAbbrMap.isEmpty()) {
            if (spmnPathStatusAbbrMap.containsKey(specimen.getPathologicalStatus())) {
                return spmnPathStatusAbbrMap.get(specimen.getPathologicalStatus());
            } else {
                throw OpenSpecimenException.userError(AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
                    specimen.getPathologicalStatus(),
                    SPECIMEN_PATH_STATUS.replace("_", " "));
            }
        } else {
            throw OpenSpecimenException.userError(AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
                specimen.getPathologicalStatus(),
                SPECIMEN_PATH_STATUS.replace("_", " "));
        }
    }

    private static final String SPECIMEN_PATH_STATUS = "Specimen_Pathology_Status";
}