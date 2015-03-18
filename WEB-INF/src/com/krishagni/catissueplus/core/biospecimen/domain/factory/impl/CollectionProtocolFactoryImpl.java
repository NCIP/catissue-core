package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class CollectionProtocolFactoryImpl implements CollectionProtocolFactory {
	private DaoFactory daoFactory;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public CollectionProtocol createCollectionProtocol(CollectionProtocolDetail input) {
		CollectionProtocol cp = new CollectionProtocol();

		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		cp.setId(input.getId());
		setRepositories(input, cp, ose);
		setTitle(input, cp, ose);
		setShortTitle(input, cp, ose);
		setPrincipalInvestigator(input, cp, ose);
		cp.setStartDate(input.getStartDate());
		cp.setEndDate(input.getEndDate());
		setCoordinators(input, cp, ose);

		cp.setIrbIdentifier(input.getIrbId());
		cp.setPpidFormat(input.getPpidFmt());
		cp.setEnrollment(input.getAnticipatedParticipantsCount());
		cp.setDescriptionURL(input.getDescriptionUrl());
		cp.setSpecimenLabelFormat(input.getSpecimenLabelFmt());
		cp.setDerivativeLabelFormat(input.getDerivativeLabelFmt());
		cp.setAliquotLabelFormat(input.getAliquotLabelFmt());

		setActivityStatus(input, cp, ose);

		ose.checkAndThrow();
		return cp;
	}
	
	private void setRepositories(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		List<String> repositoryNames = input.getRepositoryNames();
		if (CollectionUtils.isEmpty(repositoryNames)) {
			ose.addError(CpErrorCode.REPOSITORIES_REQUIRED);
			return;
		}
		
		List<Site> repositories = daoFactory.getSiteDao().getSitesByNames(repositoryNames);
		if (repositories.size() != repositoryNames.size()) {
			ose.addError(CpErrorCode.INVALID_REPOSITORIES);
			return;
		}
		
		result.setRepositories(new HashSet<Site>(repositories));
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
		User pi = null;
		if (user != null && user.getId() != null) {
			pi = daoFactory.getUserDao().getById(user.getId());
		} else if (user != null && user.getLoginName() != null && user.getDomain() != null) {
			pi = daoFactory.getUserDao().getUser(user.getLoginName(), user.getDomain());
		} else {			
			ose.addError(CpErrorCode.PI_REQUIRED);
			return;
		}
		if (pi == null) {
			ose.addError(CpErrorCode.PI_NOT_FOUND);
			return;
		}

		result.setPrincipalInvestigator(pi);
	}
	
	private void setCoordinators(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {		
		List<UserSummary> users = input.getCoordinators();
		if (CollectionUtils.isEmpty(users)) {
			return;
		}
		
		Set<User> coordinators = new HashSet<User>();		
		for (UserSummary user : users) {
			User coordinator = null;
			
			if (user.getId() != null) {
				coordinator = daoFactory.getUserDao().getById(user.getId());
			} else if (user.getLoginName() != null && user.getDomain() != null) {
				coordinator = daoFactory.getUserDao().getUser(user.getLoginName(), user.getDomain());
			} else {
				ose.addError(CpErrorCode.INVALID_COORDINATORS);
				return;
			}
			
			coordinators.add(coordinator);
		}

		result.setCoordinators(coordinators);
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
