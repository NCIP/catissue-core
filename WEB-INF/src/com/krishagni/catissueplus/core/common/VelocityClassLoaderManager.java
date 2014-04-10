
package com.krishagni.catissueplus.core.common;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class VelocityClassLoaderManager {

	private static VelocityClassLoaderManager velocityManager;

	private static VelocityEngine velocityEngine;

	private static final String VELOCITY_CLASSPATH_RESOURCE_LOADER = "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader";

	private static final String CLASS_RESOURCE_LOADER_CLASS = "class.resource.loader.class";

	private static final String RESOURCE_LOADER = "resource.loader";

	private static final String CLASS = "class";

	/**
	 * Singleton class
	 * 
	 * @throws Exception
	 */
	private VelocityClassLoaderManager() throws Exception {
		initializeVelocityEngine();
	}

	public String evaluate(Map<String, Object> reportListObject, String templateFileName) throws Exception {

		Template template = velocityEngine.getTemplate(templateFileName);
		VelocityContext context = new VelocityContext();
		if (reportListObject != null) {
			for (Map.Entry<String, Object> gridObj : reportListObject.entrySet()) {
				context.put(gridObj.getKey(), gridObj.getValue());
			}
		}

		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return writer.toString();
	}

	public static VelocityClassLoaderManager getInstance() throws Exception {
		if (velocityManager == null) {
			velocityManager = new VelocityClassLoaderManager();
		}
		return velocityManager;
	}

	private void initializeVelocityEngine() throws Exception {
		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RESOURCE_LOADER, CLASS);
		velocityEngine.setProperty(CLASS_RESOURCE_LOADER_CLASS, VELOCITY_CLASSPATH_RESOURCE_LOADER);
		velocityEngine.init();
	}
}
