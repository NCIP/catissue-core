
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTierResponse;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierResponseDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.util.PpidGenerator;
import com.krishagni.catissueplus.core.biospecimen.util.impl.PpidGeneratorImpl;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.KeyGenFactory;
import com.krishagni.catissueplus.core.common.util.Status;


public class CollectionProtocolRegistrationFactoryImpl implements CollectionProtocolRegistrationFactory {

	private DaoFactory daoFactory;

	private final String CONSENT_RESP_NOT_SPECIFIED = "Not Specified";

	private ParticipantFactory participantFactory;

	private KeyGenFactory keyFactory;

	public void setKeyFactory(KeyGenFactory keyFactory) {
		this.keyFactory = keyFactory;
	}

	public void setParticipantFactory(ParticipantFactory participantFactory) {
		this.participantFactory = participantFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	/**
	 * This method validate and populates collection protocol registration with the given details.
	 * @param detail
	 * @returns instance of CollectionProtocolRegistration
	 */
	@Override
	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setBarcode(detail.getBarcode());
		setRegistrationDate(cpr, detail.getRegistrationDate(), ose);
		setActivityStatus(cpr, detail.getActivityStatus(), ose);
		setCollectionProtocol(cpr, detail, ose);
		setConsents(cpr, detail, ose);
		setPpid(cpr, detail.getPpid(), ose);
		setParticipant(cpr, detail.getParticipant(), ose);
		
		ose.checkAndThrow();
		return cpr;
	}
	
	private void setRegistrationDate(CollectionProtocolRegistration cpr, Date regDate, OpenSpecimenException ose) {
		if (regDate == null) {
			ose.addError(CprErrorCode.REG_DATE_REQUIRED);
			return;
		}
		
		cpr.setRegistrationDate(regDate);
	}

	private void setActivityStatus(CollectionProtocolRegistration registration, String activityStatus, OpenSpecimenException ose) {
		if (StringUtils.isBlank(activityStatus)) {
			registration.setActive();
		} else if (isValidPv(activityStatus, Status.ACTIVITY_STATUS.getStatus())) {
			registration.setActivityStatus(activityStatus);
		} else {
			ose.addError(ActivityStatusErrorCode.INVALID);
		}

	}

	private void setCollectionProtocol(
			CollectionProtocolRegistration cpr, 
			CollectionProtocolRegistrationDetail detail, 
			OpenSpecimenException ose) {
				
		Long cpId = detail.getCpId();
		String title = detail.getCpTitle();
		
		CollectionProtocol protocol = null;
		if (cpId == null && StringUtils.isNotBlank(title)) {			
			protocol = daoFactory.getCollectionProtocolDao().getCollectionProtocol(title);
		} else if (cpId != null){
			protocol = daoFactory.getCollectionProtocolDao().getById(detail.getCpId());
		} else {
			ose.addError(CprErrorCode.CP_REQUIRED);
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

	private void setPpid(CollectionProtocolRegistration cpr, String ppid, OpenSpecimenException ose) {
		if (cpr.getCollectionProtocol() == null) {
			return;
		}
		
		String ppidFormat = cpr.getCollectionProtocol().getPpidFormat();

		if (StringUtils.isBlank(ppid) && StringUtils.isBlank(ppidFormat)) {
			ose.addError(CprErrorCode.PPID_REQUIRED);
		} else if (StringUtils.isBlank(ppid)) {
			Long value = keyFactory.getValueByKey(
					cpr.getCollectionProtocol().getId().toString(), CollectionProtocol.class.getName());

			PpidGenerator generator = new PpidGeneratorImpl();
			cpr.setProtocolParticipantIdentifier(generator.generatePpid(ppidFormat, value));
		} else {
			cpr.setProtocolParticipantIdentifier(ppid);
		}
	}

	private void setConsents(
			CollectionProtocolRegistration cpr, 
			CollectionProtocolRegistrationDetail detail,
			OpenSpecimenException ose) {
		if (cpr.getCollectionProtocol() == null) {
			return;
		}
		
		Collection<ConsentTier> consents = cpr.getCollectionProtocol().getConsentTier();
		if (consents == null || consents.isEmpty()) {
			return;
		}

		ConsentDetail consentDetail = detail.getConsentDetails();
		if (consentDetail == null) {
			return;
		}
				
		setConsentSignDate(cpr, consentDetail);
		setConsentWitness(cpr, consentDetail, ose);
		setConsentResponses(cpr, consentDetail);
		setConsentDocumentUrl(cpr, consentDetail.getConsentDocumentUrl());
	}
	
	private void setConsentDocumentUrl(CollectionProtocolRegistration cpr, String consentDocumentUrl) {
		cpr.setSignedConsentDocumentURL(consentDocumentUrl);
	}

	private void setConsentSignDate(CollectionProtocolRegistration cpr, ConsentDetail consentDetail) {
		if (consentDetail.getConsentSignatureDate() != null) {
			cpr.setConsentSignatureDate(consentDetail.getConsentSignatureDate());
		}				
	}
	
	private void setConsentWitness(CollectionProtocolRegistration cpr, ConsentDetail consentDetail, OpenSpecimenException ose) {
		String witnessEmailId = consentDetail.getWitnessName();
		if (StringUtils.isBlank(witnessEmailId)) {
			return;
		}
		
		User witness = daoFactory.getUserDao().getUser(witnessEmailId);
		if (witness == null) {
			ose.addError(CprErrorCode.CONSENT_WITNESS_NOT_FOUND);
		}
		
		cpr.setConsentWitness(witness);
	}
	
	private void setConsentResponses(CollectionProtocolRegistration cpr, ConsentDetail consentDetail) {
		Set<ConsentTierResponse> consentResponses = new HashSet<ConsentTierResponse>();
		
		for (ConsentTier consent : cpr.getCollectionProtocol().getConsentTier()) {
			ConsentTierResponse response = new ConsentTierResponse();
			response.setResponse(CONSENT_RESP_NOT_SPECIFIED);
			response.setConsentTier(consent);
			response.setCpr(cpr);
			
			for (ConsentTierResponseDetail userResp : consentDetail.getConsentTierResponses()) {
				if (consent.getStatement().equals(userResp.getConsentStatment())) {
					response.setResponse(userResp.getParticipantResponse());
					break;
				}
			}
			
			consentResponses.add(response);
		}
		
		cpr.setConsentResponseCollection(consentResponses);		
	}

	private void setParticipant(
			CollectionProtocolRegistration cpr,
			ParticipantDetail participantDetail, 
			OpenSpecimenException ose) {
		
		if (participantDetail == null) {
			ose.addError(CprErrorCode.PARTICIPANT_DETAIL_REQUIRED);
			return;
		}
		
		Long participantId = participantDetail.getId();
		Participant participant;
		if (participantId == null) {
			participant = participantFactory.createParticipant(participantDetail);
		} else {
			participant = daoFactory.getParticipantDao().getById(participantId);
		}
		
		if (participant == null) {
			ose.addError(ParticipantErrorCode.NOT_FOUND);
		}
		
		cpr.setParticipant(participant);
	}
}