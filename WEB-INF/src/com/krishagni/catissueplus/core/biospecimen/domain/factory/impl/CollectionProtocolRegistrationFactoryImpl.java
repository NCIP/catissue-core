
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;


public class CollectionProtocolRegistrationFactoryImpl implements CollectionProtocolRegistrationFactory {
	private DaoFactory daoFactory;

	private ParticipantFactory participantFactory;

	public void setParticipantFactory(ParticipantFactory participantFactory) {
		this.participantFactory = participantFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetail detail) {
		return createCpr(null, detail);
	}

	@Override
	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistration existing, CollectionProtocolRegistrationDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setForceDelete(detail.isForceDelete());
		setBarcode(detail, existing, cpr, ose);
		setRegDate(detail, existing, cpr, ose);
		setActivityStatus(detail, existing, cpr, ose);
		setCollectionProtocol(detail, existing, cpr, ose);
		setPpid(detail, existing, cpr, ose);
		setParticipant(detail, existing, cpr, ose);

		ose.checkAndThrow();
		return cpr;
	}

	private void setBarcode(
			CollectionProtocolRegistrationDetail detail,
			CollectionProtocolRegistration existing,
			CollectionProtocolRegistration cpr,
			OpenSpecimenException ose) {

		if (existing == null || detail.isAttrModified("barcode")) {
			cpr.setBarcode(detail.getBarcode());
		} else {
			cpr.setBarcode(existing.getBarcode());
		}
	}

	private void setRegDate(CollectionProtocolRegistrationDetail detail, CollectionProtocolRegistration cpr, OpenSpecimenException ose) {
		if (detail.getRegistrationDate() == null) {
			ose.addError(CprErrorCode.REG_DATE_REQUIRED);
			return;
		}
		
		cpr.setRegistrationDate(detail.getRegistrationDate());
	}

	private void setRegDate(
			CollectionProtocolRegistrationDetail detail,
			CollectionProtocolRegistration existing,
			CollectionProtocolRegistration cpr,
			OpenSpecimenException ose) {

		if (existing == null || detail.isAttrModified("registrationDate")) {
			setRegDate(detail, cpr, ose);
		} else {
			cpr.setRegistrationDate(existing.getRegistrationDate());
		}
	}

	private void setActivityStatus(CollectionProtocolRegistrationDetail detail, CollectionProtocolRegistration cpr, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		
		if (StringUtils.isBlank(activityStatus)) {
			cpr.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		} else if (Status.isValidActivityStatus(activityStatus)) {
			cpr.setActivityStatus(activityStatus);
		} else {
			ose.addError(ActivityStatusErrorCode.INVALID);
		}
	}

	private void setActivityStatus(
			CollectionProtocolRegistrationDetail detail,
			CollectionProtocolRegistration existing,
			CollectionProtocolRegistration cpr,
			OpenSpecimenException ose) {

		if (existing == null || detail.isAttrModified("activityStatus")) {
			setActivityStatus(detail, cpr, ose);
		} else {
			cpr.setActivityStatus(existing.getActivityStatus());
		}
	}

	private void setCollectionProtocol(
			CollectionProtocolRegistrationDetail detail,
			CollectionProtocolRegistration cpr, 			 
			OpenSpecimenException ose) {
				
		Long cpId = detail.getCpId();
		String title = detail.getCpTitle();
		String shortTitle = detail.getCpShortTitle();
		
		CollectionProtocol protocol = null;
		if (cpId != null) {
			protocol = daoFactory.getCollectionProtocolDao().getById(detail.getCpId());
		} else if (StringUtils.isNotBlank(title)) {
			protocol = daoFactory.getCollectionProtocolDao().getCollectionProtocol(title);
		} else if (StringUtils.isNotBlank(shortTitle)) {
			protocol = daoFactory.getCollectionProtocolDao().getCpByShortTitle(shortTitle);
		} else {
			ose.addError(CprErrorCode.CP_REQUIRED);
			return;
		} 
		
		if (protocol == null) {
			ose.addError(CpErrorCode.NOT_FOUND);
			return;
		}
		
		if (!Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(protocol.getActivityStatus())) {
			ose.addError(CpErrorCode.NOT_FOUND);
			return;
		}
		
		cpr.setCollectionProtocol(protocol);
	}

	private void setCollectionProtocol(
			CollectionProtocolRegistrationDetail detail,
			CollectionProtocolRegistration existing,
			CollectionProtocolRegistration cpr,
			OpenSpecimenException ose) {

		if (existing == null) {
			setCollectionProtocol(detail, cpr, ose);
		} else {
			cpr.setCollectionProtocol(existing.getCollectionProtocol());
		}
	}

	private void setPpid(
			CollectionProtocolRegistrationDetail detail,
			CollectionProtocolRegistration existing,
			CollectionProtocolRegistration cpr, 
			OpenSpecimenException ose) {

		if (existing == null || detail.isAttrModified("ppid")) {
			cpr.setPpid(detail.getPpid());
		} else {
			cpr.setPpid(existing.getPpid());
		}
	}
	
	private void setParticipant(
			CollectionProtocolRegistrationDetail detail,
			CollectionProtocolRegistration existing,
			CollectionProtocolRegistration cpr,
			OpenSpecimenException ose) {
		
		ParticipantDetail participantDetail = detail.getParticipant();
		if (participantDetail == null) {
			participantDetail = new ParticipantDetail();
		}

		if (existing != null && participantDetail.getId() == null) {
			participantDetail.setId(existing.getParticipant().getId());
		}

		if (cpr.getCollectionProtocol() != null) {
			participantDetail.setCpId(cpr.getCollectionProtocol().getId());
		}

		Participant participant;
		if (participantDetail.getId() == null) {
			participant = participantFactory.createParticipant(participantDetail);			
			if (participant == null) {
				ose.addError(CprErrorCode.PARTICIPANT_DETAIL_REQUIRED);
			}
		} else {
			participant = daoFactory.getParticipantDao().getById(participantDetail.getId());
			if (participant == null) {
				ose.addError(ParticipantErrorCode.NOT_FOUND);
			} else {
				participant = participantFactory.createParticipant(participant, participantDetail);
			}			
		}
		
		if (participant == null) {
			return;
		}
		
		cpr.setParticipant(participant);
	}

}