
package com.krishagni.catissueplus.core.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OpenSpecimenAppCtxProvider implements ApplicationContextAware {

	private static ApplicationContext context;

	public static ApplicationContext getAppCtx() {
		if (context == null) { 
			//
			// might be a standalone application
			//
			initializeAppCtx();			
		}
		
		return context;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T)getAppCtx().getBean(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx)
	throws BeansException {
		context = ctx;		
	}
	
	
	public static synchronized void initializeAppCtx() {
		if (context != null) {
			return;
		}
		
		context = new ClassPathXmlApplicationContext("../applicationContext.xml");
	}
}