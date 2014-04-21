
package com.krishagni.catissueplus.core.audit.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class GetUsersInfoEvent extends ResponseEvent{

	List<UserInfo> usersInfo;

	public List<UserInfo> getUsersInfo() {
		return usersInfo;
	}

	public void setUserDetails(List<UserInfo> userDetails) {
		this.usersInfo = userDetails;
	}
}
