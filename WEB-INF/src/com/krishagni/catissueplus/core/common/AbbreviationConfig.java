package com.krishagni.catissueplus.core.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.util.Utility;

public class AbbreviationConfig implements InitializingBean, ConfigChangeListener {

    private ConfigurationService cfgSvc;

    private Map<String, Map<String, String>> abbreviationMap = new HashMap<String, Map<String, String>>();

    public void setCfgSvc(ConfigurationService cfgSvc) {
        this.cfgSvc = cfgSvc;
    }

    public String getAbbreviation(String keyType, String key) {
        Map<String, String> keyTypeMap = abbreviationMap.get(keyType);

        if (keyTypeMap != null) {
            if(keyTypeMap.containsKey(key)) {
                return keyTypeMap.get(key);
            }
            return null;
        }

        return null;
    }

    @Override
    public void onConfigChange(String name, String value) {
        if (!name.equals(ConfigParams.ABBREVIATION_MAP)) {
            return;
        }

        String customMappingFile = cfgSvc.getStrSetting(
            ConfigParams.MODULE,
            ConfigParams.ABBREVIATION_MAP,
            "");
        loadAbbreviationMap(customMappingFile);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String customMappingFile = cfgSvc.getStrSetting(
            ConfigParams.MODULE,
            ConfigParams.ABBREVIATION_MAP,
            "");

        loadAbbreviationMap(customMappingFile);
        cfgSvc.registerChangeListener(ConfigParams.MODULE, this);
    }

    private void loadAbbreviationMap(String customMappingFile) {
        InputStream defIn = null;
        InputStream custIn = null;

        try {
            defIn = Utility.getResourceInputStream(DEF_ABBR_MAP_FILE);
            if (defIn == null) {
                throw OpenSpecimenException.serverError(new RuntimeException(DEF_FILE_NOT_FOUND_ERROR));
            }

            getAbbrMap(defIn);

            if (StringUtils.isBlank(customMappingFile)) {
                return;
            }

            if (customMappingFile.startsWith("classpath:")) {
                custIn = Utility.getResourceInputStream(customMappingFile.substring(10));
            } else {
                custIn = new FileInputStream(customMappingFile);
            }

            getAbbrMap(custIn);
        } catch (Exception e) {
            LOGGER.error("Error loading abbreviation map", e);
            throw OpenSpecimenException.serverError(e);
        } finally {
            IOUtils.closeQuietly(defIn);
            IOUtils.closeQuietly(custIn);
        }
    }

    private void getAbbrMap(InputStream in) {
        BufferedReader reader = null;
 
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }

                String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                if (fields.length != 3) {
                    LOGGER.error("Invalid input line: " + line + ". Ignoring");
                    continue;
                }

                Map<String, String> abbreviations = abbreviationMap.get(fields[0]);
                if (abbreviations == null) {
                    abbreviations = new HashMap<String, String>();
                    abbreviationMap.put(fields[0].trim(), abbreviations);
                }

                String fieldValue = fields[1].trim().replaceAll("\"", "");
                String abbr = fields[2].trim().replaceAll("\"", "");

                if (StringUtils.isEmpty(fieldValue)) {
                    LOGGER.error("Invalid input line: " + line + ". is empty. Ignoring");
                    continue;
                }

                if (StringUtils.isEmpty(abbr)) {
                    LOGGER.error("Invalid input line: " + line + ". Abbreviation is empty. Ignoring");
                    continue;
                }

                abbreviations.put(fieldValue, abbr);
            }
        } catch (Exception e) {
            LOGGER.error("Error loading abbreviation map", e);
            throw OpenSpecimenException.serverError(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private static final Logger LOGGER = Logger.getLogger(AbbreviationConfig.class);
  
    private static final String DEF_FILE_NOT_FOUND_ERROR = "Couldn't open default abbreviation mapping file";
 
    private static final String DEF_ABBR_MAP_FILE = "/com/krishagni/catissueplus/core/common/abbreviation-values.csv";

}
