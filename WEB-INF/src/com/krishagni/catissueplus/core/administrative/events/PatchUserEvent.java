
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class PatchUserEvent extends RequestEvent {

	private Long userId;

	private UserPatchDetails userDetails;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public UserPatchDetails getUserDetails() {
		return userDetails;
	}
	
	public void setUserDetails(UserPatchDetails userDetails) {
		this.userDetails = userDetails;
	}
}
