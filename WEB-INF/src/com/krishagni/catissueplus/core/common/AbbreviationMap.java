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

public class AbbreviationMap implements InitializingBean, ConfigChangeListener {

    private ConfigurationService cfgSvc;

    private Map<String, Map<String, String>> allAbbrValuesMap = new HashMap<String, Map<String, String>>();

    public void setCfgSvc(ConfigurationService cfgSvc) {
        this.cfgSvc = cfgSvc;
    }

    public Map<String, Map<String, String>> getAbbreviationMap() {
        return allAbbrValuesMap;
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

    public void loadAbbreviationMap(String customMappingFile) {
        InputStream defIn = null;
        InputStream custIn = null;

        try {
            defIn = Utility.getResourceInputStream(defAbbreviationMapFile);
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
            logger.error("Error loading abbreviation map", e);
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
                    logger.error("Invalid input line: " + line + ". Ignoring");
                    continue;
                }

                Map<String, String> abbreviationMap = allAbbrValuesMap.get(fields[0]);
                if (abbreviationMap == null) {
                    abbreviationMap = new HashMap<String, String>();
                    allAbbrValuesMap.put(fields[0].trim(), abbreviationMap);
                }

                String fieldValue = fields[1].trim().replaceAll("\"", "");
                String abbr = fields[2].trim().replaceAll("\"", "");

                if (StringUtils.isEmpty(fieldValue)) {
                    logger.error("Invalid input line: " + line + ". is empty. Ignoring");
                    continue;
                }

                if (StringUtils.isEmpty(abbr)) {
                    logger.error("Invalid input line: " + line + ". Abbreviation is empty. Ignoring");
                    continue;
                }

                abbreviationMap.put(fieldValue, abbr);
            }
        } catch (Exception e) {
            logger.error("Error loading abbreviation map", e);
            throw OpenSpecimenException.serverError(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private static final Logger logger = Logger.getLogger(AbbreviationMap.class);
  
    private static final String DEF_FILE_NOT_FOUND_ERROR = "Couldn't open default abbreviation mapping file";
 
    private static final String defAbbreviationMapFile = "/com/krishagni/catissueplus/core/common/abbreviation-values.csv";

}
