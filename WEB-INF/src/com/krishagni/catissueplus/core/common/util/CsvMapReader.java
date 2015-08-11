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

import com.krishagni.catissueplus.core.common.errors.CommonErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class CsvMapReader {

	public static Map<String, String> getMap(String resourcePath) {
		Map<String, String> abbreviationMap = new HashMap<String, String>();

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
				throw OpenSpecimenException.userError(CommonErrorCode.CSV_FILE_NOT_FOUND);
			}

			abbreviationMap = getAbbrMap(resourceFile);
		} catch (Exception e) {
			LOGGER.error("Error loading values from csv file", e);
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
					LOGGER.error("Invalid input line: " + line + ".  is empty. Ignoring");
					continue;
				}

				abbreviationMap.put(fieldValue, abbr);
			}
		} catch (Exception e) {
			LOGGER.error("Error loading csv file", e);
			throw OpenSpecimenException.serverError(e);
		} finally {
			IOUtils.closeQuietly(reader);
		}

		return abbreviationMap;
	}

	private static final Logger LOGGER = Logger.getLogger(CsvMapReader.class);

}
