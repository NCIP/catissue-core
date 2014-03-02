
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
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

	private final String ACTIVITY_STATUS_ACTIVE = "Active";

	private final String CONSENT_RESP_NOT_SPECIFIED = "Not Specified";

	private ParticipantFactory participantFactory;

	private SpecimenCollectionGroupFactory scgFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetails details) {
		CollectionProtocolRegistration registration = new CollectionProtocolRegistration();

		setBarcode(registration, details);
		setRegistrationDate(registration, details);
		setActivityStatus(registration, details);
		setCollectionProtocol(registration, details);
		Participant participant = participantFactory.createParticipant(details.getParticipantDetails());
		registration.setParticipant(participant);
		participant.getCollectionProtocolRegistrationCollection().put(registration.getCollectionProtocol().getTitle(),
				registration);
		setPPId(registration, details);
		setConsents(registration, details);
		setSpecimenCollectionGroups(registration);

		return registration;
	}

	private void setBarcode(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetails details) {
		registration.setBarcode(details.getBarcode());
	}

	private void setRegistrationDate(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetails details) {
		if (details.getRegistrationDate() == null) {
			registration.setRegistrationDate(new Date());
		}
		else {
			registration.setRegistrationDate(details.getRegistrationDate());
		}
	}

	private void setActivityStatus(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetails details) {
		registration.setActivityStatus(ACTIVITY_STATUS_ACTIVE);
	}

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

	private void setPPId(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetails details) {
		if (StringUtils.isBlank(details.getPpid())) {
			reportError(ParticipantErrorCode.MISSING_ATTR_VALUE, PPID);
		}
		//TODO: have to confirm this
		CollectionProtocolRegistration cpr = daoFactory.getCollectionProtocolDao().getCpr(details.getCpId(),
				details.getPpid());
		if (cpr != null) {
			reportError(ParticipantErrorCode.DUPLICATE_PPID, PPID);
		}
		registration.setProtocolParticipantIdentifier(details.getPpid());
	}

	private void setConsents(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetails details) {
		if (details.getConsentResponseDetails() != null) {
			registration.setConsentSignatureDate(details.getConsentResponseDetails().getConsentDate());
			User witness = daoFactory.getUserDao().getUser(details.getConsentResponseDetails().getWitnessName());
			if (witness == null) {
				reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, "consent witness");
			}
			registration.setConsentWitness(witness);
		}
		Collection<ConsentTier> consentTierCollection = daoFactory.getCollectionProtocolDao().getConsentTierCollection(
				details.getCpId());
		Collection<ConsentTierResponse> consentTierResponseCollection = new HashSet<ConsentTierResponse>();
		if (consentTierCollection != null && !consentTierCollection.isEmpty()) {
			Iterator<ConsentTier> iter = consentTierCollection.iterator();
			while (iter.hasNext()) {
				ConsentTier consentTier = (ConsentTier) iter.next();
				ConsentTierResponse consentTierResponse = new ConsentTierResponse();
				consentTierResponse.setResponse(CONSENT_RESP_NOT_SPECIFIED);
				consentTierResponse.setConsentTier(consentTier);
				consentTierResponseCollection.add(consentTierResponse);
				for (ConsentTierDetails tier : details.getConsentResponseDetails()
						.getConsentTierList()) {
					if (consentTier.getStatement().equals(tier.getConsentStatment())) {
						consentTierResponse.setResponse(tier.getParticipantResponse());
					}
				}

			}
		}
		registration.setConsentTierResponseCollection(consentTierResponseCollection);
	}

	private void setSpecimenCollectionGroups(CollectionProtocolRegistration registration) {
		Collection<CollectionProtocolEvent> cpeColl = daoFactory.getCollectionProtocolDao().getEventCollection(
				registration.getCollectionProtocol().getId());
		Iterator<CollectionProtocolEvent> collectionProtocolEventIterator = cpeColl.iterator();
		Collection<SpecimenCollectionGroup> scgCollection = new HashSet<SpecimenCollectionGroup>();
		while (collectionProtocolEventIterator.hasNext()) {
			CollectionProtocolEvent collectionProtocolEvent = collectionProtocolEventIterator.next();
			if (ACTIVITY_STATUS_ACTIVE.toString().equalsIgnoreCase(collectionProtocolEvent.getActivityStatus())) {
				scgCollection.add(scgFactory.createScg(registration, collectionProtocolEvent));
			}
		}
		registration.setSpecimenCollectionGroupCollection(scgCollection);
	}

}
