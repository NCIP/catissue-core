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

import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Utility;

public class AbbreviationMap {

    public static Map<String, String> abbreviationMap = new HashMap<String, String>();

    public static void loadAbbreviationMap(String customMappingFile, String field) {
        InputStream defIn = null;
        InputStream custIn = null;

        try {
            defIn = Utility.getResourceInputStream(defAbbreviationMapFile);
            if (defIn == null) {
                throw OpenSpecimenException.serverError(new RuntimeException(DEF_FILE_NOT_FOUND_ERROR));
            }

            abbreviationMap.putAll(getAbbrMap(defIn,field));

            if (StringUtils.isBlank(customMappingFile)) {
                return;
            }

            if (customMappingFile.startsWith("classpath:")) {
                custIn = Utility.getResourceInputStream(customMappingFile.substring(10));
            } else {
                custIn = new FileInputStream(customMappingFile);
            }

            abbreviationMap.putAll(getAbbrMap(custIn,field));
        } catch (Exception e) {
            logger.error("Error loading abbreviation map", e);
            throw OpenSpecimenException.serverError(e);
        } finally {
            IOUtils.closeQuietly(defIn);
            IOUtils.closeQuietly(custIn);
        }
    }

    private static Map<String, String> getAbbrMap(InputStream in, String field) {
        BufferedReader reader = null;
        Map<String, String> result = new HashMap<String, String>();

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

                if(fields[0].equals(field)) {
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

                    result.put(fieldValue, abbr);
                }
            }

            return result;
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
