package com.krishagni.catissueplus.core.init;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.context.ConfigurableWebApplicationContext;

public class AppInitializer implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

	@Override
	public void initialize(ConfigurableWebApplicationContext appCtx) {
		try {
			appCtx.getEnvironment()
				.getPropertySources()
				.addFirst(new ResourcePropertySource("classpath:application.properties"));
			
		} catch (Exception e) {
			throw new RuntimeException("Error adding properties from application.properties", e);
		}
	}
}
