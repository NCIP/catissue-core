package com.krishagni.openspecimen.rde.domain;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class Session extends BaseEntity {
	private String uid;

	private User user;
	
	private Map<String, Object> data;
	
	private Date createdOn;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getDataStr()
	throws Exception {
		return new ObjectMapper().writeValueAsString(data);
	}

	public void setDataStr(String dataStr)
	throws Exception {
		this.data = new ObjectMapper().readValue(dataStr, Map.class);
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
}
