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
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
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

		ObjectCreationException oce = new ObjectCreationException();

		cp.setId(input.getId());
		setTitle(input, cp, oce);
		setShortTitle(input, cp, oce);
		setPrincipalInvestigator(input, cp, oce);
		cp.setStartDate(input.getStartDate());
		setCoordinators(input, cp, oce);
		setConsentsWaived(input, cp, oce);

		cp.setIrbIdentifier(input.getIrbId());
		cp.setPpidFormat(input.getPpidFmt());
		cp.setEnrollment(input.getAnticipatedParticipantsCount());
		cp.setDescriptionURL(input.getDescriptionUrl());
		cp.setSpecimenLabelFormat(input.getSpecimenLabelFmt());
		cp.setDerivativeLabelFormat(input.getDerivativeLabelFmt());
		cp.setAliquotLabelFormat(input.getAliquotLabelFmt());
		cp.setAliquotInSameContainer(input.getAliquotsInSameContainer());

		setActivityStatus(input, cp, oce);

		oce.checkErrorAndThrow();
		return cp;
	}

	private void setTitle(CollectionProtocolDetail input, CollectionProtocol result, ObjectCreationException oce) {
		if (StringUtils.isBlank(input.getTitle())) {
			oce.addError(CpErrorCode.MISSING_TITLE, "title");
			return;
		}

		result.setTitle(input.getTitle());
	}
	
	private void setShortTitle(CollectionProtocolDetail input, CollectionProtocol result, ObjectCreationException oce) {
		if (StringUtils.isBlank(input.getShortTitle())) {
			oce.addError(CpErrorCode.MISSING_SHORT_TITLE, "shortTitle");
			return;
		}

		result.setShortTitle(input.getShortTitle());
	}

	private void setPrincipalInvestigator(CollectionProtocolDetail input, CollectionProtocol result, ObjectCreationException oce) {
		UserSummary user = input.getPrincipalInvestigator();
		Long piId = user != null ? user.getId() : null;
		if (piId == null) {
			oce.addError(CpErrorCode.MISSING_PI, "principalInvestigator");
			return;
		}

		User pi = userDao.getUser(piId);
		if (pi == null) {
			oce.addError(CpErrorCode.INVALID_PI, "principalInvestigator");
			return;
		}

		result.setPrincipalInvestigator(pi);
	}

	private void setCoordinators(CollectionProtocolDetail input, CollectionProtocol result, ObjectCreationException oce) {		
		List<Long> userIds = new ArrayList<Long>();

		List<UserSummary> users = input.getCoordinators();
		if (CollectionUtils.isEmpty(users)) {
			return;
		}

		for (UserSummary user : users) {
			if (user.getId() == null) {
				oce.addError(CpErrorCode.INVALID_COORDINATORS, "coordinators");
				return;
			}

			userIds.add(user.getId());
		}

		List<User> coordinators = userDao.getUsersById(userIds);
		if (coordinators.size() != userIds.size()) {
			oce.addError(CpErrorCode.INVALID_COORDINATORS, "coordinators");
			return;
		}

		result.setCoordinators(new HashSet<User>(coordinators));
	}

	private void setConsentsWaived(CollectionProtocolDetail input, CollectionProtocol result, ObjectCreationException oce) {
		if (input.getConsentsWaived() == null) {
			oce.addError(CpErrorCode.MISSING_CONSENTS_WAIVED, "consentsWaived");
			return;
		}

		result.setConsentsWaived(input.getConsentsWaived());
	}

	private void setActivityStatus(CollectionProtocolDetail input, CollectionProtocol result, ObjectCreationException oce) {
		if (StringUtils.isBlank(input.getActivityStatus())) {
			result.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
			return;
		}

		// TODO: validate activity status
		result.setActivityStatus(input.getActivityStatus());
	}
}
