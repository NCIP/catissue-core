package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.PvAttributes.CLINICAL_DIAG;
import static com.krishagni.catissueplus.core.common.PvAttributes.SPECIMEN_CLASS;
import static com.krishagni.catissueplus.core.common.PvAttributes.SPECIMEN_ANATOMIC_SITE;
import static com.krishagni.catissueplus.core.common.PvAttributes.PATH_STATUS;
import static com.krishagni.catissueplus.core.common.service.PvValidator.isValid;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.DpRequirement;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DpRequirementErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DpRequirementFactory;
import com.krishagni.catissueplus.core.administrative.events.DpRequirementDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.NumUtil;
import com.krishagni.catissueplus.core.common.util.Status;

public class DpRequirementFactoryImpl implements DpRequirementFactory {
	
	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public DpRequirement createDistributionProtocolRequirement(DpRequirementDetail detail) {
		DpRequirement dpr = new DpRequirement();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		dpr.setId(detail.getId());
		setDistributionProtocol(detail, dpr, ose);
		setSpecimenType(detail, dpr, ose);
		setAnatomicSite(detail, dpr, ose);
		setPathologyStatus(detail, dpr, ose);
		setClinicalDiagnosis(detail, dpr, ose);
		setSpecimenCount(detail, dpr, ose);
		setQuantity(detail, dpr, ose);
		setComments(detail, dpr, ose);
		setActivityStatus(detail, dpr, ose);
		
		ose.checkAndThrow();
		return dpr;
	}
	
	private void setDistributionProtocol(DpRequirementDetail detail, DpRequirement dpr, OpenSpecimenException ose) {
		DistributionProtocolSummary dps = detail.getDp();
		Long dpId = dps != null ? dps.getId() : null;
		String dpShortTitle = dps != null ? dps.getShortTitle() : null;
		
		if (dpId == null && StringUtils.isBlank(dpShortTitle)) {
			ose.addError(DpRequirementErrorCode.DP_REQUIRED);
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
	
	private void setSpecimenType(DpRequirementDetail detail, DpRequirement dpr, OpenSpecimenException ose) {
		String specimenType = detail.getSpecimenType();
		if (!isValid(SPECIMEN_CLASS, 1, specimenType, true)) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_TYPE);
			return;
		}
		
		dpr.setSpecimenType(specimenType);
	}
	
	private void setAnatomicSite(DpRequirementDetail detail, DpRequirement dpr, OpenSpecimenException ose) {
		String anatomicSite = detail.getAnatomicSite();
		if (!isValid(SPECIMEN_ANATOMIC_SITE, anatomicSite)) {
			ose.addError(SpecimenErrorCode.INVALID_ANATOMIC_SITE);
			return;
		}
		
		dpr.setAnatomicSite(anatomicSite);
	}
	
	private void setPathologyStatus(DpRequirementDetail detail, DpRequirement dpr, OpenSpecimenException ose) {
		String pathologyStatus = detail.getPathologyStatus();
		if (!isValid(PATH_STATUS, pathologyStatus)) {
			ose.addError(SpecimenErrorCode.INVALID_PATHOLOGY_STATUS);
			return;
		}
		
		dpr.setPathologyStatus(pathologyStatus);
	}

	private void setClinicalDiagnosis(DpRequirementDetail detail, DpRequirement dpr, OpenSpecimenException ose) {
		String clinicalDiagnosis = detail.getClinicalDiagnosis();
		if (!isValid(CLINICAL_DIAG, clinicalDiagnosis)) {
			ose.addError(VisitErrorCode.INVALID_CLINICAL_DIAGNOSIS);
			return;
		}

		dpr.setClinicalDiagnosis(detail.getClinicalDiagnosis());
	}
	
	private void setSpecimenCount(DpRequirementDetail detail, DpRequirement dpr, OpenSpecimenException ose) {
		Long specimenCount = detail.getSpecimenCount();
		if (specimenCount == null) {
			ose.addError(DpRequirementErrorCode.SPECIMEN_COUNT_REQUIRED);
			return;
		}
		
		if (specimenCount <= 0L) {
			ose.addError(DpRequirementErrorCode.INVALID_SPECIMEN_COUNT);
			return;
		}
		
		dpr.setSpecimenCount(specimenCount);
	}
	
	private void setQuantity(DpRequirementDetail detail, DpRequirement dpr, OpenSpecimenException ose) {
		BigDecimal quantity = detail.getQuantity();
		if (quantity == null) {
			ose.addError(DpRequirementErrorCode.QUANTITY_REQUIRED);
			return;
		}
		
		if (NumUtil.lessThanEqualsZero(quantity)) {
			ose.addError(DpRequirementErrorCode.INVALID_QUANTITY);
			return;
		}
		
		dpr.setQuantity(quantity);
	}
	
	private void setComments(DpRequirementDetail detail, DpRequirement dpr, OpenSpecimenException ose) {
		dpr.setComments(detail.getComments());
	}
	
	private void setActivityStatus(DpRequirementDetail detail, DpRequirement dpr, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		dpr.setActivityStatus(activityStatus);
	}
	
}
