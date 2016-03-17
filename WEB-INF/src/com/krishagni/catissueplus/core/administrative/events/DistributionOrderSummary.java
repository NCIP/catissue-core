package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class DistributionOrderSummary {
	private Long id;
	
	private String name;
	
	private DistributionProtocolDetail distributionProtocol;
	
	private String instituteName;
	
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

	public String getInstituteName() {
		return instituteName;
	}
	
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
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

	public static DistributionOrderSummary from(DistributionOrder order) {
		DistributionOrderSummary detail = new DistributionOrderSummary();
		copy(order, detail);
		return detail;
	}

	public static List<DistributionOrderSummary> from(Collection<DistributionOrder> orders) {
		List<DistributionOrderSummary> list = new ArrayList<DistributionOrderSummary>();

		for (DistributionOrder order: orders) {
			list.add(from(order));
		}

		return list;
	}

	public static void copy(DistributionOrder order, DistributionOrderSummary detail) {
		detail.setId(order.getId());
		detail.setName(order.getName());
		detail.setDistributionProtocol(DistributionProtocolDetail.from(order.getDistributionProtocol()));
		detail.setInstituteName(order.getInstitute().getName());
		if (order.getSite() != null) {
			detail.setSiteId(order.getSite().getId());
			detail.setSiteName(order.getSite().getName());
		}

		detail.setRequester(UserSummary.from(order.getRequester()));
		detail.setCreationDate(order.getCreationDate());
		detail.setExecutionDate(order.getExecutionDate());
		detail.setStatus(order.getStatus().toString());
	}
}
