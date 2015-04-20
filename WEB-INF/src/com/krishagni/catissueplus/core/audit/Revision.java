package com.krishagni.catissueplus.core.audit;

import java.util.Date;
import java.util.Set;

import org.hibernate.envers.ModifiedEntityNames;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@RevisionEntity(EntityRevisionListenerImpl.class)
public class Revision {
	
	@RevisionNumber
	private long id;
	  
	@RevisionTimestamp
	private Date revtstmp;
	
	private Long userId;
	
	private String ipAddress;
	
	@ModifiedEntityNames
	private Set<String> entityNames;

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

	public Set<String> getEntityNames() {
		return entityNames;
	}

	public void setEntityNames(Set<String> entityNames) {
		this.entityNames = entityNames;
	}
}