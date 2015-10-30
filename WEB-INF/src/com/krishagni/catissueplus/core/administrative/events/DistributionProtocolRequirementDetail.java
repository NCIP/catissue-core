package com.krishagni.catissueplus.core.administrative.events;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocolRequirement;

public class DistributionProtocolRequirementDetail {
	private Long id;
	
	private DistributionProtocolSummary dp;
	
	private String specimenType;
	
	private String anatomicSite;
	
	private String pathologyStatus;
	
	private Long specimenCount;
	
	private BigDecimal quantity;
	
	private BigDecimal distributedQty;
	
	private String comments;
	
	private String activityStatus;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public DistributionProtocolSummary getDp() {
		return dp;
	}
	
	public void setDp(DistributionProtocolSummary dp) {
		this.dp = dp;
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

	public Long getSpecimenCount() {
		return specimenCount;
	}

	public void setSpecimenCount(Long specimenCount) {
		this.specimenCount = specimenCount;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public BigDecimal getDistributedQty() {
		return distributedQty;
	}
	
	public void setDistributedQty(BigDecimal distributedQty) {
		this.distributedQty = distributedQty;
	}
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String getActivityStatus() {
		return activityStatus;
	}
	
	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	public static DistributionProtocolRequirementDetail from(DistributionProtocolRequirement dpr) {
		DistributionProtocolRequirementDetail detail = new DistributionProtocolRequirementDetail();
		
		detail.setId(dpr.getId());
		detail.setDp(DistributionProtocolSummary.from(dpr.getDistributionProtocol()));
		detail.setSpecimenType(dpr.getSpecimenType());
		detail.setAnatomicSite(dpr.getAnatomicSite());
		detail.setPathologyStatus(dpr.getPathologyStatus());
		detail.setSpecimenCount(dpr.getSpecimenCount());
		detail.setQuantity(dpr.getQuantity());
		detail.setComments(dpr.getComments());
		detail.setActivityStatus(dpr.getActivityStatus());
		
		return detail;
	}
	
	public static List<DistributionProtocolRequirementDetail> from(Collection<DistributionProtocolRequirement> dprs) {
		List<DistributionProtocolRequirementDetail> details = new ArrayList<DistributionProtocolRequirementDetail>();
		
		for (DistributionProtocolRequirement dpr : dprs) {
			details.add(from(dpr));
		}
		
		return details;
	}
}
