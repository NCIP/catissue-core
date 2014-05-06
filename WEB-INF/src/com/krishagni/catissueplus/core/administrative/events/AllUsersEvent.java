package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class AllUsersEvent extends ResponseEvent {
	private List<UserSummary> users;

	public List<UserSummary> getUsers() {
		return users;
	}

	public void setUsers(List<UserSummary> users) {
		this.users = users;
	}
	
	public static AllUsersEvent ok(List<UserSummary> users) {
		AllUsersEvent resp = new AllUsersEvent();
		resp.setStatus(EventStatus.OK);
		resp.setUsers(users);
		return resp;
	}
}
