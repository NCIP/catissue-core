
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;

public class Password {

	private Long id;

	private String password;

	private Date updateDate;

	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public User getUser() {
		return user;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public void setUser(User user) {
		this.user = user;
	}

}