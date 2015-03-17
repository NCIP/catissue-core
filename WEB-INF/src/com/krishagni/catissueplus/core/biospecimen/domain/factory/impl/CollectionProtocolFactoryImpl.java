package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.UserDao;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.util.Status;

public class CollectionProtocolFactoryImpl implements CollectionProtocolFactory {
	private UserDao userDao;
	
	private LabelGenerator specimenLabelGenerator;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setSpecimenLabelGenerator(LabelGenerator specimenLabelGenerator) {
		this.specimenLabelGenerator = specimenLabelGenerator;
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
		cp.setAliquotInSameContainer(input.getAliquotsInSameContainer());

		setLabelFormats(input, cp, ose);
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
		User pi = null;
		if (user != null && user.getId() != null) {
			pi = userDao.getById(user.getId());
		} else if (user != null && user.getLoginName() != null && user.getDomain() != null) {
			pi = userDao.getUser(user.getLoginName(), user.getDomain());
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
				coordinator = userDao.getById(user.getId());
			} else if (user.getLoginName() != null && user.getDomain() != null) {
				coordinator = userDao.getUser(user.getLoginName(), user.getDomain());
			} else {
				ose.addError(CpErrorCode.INVALID_COORDINATORS);
				return;
			}
			
			coordinators.add(coordinator);
		}

		result.setCoordinators(coordinators);
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
	
	private void setLabelFormats(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		String labelFmt = ensureValidLabelFmt(input.getSpecimenLabelFmt(), CpErrorCode.INVALID_SPECIMEN_LABEL_FMT, ose);		
		result.setSpecimenLabelFormat(labelFmt);
		
		labelFmt = ensureValidLabelFmt(input.getAliquotLabelFmt(), CpErrorCode.INVALID_ALIQUOT_LABEL_FMT, ose);
		result.setAliquotLabelFormat(labelFmt);
		
		labelFmt = ensureValidLabelFmt(input.getDerivativeLabelFmt(), CpErrorCode.INVALID_DERIVATIVE_LABEL_FMT, ose);
		result.setDerivativeLabelFormat(labelFmt);
	}
	
	private String ensureValidLabelFmt(String labelFmt, ErrorCode error, OpenSpecimenException ose) {
		if (StringUtils.isNotBlank(labelFmt) && !specimenLabelGenerator.isValidLabelTmpl(labelFmt)) {
			ose.addError(error);
		}
		
		return labelFmt;
	}
}
