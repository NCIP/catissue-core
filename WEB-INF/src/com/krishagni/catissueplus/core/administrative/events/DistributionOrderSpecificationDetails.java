package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;

public class DistributionOrderSpecificationDetails {
	private Long id;
	
	private String name;
	
	private DistributionProtocolSummary distributionProtocol;

	private Date executionDate;
	
	private String specimenType;
	
	private String anatomicSite;
	
	private String pathologicalStatus;
	
	private Long specimenDistributed;

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

	public String getPathologicalStatus() {
		return pathologicalStatus;
	}

	public void setPathologicalStatus(String pathologicalStatus) {
		this.pathologicalStatus = pathologicalStatus;
	}

	public Long getSpecimenDistributed() {
		return specimenDistributed;
	}

	public void setSpecimenDistributed(Long specimenDistributed) {
		this.specimenDistributed = specimenDistributed;
	}
	
}
