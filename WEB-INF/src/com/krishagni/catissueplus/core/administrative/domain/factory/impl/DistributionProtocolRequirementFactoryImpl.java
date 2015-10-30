package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.PvAttributes.SPECIMEN_CLASS;
import static com.krishagni.catissueplus.core.common.PvAttributes.SPECIMEN_ANATOMIC_SITE;
import static com.krishagni.catissueplus.core.common.PvAttributes.PATH_STATUS;
import static com.krishagni.catissueplus.core.common.service.PvValidator.isValid;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocolRequirement;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolRequirementErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolRequirementFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolRequirementDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.NumUtil;
import com.krishagni.catissueplus.core.common.util.Status;

public class DistributionProtocolRequirementFactoryImpl implements DistributionProtocolRequirementFactory {
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public DistributionProtocolRequirement createDistributionProtocolRequirement(
			DistributionProtocolRequirementDetail detail) {
		DistributionProtocolRequirement dpr = new DistributionProtocolRequirement();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		dpr.setId(detail.getId());
		setDistributionProtocol(detail, dpr, ose);
		setSpecimenType(detail, dpr, ose);
		setAnatomicSite(detail, dpr, ose);
		setPathologyStatus(detail, dpr, ose);
		setSpecimenCount(detail, dpr, ose);
		setQuantity(detail, dpr, ose);
		setComments(detail, dpr, ose);
		setActivityStatus(detail, dpr, ose);
		
		ose.checkAndThrow();
		return dpr;
	}
	
	private void setDistributionProtocol(DistributionProtocolRequirementDetail detail,
			DistributionProtocolRequirement dpr, OpenSpecimenException ose) {
		DistributionProtocolSummary dps = detail.getDp();
		Long dpId = dps != null ? dps.getId() : null;
		String dpShortTitle = dps != null ? dps.getShortTitle() : null;
		
		if (dpId == null && StringUtils.isEmpty(dpShortTitle)) {
			ose.addError(DistributionProtocolRequirementErrorCode.DP_REQUIRED);
			return;
		}
		
		DistributionProtocol dp = null;
		if (dpId != null) {
			dp = daoFactory.getDistributionProtocolDao().getById(dpId);
		} else {
			dp = daoFactory.getDistributionProtocolDao().getByShortTitle(dpShortTitle);
		}
		
		if (dp == null) {
			ose.addError(DistributionProtocolErrorCode.NOT_FOUND);
			return;
		}
		
		dpr.setDistributionProtocol(dp);
	}
	
	private void setSpecimenType(DistributionProtocolRequirementDetail detail, DistributionProtocolRequirement dpr,
			OpenSpecimenException ose) {
		String specimenType = detail.getSpecimenType();
		if (StringUtils.isEmpty(specimenType)) {
			ose.addError(DistributionProtocolRequirementErrorCode.SPECIMEN_TYPE_REQUIRED);
			return;
		}
		
		if (!isValid(SPECIMEN_CLASS, 1, specimenType)) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_TYPE);
			return;
		}
		
		dpr.setSpecimenType(specimenType);
	}
	
	private void setAnatomicSite(DistributionProtocolRequirementDetail detail, DistributionProtocolRequirement dpr,
			OpenSpecimenException ose) {
		String anatomicSite = detail.getAnatomicSite();
		if (StringUtils.isEmpty(anatomicSite)) {
			ose.addError(DistributionProtocolRequirementErrorCode.ANATOMIC_SITE_REQUIRED);
			return;
		}
		
		if (!isValid(SPECIMEN_ANATOMIC_SITE, anatomicSite)) {
			ose.addError(SpecimenErrorCode.INVALID_ANATOMIC_SITE);
			return;
		}
		
		dpr.setAnatomicSite(anatomicSite);
	}
	
	private void setPathologyStatus(DistributionProtocolRequirementDetail detail, DistributionProtocolRequirement dpr,
			OpenSpecimenException ose) {
		String pathologyStatus = detail.getPathologyStatus();
		if (StringUtils.isEmpty(pathologyStatus)) {
			ose.addError(DistributionProtocolRequirementErrorCode.PATHOLOGY_STATUS_REQUIRED);
			return;
		}
		
		if (!isValid(PATH_STATUS, pathologyStatus)) {
			ose.addError(SpecimenErrorCode.INVALID_PATHOLOGY_STATUS);
			return;
		}
		
		dpr.setPathologyStatus(pathologyStatus);
	}
	
	private void setSpecimenCount(DistributionProtocolRequirementDetail detail, DistributionProtocolRequirement dpr,
			OpenSpecimenException ose) {
		Long specimenCount = detail.getSpecimenCount();
		if (specimenCount == null) {
			ose.addError(DistributionProtocolRequirementErrorCode.SPECIMEN_COUNT_REQUIRED);
			return;
		}
		
		if (specimenCount.compareTo(new Long(0)) <= 0) {
			ose.addError(DistributionProtocolRequirementErrorCode.INVALID_SPECIMEN_COUNT);
			return;
		}
		
		dpr.setSpecimenCount(specimenCount);
	}
	
	private void setQuantity(DistributionProtocolRequirementDetail detail, DistributionProtocolRequirement dpr,
			OpenSpecimenException ose) {
		BigDecimal quantity = detail.getQuantity();
		if (quantity == null) {
			ose.addError(DistributionProtocolRequirementErrorCode.QUANTITY_REQUIRED);
			return;
		}
		
		if (NumUtil.lessThanEqualsZero(quantity)) {
			ose.addError(DistributionProtocolRequirementErrorCode.INVALID_QUANTITY);
			return;
		}
		
		dpr.setQuantity(quantity);
	}
	
	private void setComments(DistributionProtocolRequirementDetail detail, DistributionProtocolRequirement dpr,
			OpenSpecimenException ose) {
		dpr.setComments(detail.getComments());
	}
	
	private void setActivityStatus(DistributionProtocolRequirementDetail detail, DistributionProtocolRequirement dpr,
			OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isEmpty(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		dpr.setActivityStatus(activityStatus);
	}
	
	private DaoFactory daoFactory;
}
