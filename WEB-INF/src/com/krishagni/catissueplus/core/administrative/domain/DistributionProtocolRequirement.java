package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.util.Status;

public class DistributionProtocolRequirement extends BaseEntity{
	private DistributionProtocol dp;
	
	private String specimenType;
	
	private String anatomicSite;
	
	private String pathologyStatus;
	
	private Long specimenRequired;
	
	private Double price;
	
	private String comments;
	
	private String activityStatus;
	
	public DistributionProtocol getDp() {
		return dp;
	}
	
	public void setDp(DistributionProtocol dp) {
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
	
	public void update(DistributionProtocolRequirement dpr) {
		setDp(dpr.getDp());
		setSpecimenType(dpr.getSpecimenType());
		setAnatomicSite(dpr.getAnatomicSite());
		setPathologyStatus(dpr.getPathologyStatus());
		setSpecimenRequired(dpr.getSpecimenRequired());
		setPrice(dpr.getPrice());
		setComments(dpr.getComments());
		setActivityStatus(dpr.getActivityStatus());
	}
	
	public boolean equalsSpecimenGroup(DistributionProtocolRequirement dpr) {
		if (getSpecimenType().equals(dpr.getSpecimenType()) &&
				getAnatomicSite().equals(dpr.getAnatomicSite()) &&
				getPathologyStatus().equals(dpr.getPathologyStatus())) {
			
			return true;
		}
		
		return false;
	}
	
	public void delete() {
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
	
}
