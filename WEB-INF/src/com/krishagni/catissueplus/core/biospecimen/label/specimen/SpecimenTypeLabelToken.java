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

    private Map<String, String> typeAbbrMap = new HashMap<String, String>();

    public void setDefaultAbbrFile(String defaultAbbrFile) {
        this.defaultAbbrFile = defaultAbbrFile;
    }

    public void setCfgSvc(ConfigurationService cfgSvc) {
        this.cfgSvc = cfgSvc;
    }

    @Override
    public void onConfigChange(String name, String value) {
        typeAbbrMap = CsvMapReader.getMap(defaultAbbrFile);

        if (!name.equals(ConfigParams.SP_TYPE_ABBREVIATION_MAP)) {
            return;
        }

        String customMappingFile = cfgSvc.getStrSetting(
            ConfigParams.MODULE,
            ConfigParams.SP_TYPE_ABBREVIATION_MAP,
            "");
 
        typeAbbrMap.putAll(CsvMapReader.getMap(customMappingFile));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        typeAbbrMap = CsvMapReader.getMap(defaultAbbrFile);

        String customMappingFile = cfgSvc.getStrSetting(
            ConfigParams.MODULE,
            ConfigParams.SP_TYPE_ABBREVIATION_MAP,
            "");

        typeAbbrMap.putAll(CsvMapReader.getMap(customMappingFile));
        cfgSvc.registerChangeListener(ConfigParams.MODULE, this);
    }

    public SpecimenTypeLabelToken() {
        this.name = "SP_TYPE";
    }

    @Override
    public String getLabel(Specimen specimen) {
        if(!typeAbbrMap.isEmpty()) {
            if (typeAbbrMap.containsKey(specimen.getSpecimenType())) {
                 return typeAbbrMap.get(specimen.getSpecimenType());
             } else {
                 throw OpenSpecimenException.userError(AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
                     specimen.getSpecimenType(),
                     SPECIMEN_TYPE.replace("_", " "));
             }
        } else {
            throw OpenSpecimenException.userError(AbbreviationErrorCode.ABBR_VALUE_NOT_FOUND,
                specimen.getSpecimenType(),
                SPECIMEN_TYPE.replace("_", " "));
        }
    }

    private static final String SPECIMEN_TYPE = "Specimen_Type";

}