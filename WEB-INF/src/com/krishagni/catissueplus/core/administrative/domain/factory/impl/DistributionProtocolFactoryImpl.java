
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
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
	public DistributionProtocol createDistributionProtocol(DistributionProtocolDetail detail) {
		DistributionProtocol distributionProtocol = new DistributionProtocol();
		ObjectCreationException oce = new ObjectCreationException();
		
		distributionProtocol.setId(detail.getId());
		setTitle(detail, distributionProtocol, oce);
		setShortTitle(detail, distributionProtocol, oce);
		setPrincipalInvestigator(detail, distributionProtocol, oce);
		setIbrId(detail, distributionProtocol, oce);
		setStartDate(detail, distributionProtocol);
		setActivityStatus(detail, distributionProtocol, oce);
		
		oce.checkErrorAndThrow();
		return distributionProtocol;
	}
	
	private void setTitle(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, ObjectCreationException oce) {
		if (StringUtils.isBlank(detail.getTitle())) {
			oce.addError(DistributionProtocolErrorCode.MISSING_ATTR_VALUE, TITLE);
			return;
		}
		distributionProtocol.setTitle(detail.getTitle());

	}

	private void setShortTitle(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, ObjectCreationException oce) {
		if (StringUtils.isBlank(detail.getShortTitle())) {
			oce.addError(DistributionProtocolErrorCode.MISSING_ATTR_VALUE, SHORT_TITLE);
			return;
		}
		distributionProtocol.setShortTitle(detail.getShortTitle());
	}
	
	private void setStartDate(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol) {
		distributionProtocol.setStartDate(detail.getStartDate());
	}

	private void setPrincipalInvestigator(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, ObjectCreationException oce) {

		User pi = daoFactory.getUserDao().getUser(detail.getPrincipalInvestigator().getId());
		if (pi == null) {
			oce.addError(DistributionProtocolErrorCode.INVALID_PRINCIPAL_INVESTIGATOR, PRINCIPLE_INVESTIGATOR);
			return;
		}
		distributionProtocol.setPrincipalInvestigator(pi);
	}

	private void setIbrId(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, ObjectCreationException oce) {
		distributionProtocol.setIrbId(detail.getIrbId());
	}

	private void setActivityStatus(DistributionProtocolDetail detail, DistributionProtocol distributionProtocol, ObjectCreationException oce) {
		String activityStatus = detail.getActivityStatus();
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			oce.addError(DistributionProtocolErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
		}
		
		activityStatus = StringUtils.isBlank(activityStatus) ? Status.ACTIVITY_STATUS_ACTIVE.getStatus() : activityStatus;
		distributionProtocol.setActivityStatus(activityStatus);
	}
}
