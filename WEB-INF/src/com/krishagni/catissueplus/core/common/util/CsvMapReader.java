package com.krishagni.catissueplus.core.common.util;

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

public class CsvMapReader {

    private static Map<String, String> abbreviationMap = new HashMap<String, String>();

    public static Map<String, String> getMap(String resourcePath) {
        if(StringUtils.isBlank(resourcePath)) {
            return abbreviationMap;
        }

        InputStream resourceFile = null;

        try {
            if (resourcePath.startsWith("classpath:")) {
                resourceFile = Utility.getResourceInputStream(resourcePath.substring(10));
            } else {
                resourceFile = new FileInputStream(resourcePath);
            }

            if (resourceFile == null) {
                throw OpenSpecimenException.serverError(new RuntimeException(DEF_FILE_NOT_FOUND_ERROR));
            }

            abbreviationMap = getAbbrMap(resourceFile);
        } catch (Exception e) {
            LOGGER.error("Error loading abbreviation map", e);
            throw OpenSpecimenException.serverError(e);
        } finally {
            IOUtils.closeQuietly(resourceFile);
        }

        return abbreviationMap;
    }
 
    private static Map<String, String> getAbbrMap(InputStream in) {
        BufferedReader reader = null;

        Map<String, String> abbreviationMap = new HashMap<String, String>();

        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }

                String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                if (fields.length != 2) {
                    LOGGER.error("Invalid input line: " + line + ". Ignoring");
                    continue;
                }

                String fieldValue = fields[0].trim().replaceAll("\"", "");
                String abbr = fields[1].trim().replaceAll("\"", "");

                if (StringUtils.isEmpty(fieldValue)) {
                    LOGGER.error("Invalid input line: " + line + ". is empty. Ignoring");
                    continue;
                }       

                if (StringUtils.isEmpty(abbr)) {
                    LOGGER.error("Invalid input line: " + line + ". Abbreviation is empty. Ignoring");
                    continue;
                }

                abbreviationMap.put(fieldValue, abbr);
            }
        } catch (Exception e) {
            LOGGER.error("Error loading abbreviation map", e);
            throw OpenSpecimenException.serverError(e);
        } finally {
            IOUtils.closeQuietly(reader);
        }

        return abbreviationMap;
    }

    private static final Logger LOGGER = Logger.getLogger(CsvMapReader.class);

    private static final String DEF_FILE_NOT_FOUND_ERROR = "Couldn't open default abbreviation mapping file";

}
