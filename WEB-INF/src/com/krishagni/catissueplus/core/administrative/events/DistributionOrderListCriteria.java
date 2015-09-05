package com.krishagni.catissueplus.core.administrative.events;

import java.util.Date;
import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;


public class DistributionOrderListCriteria extends AbstractListCriteria<DistributionOrderListCriteria> {
	private Set<Long> instituteIds;
	
	private Set<Long> siteIds;
	
	private String dpShortTitle;
	
	private Long dpId;
	
	private String requestor;
	
	private Long requestorId;
	
	private Date executionDate;
	
	private String receivingSite;
	
	private String receivingInstitute;
		
	@Override
	public DistributionOrderListCriteria self() {
		return this;
	}
	
	public Set<Long> instituteIds() {
		return instituteIds;
	}
	
	public DistributionOrderListCriteria instituteIds(Set<Long> instituteIds) {
		this.instituteIds = instituteIds;
		return self();
	}
	
	public Set<Long> siteIds() {
		return siteIds;
	}
	
	public DistributionOrderListCriteria siteIds(Set<Long> siteIds) {
		this.siteIds = siteIds;
		return self();
	}
	
	public String dpShortTitle() {
		return dpShortTitle;
	}
	
	public DistributionOrderListCriteria dpShortTitle(String dpShortTitle) {
		this.dpShortTitle = dpShortTitle;
		return self();
	}

	public Long dpId() {
		return dpId;
	}
	
	public DistributionOrderListCriteria dpId(Long dpId) {
		this.dpId = dpId;
		return self();
	}
	
	public String requestor() {
		return requestor;
	}
	
	public DistributionOrderListCriteria requestor(String requestor) {
		this.requestor = requestor;
		return self();
	}

	public Long requestorId() {
		return requestorId;
	}
	
	public DistributionOrderListCriteria requestorId(Long requestorId) {
		this.requestorId = requestorId;
		return self();
	}
	
	public Date executionDate() {
		return executionDate;
	}
	
	public DistributionOrderListCriteria executionDate(Date executionDate) {
		this.executionDate = executionDate;
		return self();
	}
	
	public String receivingSite() {
		return receivingSite;
	}
	
	public DistributionOrderListCriteria receivingSite(String receivingSite) {
		this.receivingSite = receivingSite;
		return self();
	}
	
	public String receivingInstitute() {
		return receivingInstitute;
	}
	
	public DistributionOrderListCriteria receivingInstitute(String receivingInstitute) {
		this.receivingInstitute = receivingInstitute;
		return self();
	}
}
