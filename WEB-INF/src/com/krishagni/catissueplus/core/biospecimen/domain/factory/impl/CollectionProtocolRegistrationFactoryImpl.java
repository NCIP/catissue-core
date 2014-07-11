
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTierResponse;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationPatchDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.util.PpidGenerator;
import com.krishagni.catissueplus.core.biospecimen.util.impl.PpidGeneratorImpl;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.KeyGenFactory;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;

public class CollectionProtocolRegistrationFactoryImpl implements CollectionProtocolRegistrationFactory {

	private DaoFactory daoFactory;

	private final String COLLECTION_PROTOCOL = "collection protocol";

	private final String PPID = "participant protocol identifier";

	private final String CONSENT_RESP_NOT_SPECIFIED = "Not Specified";

	private final String CONSENT_WITNESS = "consent witness";

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
		CollectionProtocolRegistration registration = new CollectionProtocolRegistration();
		ObjectCreationException exception = new ObjectCreationException();
		setBarcode(registration, detail.getBarcode(), exception);
		setRegistrationDate(registration, detail.getRegistrationDate(), exception);
		setActivityStatus(registration, detail.getActivityStatus(), exception);
		setCollectionProtocol(registration, detail, exception);
		setPPId(registration, detail.getPpid(), exception);

		Long participantId = detail.getParticipantDetail().getId();
		Participant participant;
		if (participantId == null) {
			participant = participantFactory.createParticipant(detail.getParticipantDetail());
		}
		else {
			participant = daoFactory.getParticipantDao().getParticipant(participantId);
		}
		if (participant == null) {
			exception.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, "participant");
		}
		registration.setParticipant(participant);
		exception.checkErrorAndThrow();
		return registration;
	}

	//	@Override
	//	public CollectionProtocolRegistration patchCpr(CollectionProtocolRegistration oldCpr,
	//			CollectionProtocolRegistrationDetail detail) {
	//		ObjectCreationException exception = new ObjectCreationException();
	//		if (detail.isBarcodeModified()) {
	//			setBarcode(oldCpr, detail.getBarcode(), exception);
	//		}
	//		if (detail.isPpidModified()) {
	//			setPPId(oldCpr, detail.getPpid(), exception);
	//		}
	//		if (detail.isActivityStatusModified()) {
	//			setActivityStatus(oldCpr, detail.getActivityStatus(), exception);
	//		}
	//		if (detail.isRegistrationDateModified()) {
	//			setRegistrationDate(oldCpr, detail.getRegistrationDate(), exception);
	//		}
	//		if (detail.isParticipantModified()) {
	//			setRegistrationDate(oldCpr, detail.getRegistrationDate(), exception);
	//		}
	//		return null;
	//	}

	/**
	 * Sets barcode
	 * @param registration
	 * @param barcode
	 * @param exception 
	 */
	private void setBarcode(CollectionProtocolRegistration registration, String barcode, ObjectCreationException exception) {
		registration.setBarcode(barcode);
	}

	/**
	 * Sets the registration date. If null then sets the current date as registration date
	 * @param registration
	 * @param date
	 * @param exception 
	 */
	private void setRegistrationDate(CollectionProtocolRegistration registration, Date date,
			ObjectCreationException exception) {
		if (date == null) {
			registration.setRegistrationDate(new Date());
		}
		else {
			registration.setRegistrationDate(date);
		}
	}

	/**
	 * Sets the activity status Active
	 * @param registration
	 * @param activityStatus
	 * @param exception 
	 */
	private void setActivityStatus(CollectionProtocolRegistration registration, String activityStatus,
			ObjectCreationException exception) {
		if (isBlank(activityStatus)) {
			registration.setActive();
		}
		else {
			registration.setActivityStatus(activityStatus);
		}

	}

	/**
	 * Sets the Collection Protocol
	 * @param registration
	 * @param detail
	 * @param exception 
	 */
	private void setCollectionProtocol(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetail detail, ObjectCreationException exception) {
		if (detail.getCpId() == null) {
			exception.addError(ParticipantErrorCode.MISSING_ATTR_VALUE, COLLECTION_PROTOCOL);
		}
		CollectionProtocol protocol = daoFactory.getCollectionProtocolDao().getCollectionProtocol(detail.getCpId());
		if (protocol == null) {
			exception.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROTOCOL);
			return;
		}
		registration.setCollectionProtocol(protocol);
		setConsents(registration, detail, exception);
	}

	/**
	 * Sets the given participant protocol identifier
	 * @param registration
	 * @param ppId
	 * @param exception 
	 */
	private void setPPId(CollectionProtocolRegistration registration, String ppId, ObjectCreationException exception) {
	    String ppidFormat = registration.getCollectionProtocol().getPpidFormat();
	   if (StringUtils.isBlank(ppId)){
	        if(StringUtils.isBlank(ppidFormat)){
	            exception.addError(ParticipantErrorCode.MISSING_ATTR_VALUE, PPID);
	            return;
	        }
	        
	        Long value = keyFactory.getValueByKey(registration.getCollectionProtocol().getId().toString(),
	                CollectionProtocol.class.getName());
	        PpidGenerator generator = new PpidGeneratorImpl();
	        registration.setProtocolParticipantIdentifier(generator.generatePpid(ppidFormat, value));
	    }
	    else{
	        registration.setProtocolParticipantIdentifier(ppId);
	    }
		
	    
		

	}

	/**
	 * This method converts the consent tier from the CP to consent response.  
	 * @param registration
	 * @param detail
	 * @param exception 
	 */
	private void setConsents(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetail detail,
			ObjectCreationException exception) {
		List<ConsentTierDetail> userProvidedConsents = new ArrayList<ConsentTierDetail>();
		Collection<ConsentTier> consentTierCollection = registration.getCollectionProtocol().getConsentTierCollection();
		if (consentTierCollection == null || consentTierCollection.isEmpty()) {
			return;
		}

		if (detail.getResponseDetail() != null) {
			userProvidedConsents = detail.getResponseDetail().getConsentTierList();
			setConsentSignDate(registration, detail.getResponseDetail().getConsentDate());
			String witnessName = detail.getResponseDetail().getWitnessName();
			if (StringUtils.isNotBlank(witnessName)) {
				User witness = daoFactory.getUserDao().getUser(witnessName);
				if (witness == null) {
					exception.addError(ParticipantErrorCode.INVALID_ATTR_VALUE, CONSENT_WITNESS);
				}
				registration.setConsentWitness(witness);
			}
		}

		Set<ConsentTierResponse> consentTierResponseCollection = new HashSet<ConsentTierResponse>();

		Iterator<ConsentTier> iter = consentTierCollection.iterator();
		while (iter.hasNext()) {
			ConsentTier consentTier = (ConsentTier) iter.next();
			ConsentTierResponse consentTierResponse = new ConsentTierResponse();
			consentTierResponse.setResponse(CONSENT_RESP_NOT_SPECIFIED);
			consentTierResponse.setConsentTier(consentTier);
			consentTierResponse.setCpr(registration);
			consentTierResponseCollection.add(consentTierResponse);
			for (ConsentTierDetail tier : userProvidedConsents) {
				if (consentTier.getStatement().equals(tier.getConsentStatment())) {
					consentTierResponse.setResponse(tier.getParticipantResponse());
				}

			}
		}
		registration.setConsentResponseCollection(consentTierResponseCollection);
	}

	//	private void setConsentDuringUpdate(CollectionProtocolRegistration registration,
	//			CollectionProtocolRegistrationDetail detail) {
	//
	//		if (detail.getResponseDetail() == null) {
	//			return;
	//		}
	//		List<ConsentTierDetail> userProvidedConsents = detail.getResponseDetail().getConsentTierList();
	//		setConsentSignDate(registration, detail.getResponseDetail().getConsentDate());
	//		String witnessName = detail.getResponseDetail().getWitnessName();
	//		if (StringUtils.isNotBlank(witnessName)) {
	//			User witness = daoFactory.getUserDao().getUser(witnessName);
	//			if (witness == null) {
	//				addError(ParticipantErrorCode.INVALID_ATTR_VALUE, CONSENT_WITNESS);
	//			}
	//			registration.setConsentWitness(witness);
	//		}
	//		Set<ConsentTierResponse> consentTierResponseCollection = new HashSet<ConsentTierResponse>();
	//
	//		for (ConsentTierDetail tier : userProvidedConsents) {
	//			ConsentTier consentTier = new ConsentTier();
	//			consentTier.setStatement(tier.getConsentStatment());
	//			ConsentTierResponse consentTierResponse = new ConsentTierResponse();
	//			consentTierResponse.setCpr(registration);
	//			consentTierResponse.setResponse(tier.getParticipantResponse());
	//			consentTierResponse.setConsentTier(consentTier);
	//			consentTierResponseCollection.add(consentTierResponse);
	//			
	//		}
	//
	//		registration.setConsentResponseCollection(consentTierResponseCollection);
	//	}

	private void setConsentSignDate(CollectionProtocolRegistration registration, Date consentDate) {
		if (consentDate != null) {
			registration.setConsentSignatureDate(consentDate);
		}
	}

	@Override
	public CollectionProtocolRegistration patchCpr(CollectionProtocolRegistration oldCpr,
			CollectionProtocolRegistrationPatchDetail detail) {
		ObjectCreationException exception = new ObjectCreationException();
	if(detail.isActivityStatusModified()){
		setActivityStatus(oldCpr, detail.getActivityStatus(), exception);
	}
	if(detail.isBarcodeModified()){
		setBarcode(oldCpr, detail.getBarcode(), exception);
	}
	if(detail.isPpidModified()){
		setPPId(oldCpr, detail.getPpid(), exception);
	}
	if(detail.isRegistrationDateModified()){
		setRegistrationDate(oldCpr, detail.getRegistrationDate(), exception);
	}
//	if(detail.isParticipantModified()){
//		participantFactory.
//	}
	exception.checkErrorAndThrow();
		return oldCpr;
	}

	//	private void addError(CatissueErrorCode event, String field) {
	//		exception.addError(event, field);
	//	}
}