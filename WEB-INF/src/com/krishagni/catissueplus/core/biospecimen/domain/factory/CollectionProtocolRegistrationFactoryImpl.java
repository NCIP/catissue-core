
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
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.tokenprocessor.TokenManager;

public class CollectionProtocolRegistrationFactoryImpl implements CollectionProtocolRegistrationFactory {

	private DaoFactory daoFactory;

	private final String COLLECTION_PROTOCOL = "collection protocol";

	private final String PPID = "participant protocol identifier";

	private final String ACTIVITY_STATUS_ACTIVE = "Active";

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
		
		setBasicAttrs(registration, details);
		setCollectionProtocol(registration, details);
		Participant participant = participantFactory.createParticipant(details.getParticipantDetails());
		registration.setParticipant(participant);
		participant.getCollectionProtocolRegistrationCollection().put(registration.getCollectionProtocol().getTitle(), registration);
		setPPId(registration, details);
		setConsents(registration, details);
		setSpecimenCollectionGroups(registration);
		
		return registration;
	}

	private void setBasicAttrs(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetails details) {
		if (StringUtils.isBlank(details.getBarcode())) {
			generateBarcode(registration);
		}
		else {
			registration.setBarcode(details.getBarcode());
		}
		if (details.getRegistrationDate() == null) {
			registration.setRegistrationDate(new Date());
		}
		else {
			registration.setRegistrationDate(details.getRegistrationDate());
		}
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

	//TODO : PPID Format
	private void setPPId(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetails details) {
		if (StringUtils.isBlank(details.getPpid())) {
			generatePPId(registration);
		}
		else {
			registration.setProtocolParticipantIdentifier(details.getPpid());
		}
	}

	private void setConsents(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetails details) {
		Collection<ConsentTier> consentTierCollection = daoFactory.getCollectionProtocolDao().getConsentTierCollection(
				details.getCpId());
		final Collection<ConsentTierResponse> consentTierResponseCollection = new HashSet<ConsentTierResponse>();
		if (consentTierCollection != null && !consentTierCollection.isEmpty()) {
			final Iterator<ConsentTier> iter = consentTierCollection.iterator();
			while (iter.hasNext()) {
				final ConsentTier consentTier = (ConsentTier) iter.next();
				final ConsentTierResponse consentTierResponse = new ConsentTierResponse();
				consentTierResponse.setResponse(Constants.NOT_SPECIFIED);
				consentTierResponse.setConsentTier(consentTier);
				consentTierResponseCollection.add(consentTierResponse);
			}
		}
		registration.setConsentTierResponseCollection(consentTierResponseCollection);
		// TODO Need to handle consents

	}

	private void setSpecimenCollectionGroups(CollectionProtocolRegistration registration) {
		final Iterator<CollectionProtocolEvent> collectionProtocolEventIterator = registration.getCollectionProtocol()
				.getCollectionProtocolEventCollection().iterator();
		final Collection<SpecimenCollectionGroup> scgCollection = new HashSet<SpecimenCollectionGroup>();
		while (collectionProtocolEventIterator.hasNext()) {
			final CollectionProtocolEvent collectionProtocolEvent = collectionProtocolEventIterator.next();
			if (ACTIVITY_STATUS_ACTIVE.toString().equalsIgnoreCase(collectionProtocolEvent.getActivityStatus())) {
				scgCollection.add(scgFactory.createScg(collectionProtocolEvent));
			}
		}
		registration.setSpecimenCollectionGroupCollection(scgCollection);
	}

	private void generateBarcode(CollectionProtocolRegistration registration) {
		// TODO Handle barcode generation

	}

	private void generatePPId(CollectionProtocolRegistration registration) {
		String PPIdformat = registration.getCollectionProtocol().getPpidFormat();
		if (StringUtils.isNotBlank(PPIdformat)) {
			try {
				//TODO: need to handle the TokenManager code, move it to new code
				registration.setProtocolParticipantIdentifier(TokenManager.getLabelValue(registration.getCollectionProtocol(),
						PPIdformat));
			}
			catch (ApplicationException e) {
				reportError(ParticipantErrorCode.CONSTRAINT_VIOLATION, PPID);
			}
		}
	}

}
