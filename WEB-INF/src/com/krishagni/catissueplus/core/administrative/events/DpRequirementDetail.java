package com.krishagni.catissueplus.core.administrative.events;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.DpRequirement;

public class DpRequirementDetail {
	private Long id;
	
	private DistributionProtocolSummary dp;
	
	private String specimenType;
	
	private String anatomicSite;
	
	private Set<String> pathologyStatuses;

	private String clinicalDiagnosis;
	
	private Long specimenCount;
	
	private BigDecimal quantity;

	private Long distributedCnt;
	
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

	public Set<String> getPathologyStatuses() {
		return pathologyStatuses;
	}

	public void setPathologyStatuses(Set<String> pathologyStatuses) {
		this.pathologyStatuses = pathologyStatuses;
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

	public Long getDistributedCnt() {
		return distributedCnt;
	}

	public void setDistributedCnt(Long distributedCnt) {
		this.distributedCnt = distributedCnt;
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
	
	public static DpRequirementDetail from(DpRequirement dpr) {
		DpRequirementDetail detail = new DpRequirementDetail();
		
		detail.setId(dpr.getId());
		detail.setDp(DistributionProtocolSummary.from(dpr.getDistributionProtocol()));
		detail.setSpecimenType(dpr.getSpecimenType());
		detail.setAnatomicSite(dpr.getAnatomicSite());
		detail.setPathologyStatuses(new HashSet<String>(dpr.getPathologyStatuses()));
		detail.setClinicalDiagnosis(dpr.getClinicalDiagnosis());
		detail.setSpecimenCount(dpr.getSpecimenCount());
		detail.setQuantity(dpr.getQuantity());
		detail.setComments(dpr.getComments());
		detail.setActivityStatus(dpr.getActivityStatus());
		
		return detail;
	}
	
	public static List<DpRequirementDetail> from(Collection<DpRequirement> dprs) {
		List<DpRequirementDetail> details = new ArrayList<DpRequirementDetail>();
		
		for (DpRequirement dpr : dprs) {
			details.add(from(dpr));
		}
		
		return details;
	}
}
