
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPositiveNumber;

import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolPatchDetails;
import com.krishagni.catissueplus.core.administrative.events.UserInfo;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class DistributionProtocolFactoryImpl implements DistributionProtocolFactory {

	private static final String START_DATE = "start date";

	private static final String SHORT_TITLE = "short title";

	private static final String PRINCIPLE_INVESTIGATOR = "principle investigator";

	private static final String TITLE = "title";

	private static final String ACTIVITY_STATUS = "activity status";

	private static final String ANTICIPANTED_SPECIMEN_COUNT = "anticipated specimen count";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public DistributionProtocol create(DistributionProtocolDetails details) {
		DistributionProtocol distributionProtocol = new DistributionProtocol();
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		setTitle(distributionProtocol, details.getTitle(), exceptionHandler);
		setIbrId(distributionProtocol, details.getIrbId(), exceptionHandler);
		setAnticipatedSpecimenCount(distributionProtocol, details.getAnticipatedSpecimenCount(), exceptionHandler);
		setPrincipalInvestigator(distributionProtocol, details.getPrincipalInvestigator(), exceptionHandler);
		setShortTitle(distributionProtocol, details.getShortTitle(), exceptionHandler);
		setDescriptionURL(distributionProtocol, details.getDescriptionUrl(), exceptionHandler);
		setStartDate(distributionProtocol, details.getStartDate(), exceptionHandler);
		setActivityStatus(distributionProtocol, details.getActivityStatus(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return distributionProtocol;
	}

	@Override
	public DistributionProtocol patch(DistributionProtocol distributionProtocol, DistributionProtocolPatchDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();

		if (details.isDistributionProtocolTitleModified()) {
			setTitle(distributionProtocol, details.getTitle(), exceptionHandler);
		}

		if (details.isDistributionProtocolPrincipalInvestigatorModified()) {
			setPrincipalInvestigator(distributionProtocol, details.getPrincipalInvestigator(), exceptionHandler);
		}

		if (details.isDistributionProtocolIrbIdModified()) {
			setIbrId(distributionProtocol, details.getIrbId(), exceptionHandler);
		}

		if (details.isDistributionProtocolShortTitleModified()) {
			setShortTitle(distributionProtocol, details.getShortTitle(), exceptionHandler);
		}

		if (details.isDistributionProtocolDescriptionUrlModified()) {
			setDescriptionURL(distributionProtocol, details.getDescriptionUrl(), exceptionHandler);
		}

		if (details.isDistributionProtocolAnticipatedSpecimenCountModified()) {
			setAnticipatedSpecimenCount(distributionProtocol, details.getAnticipatedSpecimenCount(), exceptionHandler);
		}

		if (details.isDistributionProtocolStartDateModified()) {
			setStartDate(distributionProtocol, details.getStartDate(), exceptionHandler);
		}

		if (details.isDistributionProtocolActivityStatusModified()) {
			setActivityStatus(distributionProtocol, details.getActivityStatus(), exceptionHandler);
		}
		exceptionHandler.checkErrorAndThrow();
		return distributionProtocol;
	}

	private void setStartDate(DistributionProtocol distributionProtocol, Date startDate,
			ObjectCreationException exceptionHandler) {
		if (startDate == null) {
			exceptionHandler.addError(DistributionProtocolErrorCode.INVALID_ATTR_VALUE, START_DATE);
			return;
		}
		distributionProtocol.setStartDate(startDate);
	}

	private void setDescriptionURL(DistributionProtocol distributionProtocol, String descriptionURL,
			ObjectCreationException exceptionHandler) {
		distributionProtocol.setDescriptionUrl(descriptionURL);
	}

	private void setShortTitle(DistributionProtocol distributionProtocol, String shortTitle,
			ObjectCreationException exceptionHandler) {
		if (isBlank(shortTitle)) {
			exceptionHandler.addError(DistributionProtocolErrorCode.MISSING_ATTR_VALUE, SHORT_TITLE);
			return;
		}
		distributionProtocol.setShortTitle(shortTitle);
	}

	private void setPrincipalInvestigator(DistributionProtocol distributionProtocol, UserInfo principalInvestigator,
			ObjectCreationException exceptionHandler) {

		if (isBlank(principalInvestigator.getLoginName())) {
			exceptionHandler.addError(DistributionProtocolErrorCode.MISSING_ATTR_VALUE, PRINCIPLE_INVESTIGATOR);
			return;
		}

		User pi = daoFactory.getUserDao().getUserByLoginNameAndDomainName(principalInvestigator.getLoginName(),
				principalInvestigator.getDomainName());
		if (pi == null) {
			exceptionHandler.addError(DistributionProtocolErrorCode.INVALID_PRINCIPAL_INVESTIGATOR, PRINCIPLE_INVESTIGATOR);
			return;
		}
		distributionProtocol.setPrincipalInvestigator(pi);
	}

	private void setAnticipatedSpecimenCount(DistributionProtocol distributionProtocol, Long anticipatedSpecimenCount,
			ObjectCreationException exceptionHandler) {
		if (!isValidPositiveNumber(anticipatedSpecimenCount)) {
			exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, ANTICIPANTED_SPECIMEN_COUNT);
			return;
		}
		distributionProtocol.setAnticipatedSpecimenCount(anticipatedSpecimenCount);

	}

	private void setIbrId(DistributionProtocol distributionProtocol, String irbId,
			ObjectCreationException exceptionHandler) {
		distributionProtocol.setIrbId(irbId);

	}

	private void setTitle(DistributionProtocol distributionProtocol, String title,
			ObjectCreationException exceptionHandler) {
		if (isBlank(title)) {
			exceptionHandler.addError(DistributionProtocolErrorCode.MISSING_ATTR_VALUE, TITLE);
			return;
		}
		distributionProtocol.setTitle(title);

	}

	private void setActivityStatus(DistributionProtocol distributionProtocol, String activityStatus,
			ObjectCreationException exceptionHandler) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			exceptionHandler.addError(DistributionProtocolErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
		}
		distributionProtocol.setActivityStatus(activityStatus);
	}
}
