package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.UserSummary;

public class DistributionOrderSummary {
	private Long id;
	
	private String name;
	
	private DistributionProtocolDetail distributionProtocol;
	
	private Long siteId;
	
	private String siteName;
	
	private UserSummary requester;
	
	private Date creationDate;
	
	private Date executionDate;
	
	private String status;
	
	private Long specimenCnt = 0L;

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

	public DistributionProtocolDetail getDistributionProtocol() {
		return distributionProtocol;
	}

	public void setDistributionProtocol(DistributionProtocolDetail distributionProtocol) {
		this.distributionProtocol = distributionProtocol;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public UserSummary getRequester() {
		return requester;
	}

	public void setRequester(UserSummary requester) {
		this.requester = requester;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getSpecimenCnt() {
		return specimenCnt;
	}

	public void setSpecimenCnt(Long specimenCnt) {
		this.specimenCnt = specimenCnt;
	}
}
