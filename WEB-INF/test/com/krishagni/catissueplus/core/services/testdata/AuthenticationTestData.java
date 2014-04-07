package com.krishagni.catissueplus.core.services.testdata;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.events.AuthenticateUserEvent;
import com.krishagni.catissueplus.core.auth.events.LoginDetails;

import edu.wustl.common.beans.SessionDataBean;

public class AuthenticationTestData {
	
	public static final String ACTIVITY_STATUS_CLOSED = "Closed";
	
	public static final String FIRST_NAME = "first name";

	public static final String LAST_NAME = "last name";

	public static final String LOGIN_NAME = "login name";

	public static final String EMAIL_ADDRESS = "email address";
	
	public static final String DEPARTMENT = "department";
	
	public static SessionDataBean getSessionDataBean() {
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

	public static  AuthenticateUserEvent getAuthenticateUserEvent() {
		AuthenticateUserEvent reqEvent = new AuthenticateUserEvent(null);
		reqEvent.setSessionDataBean(getSessionDataBean());

		LoginDetails details = new LoginDetails();
		details.setLdapId(null);
		details.setLoginId("darpan");
		details.setPassword("Krishagni19");
		reqEvent.setLoginDetails(details);
		return reqEvent;
	}
		
	public static AuthenticateUserEvent getAuthenticateUserEventWithEmptyLoginID() {
		AuthenticateUserEvent reqEvent = getAuthenticateUserEvent();
		LoginDetails details = reqEvent.getLoginDetails();
		details.setLoginId("");
		reqEvent.setLoginDetails(details);
		return reqEvent;
	}
	
	public static AuthenticateUserEvent getAuthenticateUserEventWithEmptyPass() {
		AuthenticateUserEvent reqEvent = getAuthenticateUserEvent();
		LoginDetails details = reqEvent.getLoginDetails();
		details.setPassword("");
		reqEvent.setLoginDetails(details);
		return reqEvent;
	}

	public static List<String> getOldPasswordList() {
		List<String> Passwords = new ArrayList<String>();
		Passwords.add("kris");
		Passwords.add("Krishagni19");
		return Passwords;
	}
	
	public static User getUser(Long id) {
		User user = new User();
		user.setId(id);
		user.setFirstName("firstName1");
		user.setLastName("lastName1");
		user.setLoginName("admin@admin.com");
		user.setActivityStatus("Active");
		user.setEmailAddress("sci@sci.com");
		user.setDepartment(new Department());
		user.setAddress(new Address());
		return user;
	}

	public static AuthenticateUserEvent getAuthenticateUserEventWrongPass() {
		AuthenticateUserEvent reqEvent = getAuthenticateUserEvent();
		LoginDetails details = reqEvent.getLoginDetails();
		details.setPassword("Dpdasd");
		reqEvent.setLoginDetails(details);
		return reqEvent;
	}

	public static User getNonActiveUser(long id) {
		User user = getUser(id);
		user.setActivityStatus("Closed");
		return user;
	}
}
