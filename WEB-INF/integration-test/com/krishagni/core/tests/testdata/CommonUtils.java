package com.krishagni.core.tests.testdata;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

import edu.wustl.common.beans.SessionDataBean;

public class CommonUtils {
	public static <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(getSession(), payload);
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
}
