
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class DistributionProtocolFactoryImpl implements DistributionProtocolFactory {

	private static final String SHORT_TITLE = "short title";

	private static final String PRINCIPLE_INVESTIGATOR = "principle investigator";

	private static final String TITLE = "title";

	private static final String ACTIVITY_STATUS = "activity status";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public DistributionProtocol create(DistributionProtocolDetails details) {
		DistributionProtocol distributionProtocol = new DistributionProtocol();
		ObjectCreationException oce = new ObjectCreationException();
		
		setTitle(distributionProtocol, details.getTitle(), oce);
		setIbrId(distributionProtocol, details.getIrbId(), oce);
		setPrincipalInvestigator(distributionProtocol, details.getPrincipalInvestigator(), oce);
		setShortTitle(distributionProtocol, details.getShortTitle(), oce);
		setStartDate(distributionProtocol, details.getStartDate());
		setActivityStatus(distributionProtocol, details.getActivityStatus(), oce);
		
		oce.checkErrorAndThrow();
		
		return distributionProtocol;
	}

	private void setStartDate(DistributionProtocol distributionProtocol, Date startDate) {
		distributionProtocol.setStartDate(startDate);
	}

	private void setShortTitle(DistributionProtocol distributionProtocol, String shortTitle,
			ObjectCreationException oce) {
		if (isBlank(shortTitle)) {
			oce.addError(DistributionProtocolErrorCode.MISSING_ATTR_VALUE, SHORT_TITLE);
			return;
		}
		distributionProtocol.setShortTitle(shortTitle);
	}

	private void setPrincipalInvestigator(DistributionProtocol distributionProtocol, UserSummary principalInvestigator,
			ObjectCreationException oce) {

		User pi = daoFactory.getUserDao().getUser(principalInvestigator.getId());
		if (pi == null) {
			oce.addError(DistributionProtocolErrorCode.INVALID_PRINCIPAL_INVESTIGATOR, PRINCIPLE_INVESTIGATOR);
			return;
		}
		distributionProtocol.setPrincipalInvestigator(pi);
	}

	private void setIbrId(DistributionProtocol distributionProtocol, String irbId,
			ObjectCreationException oce) {
		distributionProtocol.setIrbId(irbId);

	}

	private void setTitle(DistributionProtocol distributionProtocol, String title,
			ObjectCreationException oce) {
		if (isBlank(title)) {
			oce.addError(DistributionProtocolErrorCode.MISSING_ATTR_VALUE, TITLE);
			return;
		}
		distributionProtocol.setTitle(title);

	}

	private void setActivityStatus(DistributionProtocol distributionProtocol, String activityStatus,
			ObjectCreationException oce) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			oce.addError(DistributionProtocolErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
		}
		
		activityStatus = activityStatus == null ? Status.ACTIVITY_STATUS_ACTIVE.getStatus() : activityStatus;
		distributionProtocol.setActivityStatus(activityStatus);
	}
}
