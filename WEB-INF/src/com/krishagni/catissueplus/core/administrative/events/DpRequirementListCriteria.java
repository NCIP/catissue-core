package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class DpRequirementListCriteria
		extends AbstractListCriteria<DpRequirementListCriteria> {
	
	private Long dpId;
	
	private String specimenType;
	
	private String anatomicSite;
	
	private String pathologyStatus;
	
	private boolean includeDistQty;
	
	@Override
	public DpRequirementListCriteria self() {
		return this;
	}
	
	public Long dpId() {
		return dpId;
	}
	
	public DpRequirementListCriteria dpId(Long dpId) {
		this.dpId = dpId;
		return self();
	}

	public String specimenType() {
		return specimenType;
	}
	
	public DpRequirementListCriteria specimenType(String specimenType) {
		this.specimenType = specimenType;
		return self();
	}
	
	public String anatomicSite() {
		return anatomicSite;
	}
	
	public DpRequirementListCriteria anatomicSite(String anatomicSite) {
		this.anatomicSite = anatomicSite;
		return self();
	}
	
	public String pathologyStatus() {
		return pathologyStatus;
	}
	
	public DpRequirementListCriteria pathologyStatus(String pathologyStatus) {
		this.pathologyStatus = pathologyStatus;
		return self();
	}
	
	public boolean includeDistQty() {
		return includeDistQty;
	}
	
	public DpRequirementListCriteria includeDistQty(boolean includeDistQty) {
		this.includeDistQty = includeDistQty;
		return self();
	}
}
