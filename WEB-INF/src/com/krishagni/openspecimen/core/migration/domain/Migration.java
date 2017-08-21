package com.krishagni.openspecimen.core.migration.domain;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class Migration extends BaseEntity  {
	
	public enum Version {
		OS_V11,
		OS_V20,
		OS_V21
	};
	
	public enum Status {
		SUCCESS,
		FAIL
	};
	
	private String name;
	
	private Version versionFrom;
	
	private Version versionTo;
	
	private Status status;
	
	private Date date;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Version getVersionFrom() {
		return versionFrom;
	}

	public void setVersionFrom(Version versionFrom) {
		this.versionFrom = versionFrom;
	}

	public Version getVersionTo() {
		return versionTo;
	}

	public void setVersionTo(Version versionTo) {
		this.versionTo = versionTo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
