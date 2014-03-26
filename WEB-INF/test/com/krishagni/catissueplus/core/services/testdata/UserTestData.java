package com.krishagni.catissueplus.core.services.testdata;

import static org.mockito.Matchers.anyLong;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.CloseUserEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateUserEvent;
import com.krishagni.catissueplus.core.administrative.events.UserDetails;

import edu.wustl.common.beans.SessionDataBean;

public class UserTestData {
	
	public static final String ACTIVITY_STATUS_CLOSED = "Closed";
	
	public static final String FIRST_NAME = "first name";

	public static final String LAST_NAME = "last name";

	public static final String LOGIN_NAME = "login name";

	public static final String EMAIL_ADDRESS = "email address";
	
	public static final String DEPARTMENT = "department";
	
	public static List<User> getUserList() {
		List<User> users = new ArrayList<User>();
		users.add(new User());
		users.add(new User());
		return users;
	}

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
	
	public static User getUser(Long id) {
		User user = new User();
		user.setId(id);
		user.setFirstName("firstName1");
		user.setLastName("lastName1");
		user.setLoginName("admin@admin.com");
		user.setEmailAddress("sci@sci.com");
		return user;
	}
	
	public static  CloseUserEvent getCloseUserEvent() {
		Long userId = 1l;
		CloseUserEvent event = new CloseUserEvent();
		event.setId(userId);
		event.setSessionDataBean(getSessionDataBean());
		return event;
	}
	
	public static UpdateUserEvent getUpdateUserEvent() {
		UserDetails details = new UserDetails();
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		details.setDeptName("Chemical");
		details.setEmailAddress("sci@sci.com");
		details.setLoginName("admin@admin.com");
		details.setLdapId(1L);
		
		UpdateUserEvent reqEvent = new UpdateUserEvent(details, 1L);
		reqEvent.setSessionDataBean(getSessionDataBean());
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	public static UpdateUserEvent getUpadteUserEventWithLNUpdate(){
		UpdateUserEvent reqEvent = getUpdateUserEvent();
		UserDetails details = reqEvent.getUserDetails();
		details.setLoginName("admin");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	public static  CreateUserEvent getCreateUserEvent() {
		CreateUserEvent reqEvent = new CreateUserEvent(null);
		reqEvent.setSessionDataBean(getSessionDataBean());

		UserDetails details = new UserDetails();
		details.setActivityStatus("Active");
		details.setFirstName("firstName");
		details.setLastName("lastName");
		details.setDeptName("Chemical");
		details.setEmailAddress("sci@sci.com");
		details.setLoginName("admin@admin.com");
		details.setLdapId(1L);
		
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	public static CreateUserEvent getCreateUserEventWithInvalidEmail() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = reqEvent.getUserDetails();
		details.setEmailAddress("admin");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}

	public static CreateUserEvent getCreateUserEventForUserCreation() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = reqEvent.getUserDetails();
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	public static CreateUserEvent getCreateUserEventWithEmptyLoginName() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = reqEvent.getUserDetails();
		details.setLoginName("");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	public static CreateUserEvent getCreateUserEventWithEmptyEmail() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = reqEvent.getUserDetails();
		details.setEmailAddress("");
	
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	public static CreateUserEvent getCreateUserEventWithEmptyFirstName() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = reqEvent.getUserDetails();
		details.setFirstName("");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	public static CreateUserEvent getCreateUserEventWithEmptyLastName() {
		CreateUserEvent reqEvent = getCreateUserEvent();
		UserDetails details = reqEvent.getUserDetails();
		details.setLastName("");
		reqEvent.setUserDetails(details);
		return reqEvent;
	}
	
	public static Department getDeparment(String name) {
		Department department = new Department();
		department.setId(10l);
	
		department.setName(name);
		return department;
	}

}
