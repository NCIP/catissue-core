
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class DistributionProtocolFactoryImpl implements DistributionProtocolFactory {

	private static final String ACTIVITY_STATUS = "activity status";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public DistributionProtocol create(DistributionProtocolDetail details) {
		DistributionProtocol distributionProtocol = new DistributionProtocol();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		setTitle(distributionProtocol, details.getTitle(), ose);
		setIbrId(distributionProtocol, details.getIrbId(), ose);
		setAnticipatedSpecimenCount(distributionProtocol, details.getAnticipatedSpecimenCount());
		setPrincipalInvestigator(distributionProtocol, details.getPrincipalInvestigator(), ose);
		setShortTitle(distributionProtocol, details.getShortTitle(), ose);
		setDescriptionURL(distributionProtocol, details.getDescriptionUrl(), ose);
		setStartDate(distributionProtocol, details.getStartDate());
		setActivityStatus(distributionProtocol, details.getActivityStatus(), ose);
		
		ose.checkAndThrow();		
		return distributionProtocol;
	}

	private void setStartDate(DistributionProtocol distributionProtocol, Date startDate) {
		distributionProtocol.setStartDate(startDate);
	}

	private void setDescriptionURL(DistributionProtocol distributionProtocol, String descriptionURL, OpenSpecimenException ose) {
		distributionProtocol.setDescriptionUrl(descriptionURL);
	}

	private void setShortTitle(DistributionProtocol distributionProtocol, String shortTitle, OpenSpecimenException ose) {
		if (StringUtils.isBlank(shortTitle)) {
			ose.addError(DistributionProtocolErrorCode.SHORT_TITLE_REQUIRED);
			return;
		}
		
		distributionProtocol.setShortTitle(shortTitle);
	}

	private void setPrincipalInvestigator(DistributionProtocol distributionProtocol, UserSummary principalInvestigator, OpenSpecimenException ose) {
		User pi = daoFactory.getUserDao().getById(principalInvestigator.getId());
		if (pi == null) {
			ose.addError(DistributionProtocolErrorCode.PI_NOT_FOUND);
			return;
		}
		
		distributionProtocol.setPrincipalInvestigator(pi);
	}

	private void setAnticipatedSpecimenCount(DistributionProtocol distributionProtocol, Long anticipatedSpecimenCount) {
		distributionProtocol.setAnticipatedSpecimenCount(anticipatedSpecimenCount);

	}

	private void setIbrId(DistributionProtocol distributionProtocol, String irbId, OpenSpecimenException ose) {
		distributionProtocol.setIrbId(irbId);

	}

	private void setTitle(DistributionProtocol distributionProtocol, String title,	OpenSpecimenException ose) {
		if (StringUtils.isBlank(title)) {
			ose.addError(DistributionProtocolErrorCode.TITLE_REQUIRED);
			return;
		}
		
		distributionProtocol.setTitle(title);
	}

	private void setActivityStatus(DistributionProtocol distributionProtocol, String activityStatus, OpenSpecimenException ose) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
		}
		
		activityStatus = activityStatus == null ? Status.ACTIVITY_STATUS_ACTIVE.getStatus() : activityStatus;
		distributionProtocol.setActivityStatus(activityStatus);
	}
}
