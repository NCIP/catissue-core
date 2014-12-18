
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Calendar;
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
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.util.PpidGenerator;
import com.krishagni.catissueplus.core.biospecimen.util.impl.PpidGeneratorImpl;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.KeyGenFactory;
import com.krishagni.catissueplus.core.common.util.Status;


public class CollectionProtocolRegistrationFactoryImpl implements CollectionProtocolRegistrationFactory {

	private DaoFactory daoFactory;

	private final String COLLECTION_PROTOCOL = "collection protocol";

	private final String PPID = "participant protocol identifier";

	private final String CONSENT_RESP_NOT_SPECIFIED = "Not Specified";

	private final String CONSENT_WITNESS = "consent witness";
	
	private final String REGISTRATION_DATE = "registration date";
	
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
		ObjectCreationException oce = new ObjectCreationException();
		
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		cpr.setBarcode(detail.getBarcode());
		setRegistrationDate(cpr, detail.getRegistrationDate(), oce);
		setActivityStatus(cpr, detail.getActivityStatus(), oce);
		setCollectionProtocol(cpr, detail, oce);
		setConsents(cpr, detail, oce);
		setPpid(cpr, detail.getPpid(), oce);
		setParticipant(cpr, detail.getParticipant(), oce);
		
		oce.checkErrorAndThrow();
		return cpr;
	}
	
	private void setRegistrationDate(CollectionProtocolRegistration cpr, Date regDate, ObjectCreationException oce) {
		if (regDate == null) {
			oce.addError(ParticipantErrorCode.MISSING_ATTR_VALUE, REGISTRATION_DATE);
			return;
		}
		
		cpr.setRegistrationDate(regDate);
	}

	private void setActivityStatus(CollectionProtocolRegistration registration, String activityStatus,
			ObjectCreationException oce) {
		if (isBlank(activityStatus)) {
			registration.setActive();
		}
		else {
			if (isValidPv(activityStatus, Status.ACTIVITY_STATUS.getStatus())) {
				registration.setActivityStatus(activityStatus);
				return;
			}
			oce.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.getStatus());
		}

	}

	private void setCollectionProtocol(
			CollectionProtocolRegistration cpr, 
			CollectionProtocolRegistrationDetail detail, 
			ObjectCreationException oce) {
				
		Long cpId = detail.getCpId();
		String title = detail.getCpTitle();
		
		CollectionProtocol protocol = null;
		if (cpId == null && StringUtils.isNotBlank(title)) {			
			protocol = daoFactory.getCollectionProtocolDao().getCollectionProtocol(title);
		} else if (cpId != null){
			protocol = daoFactory.getCollectionProtocolDao().getCollectionProtocol(detail.getCpId());
		} else {
			oce.addError(ParticipantErrorCode.MISSING_ATTR_VALUE, COLLECTION_PROTOCOL);
		} 
		
		if (protocol == null) {
			oce.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROTOCOL);
			return;
		}
		
		if (!Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(protocol.getActivityStatus())) {
			oce.addError(ParticipantErrorCode.DISABLED_CP, COLLECTION_PROTOCOL);
			return;
		}
		
		cpr.setCollectionProtocol(protocol);
	}

	private void setPpid(CollectionProtocolRegistration cpr, String ppid, ObjectCreationException oce) {
		if (cpr.getCollectionProtocol() == null) {
			return;
		}
		
		String ppidFormat = cpr.getCollectionProtocol().getPpidFormat();

		if (StringUtils.isBlank(ppid) && StringUtils.isBlank(ppidFormat)) {
			oce.addError(ParticipantErrorCode.MISSING_ATTR_VALUE, PPID);
			return;
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
			ObjectCreationException oce) {
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
		setConsentWitness(cpr, consentDetail, oce);
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
	
	private void setConsentWitness(CollectionProtocolRegistration cpr, ConsentDetail consentDetail, ObjectCreationException oce) {
		String witnessEmailId = consentDetail.getWitnessName();
		if (StringUtils.isBlank(witnessEmailId)) {
			return;
		}
		
		User witness = daoFactory.getUserDao().getUser(witnessEmailId);
		if (witness == null) {
			oce.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, CONSENT_WITNESS);
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
			
			for (ConsentTierDetail userResp : consentDetail.getConsenTierStatements()) {
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
			ObjectCreationException oce) {
		
		if (participantDetail == null) {
			oce.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, "participant");
			return;
		}
		
		Long participantId = participantDetail.getId();
		Participant participant;
		if (participantId == null) {
			participant = participantFactory.createParticipant(participantDetail);
		} else {
			participant = daoFactory.getParticipantDao().getParticipant(participantId);
		}
		
		if (participant == null) {
			oce.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, "participant");
		}
		
		cpr.setParticipant(participant);
	}
}