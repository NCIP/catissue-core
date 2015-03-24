package com.krishagni.catissueplus.core.auditquery.domain;

import java.util.Date;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.krishagni.catissueplus.core.auditquery.events.EnversListener;

@RevisionEntity(EnversListener.class)
public class EnversRevisionEntity {
	
	@RevisionNumber
	private long id;
	  
	@RevisionTimestamp
	private Date revtstmp;
	
	private Long userId;
	
	private String ipAddress;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Date getRevtstmp() {
		return revtstmp;
	}

	public void setRevtstmp(Date revtstmp) {
		this.revtstmp = revtstmp;
	}

	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}