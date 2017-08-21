package com.krishagni.core.common;

import edu.wustl.common.util.XMLPropertyHandler;

public class AppInitilizer {
	
	private AppInitilizer() {
		
	}
	
	/*
	 * This class is substitute for CatissueCoreServletContextListener
	 */
	public static void loadSystemSettings() {
		try {
			final String path = System.getProperty("app.propertiesFile");
			XMLPropertyHandler.init(path);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
