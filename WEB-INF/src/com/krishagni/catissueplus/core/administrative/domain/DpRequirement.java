package com.krishagni.catissueplus.core.administrative.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.util.Status;

@Audited
public class DpRequirement extends BaseEntity {
	private DistributionProtocol distributionProtocol;
	
	private String specimenType;
	
	private String anatomicSite;
	
	private Set<String> pathologyStatuses = new HashSet<String>();

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
		CollectionUpdater.update(getPathologyStatuses(), dpr.getPathologyStatuses());
		setClinicalDiagnosis(dpr.getClinicalDiagnosis());
		setSpecimenCount(dpr.getSpecimenCount());
		setQuantity(dpr.getQuantity());
		setComments(dpr.getComments());
		setActivityStatus(dpr.getActivityStatus());
	}
	
	public boolean equalsSpecimenGroup(DpRequirement dpr) {
		return equalsSpecimenGroup(dpr.getSpecimenType(), dpr.getAnatomicSite(), dpr.getPathologyStatuses(), dpr.getClinicalDiagnosis());
	}

	public boolean equalsSpecimenGroup(String specimenType, String anatomicSite, Set<String> pathologyStatuses, String clinicalDiagnosis) {
		return StringUtils.equals(getSpecimenType(), specimenType) &&
				StringUtils.equals(getAnatomicSite(), anatomicSite) &&
				arePathologyStatusesEqual(pathologyStatuses) &&
				StringUtils.equals(getClinicalDiagnosis(), clinicalDiagnosis);
	}

	public void delete() {
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

	private boolean arePathologyStatusesEqual(Set<String> pathologyStatuses) {
		boolean isEmptyOldPaths = CollectionUtils.isEmpty(getPathologyStatuses());
		boolean isEmptyNewPaths = CollectionUtils.isEmpty(pathologyStatuses);

		if (isEmptyOldPaths && isEmptyNewPaths) {
			return true;
		}

		if (isEmptyOldPaths || isEmptyNewPaths) {
			return false;
		}

		return CollectionUtils.isSubCollection(pathologyStatuses, getPathologyStatuses()) ||
				CollectionUtils.isSubCollection(getPathologyStatuses(), pathologyStatuses);
	}
	
}
