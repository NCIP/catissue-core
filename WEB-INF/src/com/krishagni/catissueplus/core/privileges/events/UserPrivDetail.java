package com.krishagni.catissueplus.core.privileges.events;


public class UserPrivDetail {

	private Long csmUserId;
	private String loginName;
	private Long id;
	
	public Long getCsmUserId() {
		return csmUserId;
	}
	
	public void setCsmUserId(Long csmUserId) {
		this.csmUserId = csmUserId;
	}
	
	public String getLoginName() {
		return loginName;
	}
	
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
}
