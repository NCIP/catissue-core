
package com.krishagni.catissueplus.core.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CaTissueAppContext {

	private static ApplicationContext context;

	public static ApplicationContext getInstance() {
		if (context == null) {
			context = new ClassPathXmlApplicationContext("../applicationContext.xml");
		}
		return context;

	}

}
