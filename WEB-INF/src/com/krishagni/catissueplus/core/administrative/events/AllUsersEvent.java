package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class AllUsersEvent extends ResponseEvent {

	private List<UserSummary> users;
	
	private Long count;

	public List<UserSummary> getUsers() {
		return users;
	}

	public void setUsers(List<UserSummary> users) {
		this.users = users;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public static AllUsersEvent ok(List<UserSummary> users, Long count) {
		AllUsersEvent resp = new AllUsersEvent();
		resp.setStatus(EventStatus.OK);
		resp.setUsers(users);
		resp.setCount(count);
		return resp;
	}

	public static AllUsersEvent badRequest(String msg, Throwable t) {
		AllUsersEvent resp = new AllUsersEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(msg);
		resp.setException(t);
		return resp;
	}
}
