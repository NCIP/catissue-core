
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetails;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;

public class CollectionProtocolRegistrationFactoryImpl implements CollectionProtocolRegistrationFactory {

	private DaoFactory daoFactory;

	private final String COLLECTION_PROTOCOL = "collection protocol";

	private final String PPID = "participant protocol identifier";

	private final String CONSENT_RESP_NOT_SPECIFIED = "Not Specified";

	private ParticipantFactory participantFactory;

	private SpecimenCollectionGroupFactory scgFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public ParticipantFactory getParticipantFactory() {
		return participantFactory;
	}

	public void setParticipantFactory(ParticipantFactory participantFactory) {
		this.participantFactory = participantFactory;
	}

	public SpecimenCollectionGroupFactory getScgFactory() {
		return scgFactory;
	}

	public void setScgFactory(SpecimenCollectionGroupFactory scgFactory) {
		this.scgFactory = scgFactory;
	}

	/**
	 * This method validate and populates collection protocol registration with the given details.
	 * @param details
	 * @returns instance of CollectionProtocolRegistration
	 */
	@Override
	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetails details) {
		CollectionProtocolRegistration registration = new CollectionProtocolRegistration();

		setBarcode(registration, details);
		setRegistrationDate(registration, details);
		setActivityStatus(registration, details);
		setCollectionProtocol(registration, details);
		Participant participant = participantFactory.createParticipant(details.getParticipantDetails());
		registration.setParticipant(participant);
		participant.getCprCollection().put(registration.getCollectionProtocol().getTitle(), registration);
		setPPId(registration, details);
		setConsentsDuringRegistration(registration, details);
		createAnticipatedScgs(registration);
		return registration;
	}

	/**
	 * Sets barcode
	 * @param registration
	 * @param details
	 */
	private void setBarcode(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetails details) {
		registration.setBarcode(details.getBarcode());
	}

	/**
	 * Sets the registration date. If null then sets the current date as registration date
	 * @param registration
	 * @param details
	 */
	private void setRegistrationDate(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetails details) {
		if (details.getRegistrationDate() == null) {
			registration.setRegistrationDate(new Date());
		}
		else {
			registration.setRegistrationDate(details.getRegistrationDate());
		}
	}

	/**
	 * Sets the activity status Active
	 * @param registration
	 * @param details
	 */
	private void setActivityStatus(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetails details) {
		registration.setActive();
	}

	/**
	 * Sets the Collection Protocol
	 * @param registration
	 * @param details
	 */
	private void setCollectionProtocol(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetails details) {
		if (details.getCpId() == null) {
			reportError(ParticipantErrorCode.MISSING_ATTR_VALUE, COLLECTION_PROTOCOL);
		}
		CollectionProtocol protocol = daoFactory.getCollectionProtocolDao().getCollectionProtocol(details.getCpId());
		if (protocol == null) {
			reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROTOCOL);
		}
		registration.setCollectionProtocol(protocol);
	}

	/**
	 * Sets the given participant protocol identifier
	 * @param registration
	 * @param details
	 */
	private void setPPId(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetails details) {
		if (StringUtils.isBlank(details.getPpid())) {
			reportError(ParticipantErrorCode.MISSING_ATTR_VALUE, PPID);
		}

		registration.setProtocolParticipantIdentifier(details.getPpid());
	}

	/**
	 * This method converts the consent tier from the CP to consent response.  
	 * @param registration
	 * @param details
	 */
	private void setConsentsDuringRegistration(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetails details) {
		Collection<ConsentTier> consentTierCollection = registration.getCollectionProtocol().getConsentTierCollection();
		//		daoFactory.getCollectionProtocolDao().getConsentTierCollection(
		//				details.getCpId());
		if (consentTierCollection == null || consentTierCollection.isEmpty()) {
			return;
		}

		if (details.getConsentResponseDetails() != null) {
			setConsentSignDate(registration, details.getConsentResponseDetails().getConsentDate());
			String witnessName = details.getConsentResponseDetails().getWitnessName();
			if (StringUtils.isNotBlank(witnessName)) {
				User witness = daoFactory.getUserDao().getUser(witnessName);
				if (witness == null) {
					reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, "consent witness");
				}
				registration.setConsentWitness(witness);
			}
		}

		Collection<ConsentTierResponse> consentTierResponseCollection = new HashSet<ConsentTierResponse>();

		Iterator<ConsentTier> iter = consentTierCollection.iterator();
		while (iter.hasNext()) {
			ConsentTier consentTier = (ConsentTier) iter.next();
			ConsentTierResponse consentTierResponse = new ConsentTierResponse();
			consentTierResponse.setResponse(CONSENT_RESP_NOT_SPECIFIED);
			consentTierResponse.setConsentTier(consentTier);
			consentTierResponseCollection.add(consentTierResponse);
			for (ConsentTierDetails tier : details.getConsentResponseDetails().getConsentTierList()) {
				if (consentTier.getStatement().equals(tier.getConsentStatment())) {
					consentTierResponse.setResponse(tier.getParticipantResponse());
				}

			}
		}
		registration.setConsentTierResponseCollection(consentTierResponseCollection);
	}

	private void setConsentSignDate(CollectionProtocolRegistration registration, Date consentDate) {
		if (consentDate != null) {
			registration.setConsentSignatureDate(consentDate);
		}
	}

	/**
	 * Populate the Specimen Collection Group collection for the collection protocol registration
	 * @param registration instance of CollectionProtocolRegistration
	 */
	private void createAnticipatedScgs(CollectionProtocolRegistration registration) {
		Collection<CollectionProtocolEvent> cpeColl = registration.getCollectionProtocol()
				.getCollectionProtocolEventCollection();
		Iterator<CollectionProtocolEvent> collectionProtocolEventIterator = cpeColl.iterator();
		Collection<SpecimenCollectionGroup> scgCollection = new HashSet<SpecimenCollectionGroup>();
		while (collectionProtocolEventIterator.hasNext()) {
			CollectionProtocolEvent collectionProtocolEvent = collectionProtocolEventIterator.next();
			if (collectionProtocolEvent.isActive()) {
				scgCollection.add(scgFactory.createScg(registration, collectionProtocolEvent));
			}
		}
		registration.setSpecimenCollectionGroupCollection(scgCollection);
	}

}
