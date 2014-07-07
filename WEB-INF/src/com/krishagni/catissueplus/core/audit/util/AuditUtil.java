
package com.krishagni.catissueplus.core.audit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;

public final class AuditUtil {

	private static final String AUDIT_REPORT_PROPERTY_FILE = "/AuditReportProperties.properties";

	private static Map<String, String> auditObjectMap = new TreeMap<String, String>();

	private static List<String> objectList = new ArrayList<String>();

	private static Logger logger = Logger.getCommonLogger(AuditUtil.class);

	public static Map<String, String> getAuditObjectDataMap() throws FileNotFoundException, IOException {
		if (auditObjectMap.isEmpty()) {
			populateObjectsInMap();
		}

		return auditObjectMap;
	}

	public static List<String> getObjectList() throws FileNotFoundException, IOException {
		if (objectList.isEmpty()) {
			if (auditObjectMap.isEmpty()) {
				populateObjectsInMap();
			}
			for (String key : auditObjectMap.keySet()) {
				objectList.add(auditObjectMap.get(key));
			}
		}
		return objectList;
	}

	private static void populateObjectsInMap() throws IOException, FileNotFoundException {

		final String path = CommonServiceLocator.getInstance().getPropDirPath();

		Properties properties = new Properties();
		String actualPath = path + AUDIT_REPORT_PROPERTY_FILE;
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(new File(actualPath));

			properties.load(inputStream);
			inputStream.close();
		}
		catch (FileNotFoundException e) {
			logger.error(e);
			throw new FileNotFoundException();
		}
		catch (IOException e) {
			logger.error(e);
			throw new IOException();
		}

		for (String key : properties.stringPropertyNames()) {
			auditObjectMap.put(key, properties.getProperty(key));
		}
	}
}
