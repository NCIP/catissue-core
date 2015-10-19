package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class DistributionProtocolRequirementListCriteria
		extends AbstractListCriteria<DistributionProtocolRequirementListCriteria> {
	
	private Long dpId;
	
	private String specimenType;
	
	private String anatomicSite;
	
	private String pathologyStatus;
	
	@Override
	public DistributionProtocolRequirementListCriteria self() {
		return this;
	}
	
	public Long dpId() {
		return dpId;
	}
	
	public DistributionProtocolRequirementListCriteria dpId(Long dpId) {
		this.dpId = dpId;
		return self();
	}

	public String specimenType() {
		return specimenType;
	}
	
	public DistributionProtocolRequirementListCriteria specimenType(String specimenType) {
		this.specimenType = specimenType;
		return self();
	}
	
	public String anatomicSite() {
		return anatomicSite;
	}
	
	public DistributionProtocolRequirementListCriteria anatomicSite(String anatomicSite) {
		this.anatomicSite = anatomicSite;
		return self();
	}
	
	public String pathologyStatus() {
		return pathologyStatus;
	}
	
	public DistributionProtocolRequirementListCriteria pathologyStatus(String pathologyStatus) {
		this.pathologyStatus = pathologyStatus;
		return self();
	}
}
