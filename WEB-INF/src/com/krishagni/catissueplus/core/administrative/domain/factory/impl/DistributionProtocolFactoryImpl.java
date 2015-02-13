
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

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
import com.krishagni.catissueplus.core.common.util.Status;

public class DistributionProtocolFactoryImpl implements DistributionProtocolFactory {

	private static final String ACTIVITY_STATUS = "activity status";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public DistributionProtocol createDistributionProtocol(DistributionProtocolDetail detail) {
		DistributionProtocol distributionProtocol = new DistributionProtocol();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		distributionProtocol.setId(detail.getId());
		setTitle(detail, distributionProtocol, ose);
		setShortTitle(detail, distributionProtocol, ose);
		setPrincipalInvestigator(detail, distributionProtocol, ose);
		setIbrId(detail, distributionProtocol, ose);
		setStartDate(detail, distributionProtocol);
		setActivityStatus(detail, distributionProtocol, ose);
		
		ose.checkAndThrow();
		return distributionProtocol;
	}
	
	private void setTitle(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getTitle())) {
			ose.addError(DistributionProtocolErrorCode.TITLE_REQUIRED);
			return;
		}
		distributionProtocol.setTitle(detail.getTitle());

	}

	private void setShortTitle(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getShortTitle())) {
			ose.addError(DistributionProtocolErrorCode.SHORT_TITLE_REQUIRED);
			return;
		}
		distributionProtocol.setShortTitle(detail.getShortTitle());
	}
	
	private void setStartDate(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol) {
		distributionProtocol.setStartDate(detail.getStartDate());
	}

	private void setPrincipalInvestigator(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		User pi = null;
		if(detail.getPrincipalInvestigator() != null) {
			pi = daoFactory.getUserDao().getUser(detail.getPrincipalInvestigator().getId());
		}
		
		if (pi == null) {
			ose.addError(DistributionProtocolErrorCode.PI_NOT_FOUND);
			return;
		}
		
		distributionProtocol.setPrincipalInvestigator(pi);
	}

	private void setIbrId(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		distributionProtocol.setIrbId(detail.getIrbId());
	}

	private void setActivityStatus(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
		}
		
		activityStatus = StringUtils.isBlank(activityStatus) ? Status.ACTIVITY_STATUS_ACTIVE.getStatus() : activityStatus;
		distributionProtocol.setActivityStatus(activityStatus);
	}
}