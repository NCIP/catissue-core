package com.krishagni.core.tests.testdata;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

import edu.wustl.common.beans.SessionDataBean;

public class CommonUtils {
	public static <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(getSession(), payload);
	}
	
	public static <T> RequestEvent<T> getUnauthRequest(T payload) {
		return new RequestEvent<T>(getUnauthSession(), payload);
	}
	
	public static SessionDataBean getUnauthSession() {
		SessionDataBean sessionDataBean = getSession();
		sessionDataBean.setUserId(2L);
		sessionDataBean.setAdmin(false);
		return sessionDataBean;
	}
	
	public static SessionDataBean getSession() {
		SessionDataBean sessionDataBean = new SessionDataBean();
		sessionDataBean.setAdmin(true);
		sessionDataBean.setCsmUserId("1");
		sessionDataBean.setFirstName("admin");
		sessionDataBean.setIpAddress("127.0.0.1");
		sessionDataBean.setLastName("admin");
		sessionDataBean.setUserId(1L);
		sessionDataBean.setUserName("admin@admin.com");
		return sessionDataBean;
	}
	
	public static Date getDate(int day, int month, int year) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static UserSummary getUser(Long id, String firstName, String lastName, String loginName) {
		UserSummary user = new UserSummary();
		user.setId(id);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setLoginName(loginName);
		return user;
	}
}
