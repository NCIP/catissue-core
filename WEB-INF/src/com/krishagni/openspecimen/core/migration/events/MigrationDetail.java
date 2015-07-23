package com.krishagni.openspecimen.core.migration.events;

import java.util.Date;

import com.krishagni.openspecimen.core.migration.domain.Migration;
import com.krishagni.openspecimen.core.migration.domain.Migration.Status;
import com.krishagni.openspecimen.core.migration.domain.Migration.Version;

public class MigrationDetail {
	private Long id;
	
	private String name;
	
	private Version versionFrom;
	
	private Version versionTo;
	
	private Status status;
	
	private Date date;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public static MigrationDetail from(Migration migration) {
		if (migration == null) {
			return null;
		}
		MigrationDetail detail = new MigrationDetail();
		detail.setId(migration.getId());
		detail.setName(migration.getName());
		detail.setVersionFrom(migration.getVersionFrom());
		detail.setVersionTo(migration.getVersionTo());
		detail.setStatus(migration.getStatus());
		detail.setDate(new Date());
		return detail;
	}

}
