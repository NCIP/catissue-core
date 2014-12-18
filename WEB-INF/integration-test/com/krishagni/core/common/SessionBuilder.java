package com.krishagni.core.common;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionBuilder {
	private static SessionFactory sessionFactory = null;
	
	public static SessionBuilder getInstance() {
		return new SessionBuilder();
	}
	
	public SessionBuilder() {
		
	}
	
	public SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			AppInitilizer.loadSystemSettings();
			sessionFactory = new Configuration().configure("hibernate.test.cfg.xml")
					.buildSessionFactory();
		}
		
		return sessionFactory;
	}
}
