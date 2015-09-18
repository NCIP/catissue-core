package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;

public class DistributionOrderSpecReqDetails {
	private Long id;
	
	private String name;
	
	private DistributionProtocolSummary distributionProtocol;

	private Date executionDate;
	
	private String specimenType;
	
	private String anatomicSite;
	
	private String pathologyStatus;
	
	private Long distributedSpecimenCount;

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

	public DistributionProtocolSummary getDistributionProtocol() {
		return distributionProtocol;
	}

	public void setDistributionProtocol(DistributionProtocolSummary distributionProtocol) {
		this.distributionProtocol = distributionProtocol;
	}

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getAnatomicSite() {
		return anatomicSite;
	}

	public void setAnatomicSite(String anatomicSite) {
		this.anatomicSite = anatomicSite;
	}

	public String getPathologyStatus() {
		return pathologyStatus;
	}

	public void setPathologyStatus(String pathologyStatus) {
		this.pathologyStatus = pathologyStatus;
	}

	public Long getDistributedSpecimenCount() {
		return distributedSpecimenCount;
	}

	public void setDistributedSpecimenCount(Long distributedSpecimenCount) {
		this.distributedSpecimenCount = distributedSpecimenCount;
	}
	
}
