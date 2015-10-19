package com.krishagni.catissueplus.core.administrative.events;

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
	
	private Long specimenRequired;
	
	private Double price;
	
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

	public Long getSpecimenRequired() {
		return specimenRequired;
	}

	public void setSpecimenRequired(Long specimenRequired) {
		this.specimenRequired = specimenRequired;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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
		detail.setDp(DistributionProtocolSummary.from(dpr.getDp()));
		detail.setSpecimenType(dpr.getSpecimenType());
		detail.setAnatomicSite(dpr.getAnatomicSite());
		detail.setPathologyStatus(dpr.getPathologyStatus());
		detail.setSpecimenRequired(dpr.getSpecimenRequired());
		detail.setPrice(dpr.getPrice());
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
