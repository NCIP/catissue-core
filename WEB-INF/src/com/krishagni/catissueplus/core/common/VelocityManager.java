
package com.krishagni.catissueplus.core.common;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import edu.wustl.common.util.global.CommonServiceLocator;

public class VelocityManager {

	private static VelocityManager velocityManager;

	private static VelocityEngine velocityEngine;

	private static final String VELOCITY_FILEPATH_RESOURCE_LOADER = "org.apache.velocity.runtime.resource.loader.FileResourceLoader";

	private static final String FILE_RESOURCE_LOADER_CLASS = "file.resource.loader.class";

	private static final String RESOURCE_LOADER = "resource.loader";

	private static final String FILE = "file";

	private static final String RESOURCE_LOADER_PATH = "file.resource.loader.path";

	private VelocityManager() throws Exception {
		initializeVelocityEngine();
	}

	public String evaluate(Map<String, Object> gridObjs, String templateFileName) throws Exception {
		Template template = velocityEngine.getTemplate(templateFileName);
		VelocityContext context = new VelocityContext();

		if (gridObjs != null) {
			for (Map.Entry<String, Object> gridObj : gridObjs.entrySet()) {
				context.put(gridObj.getKey(), gridObj.getValue());
			}
		}

		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}

	public static VelocityManager getInstance() throws Exception {
		if (velocityManager == null) {
			velocityManager = new VelocityManager();
		}
		return velocityManager;
	}

	private void initializeVelocityEngine() throws Exception {
		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RESOURCE_LOADER, FILE);
		velocityEngine.setProperty(FILE_RESOURCE_LOADER_CLASS, VELOCITY_FILEPATH_RESOURCE_LOADER);
		velocityEngine.setProperty(RESOURCE_LOADER_PATH, getTemplatePath());
		velocityEngine.init();
	}

	private static String getTemplatePath() {
		return CommonServiceLocator.getInstance().getPropDirPath(); //TODO need remove and co-ordinate with new code
	}

}
