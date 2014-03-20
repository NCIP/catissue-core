
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTierResponse;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolRegistrationFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.User;

public class CollectionProtocolRegistrationFactoryImpl implements CollectionProtocolRegistrationFactory {

	private DaoFactory daoFactory;

	private final String COLLECTION_PROTOCOL = "collection protocol";

	private final String PPID = "participant protocol identifier";

	private final String CONSENT_RESP_NOT_SPECIFIED = "Not Specified";

	private List<ErroneousField> erroneousFields;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	/**
	 * This method validate and populates collection protocol registration with the given details.
	 * @param detail
	 * @returns instance of CollectionProtocolRegistration
	 */
	@Override
	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetail detail,
			ObjectCreationException exception) {
		erroneousFields = new ArrayList<ErroneousField>();
		CollectionProtocolRegistration registration = new CollectionProtocolRegistration();

		setBarcode(registration, detail);
		setRegistrationDate(registration, detail);
		setActivityStatus(registration, detail);
		setCollectionProtocol(registration, detail);
		//		registration.setParticipant(participant);
		//		participant.getCprCollection().put(registration.getCollectionProtocol().getTitle(), registration);
		setPPId(registration, detail);
		setConsentsDuringRegistration(registration, detail);
		exception.addError(this.erroneousFields);
		return registration;
	}

	/**
	 * Sets barcode
	 * @param registration
	 * @param detail
	 */
	private void setBarcode(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetail detail) {
		registration.setBarcode(detail.getBarcode());
	}

	/**
	 * Sets the registration date. If null then sets the current date as registration date
	 * @param registration
	 * @param detail
	 */
	private void setRegistrationDate(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetail detail) {
		if (detail.getRegistrationDate() == null) {
			registration.setRegistrationDate(new Date());
		}
		else {
			registration.setRegistrationDate(detail.getRegistrationDate());
		}
	}

	/**
	 * Sets the activity status Active
	 * @param registration
	 * @param detail
	 */
	private void setActivityStatus(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetail detail) {
		if (isBlank(detail.getActivityStatus())) {
			registration.setActive();
		}
		else {
			registration.setActivityStatus(detail.getActivityStatus());
		}

	}

	/**
	 * Sets the Collection Protocol
	 * @param registration
	 * @param detail
	 */
	private void setCollectionProtocol(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetail detail) {
		if (detail.getCpId() == null) {
			addError(ParticipantErrorCode.MISSING_ATTR_VALUE, COLLECTION_PROTOCOL);
		}
		CollectionProtocol protocol = daoFactory.getCollectionProtocolDao().getCollectionProtocol(detail.getCpId());
		if (protocol == null) {
			addError(ParticipantErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROTOCOL);
		}
		registration.setCollectionProtocol(protocol);
	}

	/**
	 * Sets the given participant protocol identifier
	 * @param registration
	 * @param detail
	 */
	private void setPPId(CollectionProtocolRegistration registration, CollectionProtocolRegistrationDetail detail) {
		if (StringUtils.isBlank(detail.getPpid())) {
			addError(ParticipantErrorCode.MISSING_ATTR_VALUE, PPID);
		}

		registration.setProtocolParticipantIdentifier(detail.getPpid());
	}

	/**
	 * This method converts the consent tier from the CP to consent response.  
	 * @param registration
	 * @param detail
	 */
	private void setConsentsDuringRegistration(CollectionProtocolRegistration registration,
			CollectionProtocolRegistrationDetail detail) {
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
					addError(ParticipantErrorCode.INVALID_ATTR_VALUE, "consent witness");
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

	private void setConsentSignDate(CollectionProtocolRegistration registration, Date consentDate) {
		if (consentDate != null) {
			registration.setConsentSignatureDate(consentDate);
		}
	}

	private void addError(CatissueErrorCode event, String field) {
		erroneousFields.add(new ErroneousField(event, field));
	}
}
