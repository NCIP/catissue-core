package com.krishagni.catissueplus.core.administrative.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.util.Status;

public class DpRequirement extends BaseEntity {
	private DistributionProtocol distributionProtocol;
	
	private String specimenType;
	
	private String anatomicSite;
	
	private String pathologyStatus;

	private String clinicalDiagnosis;
	
	private Long specimenCount;
	
	private BigDecimal quantity;
	
	private String comments;
	
	private String activityStatus;
	
	public DistributionProtocol getDistributionProtocol() {
		return distributionProtocol;
	}
	
	public void setDistributionProtocol(DistributionProtocol distributionProtocol) {
		this.distributionProtocol = distributionProtocol;
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

	public String getClinicalDiagnosis() {
		return clinicalDiagnosis;
	}

	public void setClinicalDiagnosis(String clinicalDiagnosis) {
		this.clinicalDiagnosis = clinicalDiagnosis;
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
	
	public void update(DpRequirement dpr) {
		setDistributionProtocol(dpr.getDistributionProtocol());
		setSpecimenType(dpr.getSpecimenType());
		setAnatomicSite(dpr.getAnatomicSite());
		setPathologyStatus(dpr.getPathologyStatus());
		setClinicalDiagnosis(dpr.getClinicalDiagnosis());
		setSpecimenCount(dpr.getSpecimenCount());
		setQuantity(dpr.getQuantity());
		setComments(dpr.getComments());
		setActivityStatus(dpr.getActivityStatus());
	}
	
	public boolean equalsSpecimenGroup(DpRequirement dpr) {
		return equalsSpecimenGroup(dpr.getSpecimenType(), dpr.getAnatomicSite(), dpr.getPathologyStatus(), dpr.getClinicalDiagnosis());
	}

	public boolean equalsSpecimenGroup(String specimenType, String anatomicSite, String pathologyStatus, String clinicalDiagnosis) {
		return StringUtils.equals(getSpecimenType(), specimenType) &&
				StringUtils.equals(getAnatomicSite(), anatomicSite) &&
				StringUtils.equals(getPathologyStatus(), pathologyStatus) &&
				StringUtils.equals(getClinicalDiagnosis(), clinicalDiagnosis);
	}

	public void delete() {
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
	
}
