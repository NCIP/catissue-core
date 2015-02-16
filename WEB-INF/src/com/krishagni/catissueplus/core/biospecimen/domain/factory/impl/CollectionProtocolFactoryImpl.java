package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class CollectionProtocolFactoryImpl implements CollectionProtocolFactory {
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public CollectionProtocol createCollectionProtocol(CollectionProtocolDetail input) {
		CollectionProtocol cp = new CollectionProtocol();

		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		cp.setId(input.getId());
		setTitle(input, cp, ose);
		setShortTitle(input, cp, ose);
		setPrincipalInvestigator(input, cp, ose);
		cp.setStartDate(input.getStartDate());
		setCoordinators(input, cp, ose);
		setConsentsWaived(input, cp, ose);

		cp.setIrbIdentifier(input.getIrbId());
		cp.setPpidFormat(input.getPpidFmt());
		cp.setEnrollment(input.getAnticipatedParticipantsCount());
		cp.setDescriptionURL(input.getDescriptionUrl());
		cp.setSpecimenLabelFormat(input.getSpecimenLabelFmt());
		cp.setDerivativeLabelFormat(input.getDerivativeLabelFmt());
		cp.setAliquotLabelFormat(input.getAliquotLabelFmt());
		cp.setAliquotInSameContainer(input.getAliquotsInSameContainer());

		setActivityStatus(input, cp, ose);

		ose.checkAndThrow();
		return cp;
	}

	private void setTitle(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getTitle())) {
			ose.addError(CpErrorCode.TITLE_REQUIRED);
			return;
		}

		result.setTitle(input.getTitle());
	}
	
	private void setShortTitle(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getShortTitle())) {
			ose.addError(CpErrorCode.SHORT_TITLE_REQUIRED);
			return;
		}

		result.setShortTitle(input.getShortTitle());
	}

	private void setPrincipalInvestigator(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		UserSummary user = input.getPrincipalInvestigator();
		Long piId = user != null ? user.getId() : null;
		if (piId == null) {
			ose.addError(CpErrorCode.PI_REQUIRED);
			return;
		}

		User pi = userDao.getById(piId);
		if (pi == null) {
			ose.addError(CpErrorCode.PI_NOT_FOUND);
			return;
		}

		result.setPrincipalInvestigator(pi);
	}

	private void setCoordinators(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {		
		List<Long> userIds = new ArrayList<Long>();

		List<UserSummary> users = input.getCoordinators();
		if (CollectionUtils.isEmpty(users)) {
			return;
		}

		for (UserSummary user : users) {
			if (user.getId() == null) {
				ose.addError(CpErrorCode.INVALID_COORDINATORS);
				return;
			}

			userIds.add(user.getId());
		}

		List<User> coordinators = userDao.getUsersByIds(userIds);
		if (coordinators.size() != userIds.size()) {
			ose.addError(CpErrorCode.INVALID_COORDINATORS);
			return;
		}

		result.setCoordinators(new HashSet<User>(coordinators));
	}

	private void setConsentsWaived(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		if (input.getConsentsWaived() == null) {
			ose.addError(CpErrorCode.CONSENTS_WAIVED_REQUIRED);
			return;
		}

		result.setConsentsWaived(input.getConsentsWaived());
	}

	private void setActivityStatus(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getActivityStatus())) {
			result.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
			return;
		}

		// TODO: validate activity status
		result.setActivityStatus(input.getActivityStatus());
	}
}
