package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AllUsersEvent extends ResponseEvent {
	
	private List<User> userList;

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public static AllUsersEvent ok(List<User> userList) {
		AllUsersEvent resp = new AllUsersEvent();
		resp.setUserList(userList);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static AllUsersEvent serverError(Throwable ... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		AllUsersEvent resp = new AllUsersEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;		
	}

}
