
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import static com.krishagni.catissueplus.core.common.CommonValidator.ensureValidPermissibleValue;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;

public class SpecimenCollectionGroupFactoryImpl implements SpecimenCollectionGroupFactory {

	private DaoFactory daoFactory;

	private SpecimenFactory specimenFactory;

	public final String COLLECTION_STATUS_PENDING = "Pending";

	public final String ACTIVITY_STATUS = "Active";

	public final String ACTIVITY_STATUS_FIELD = "activity status";

	public final String CLINICAL_DIAGNOSIS = "clinical diagnosis";

	public final String SCG_COLL_STATUS = "scg_collection_status";

	private final String CLINICAL_STATUS = "clinical status";

	/**
	 * @returns daoFactory
	 */
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	/**
	 * Sets the daoFactory
	 * @param daoFactory
	 */
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	/**
	 * @returns specimenFactory
	 */
	public SpecimenFactory getSpecimenFactory() {
		return specimenFactory;
	}

	/**
	 * Sets specimenFactory
	 * @param specimenFactory
	 */
	public void setSpecimenFactory(SpecimenFactory specimenFactory) {
		this.specimenFactory = specimenFactory;
	}

	/**
	 * This method will creates the SCG from the corresponding CPR.
	 * @param registration instance of CollectionProtocolRegistration
	 * @param collectionProtocolEvent instance of CollectionProtocolEvent
	 */
	@Override
	public SpecimenCollectionGroup createScg(CollectionProtocolRegistration registration,
			CollectionProtocolEvent collectionProtocolEvent) {
		SpecimenCollectionGroup group = new SpecimenCollectionGroup();
		group.setCollectionProtocolEvent(collectionProtocolEvent);
		setClinicalDiagnosis(group, collectionProtocolEvent.getClinicalDiagnosis());
		setClinicalStatus(group, collectionProtocolEvent.getClinicalStatus());
		setCollectionStatus(group, COLLECTION_STATUS_PENDING);
		setActivityStatus(group, ACTIVITY_STATUS);
		setConsents(group, registration);
		createAnticipatedSpecimens(group, collectionProtocolEvent);
		return group;
	}

	/**
	 * Sets consents for SCG
	 * @param group
	 * @param registration
	 */
	private void setConsents(SpecimenCollectionGroup group, CollectionProtocolRegistration registration) {
		Collection<ConsentTierResponse> consentTierResponseCollection = registration.getConsentTierResponseCollection();
		if (consentTierResponseCollection == null || consentTierResponseCollection.isEmpty()) {
			return;
		}
		Collection<ConsentTierStatus> consentTierStatusCollectionN = new HashSet<ConsentTierStatus>();
		final Iterator<ConsentTierResponse> iterator = consentTierResponseCollection.iterator();
		while (iterator.hasNext()) {
			final ConsentTierResponse consentTierResponse = (ConsentTierResponse) iterator.next();
			ConsentTierStatus consentTierstatusN = new ConsentTierStatus();

			final ConsentTier consentTier = new ConsentTier(consentTierResponse.getConsentTier());
			consentTierstatusN.setConsentTier(consentTier);
			consentTierstatusN.setStatus(consentTierResponse.getResponse());
			consentTierStatusCollectionN.add(consentTierstatusN);

		}
		group.setConsentTierStatusCollection(consentTierStatusCollectionN);
	}

	/**
	 * Sets clinical status value
	 * @param group
	 * @param clinicalStatus
	 */
	private void setClinicalStatus(SpecimenCollectionGroup group, String clinicalStatus) {
		if (StringUtils.isBlank(clinicalStatus)) {
			reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, CLINICAL_STATUS);
		}
		ensureValidPermissibleValue(clinicalStatus, CLINICAL_STATUS);
		group.setClinicalStatus(clinicalStatus);

	}

	/**
	 * Sets clinical diagnosis value
	 * @param group
	 * @param clinicalDiagnosis
	 */
	private void setClinicalDiagnosis(SpecimenCollectionGroup group, String clinicalDiagnosis) {
		if (StringUtils.isBlank(clinicalDiagnosis)) {
			reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, CLINICAL_DIAGNOSIS);
		}
		ensureValidPermissibleValue(clinicalDiagnosis, CLINICAL_DIAGNOSIS);
		group.setClinicalDiagnosis(clinicalDiagnosis);
	}

	/**
	 * Sets activity status value
	 * @param group
	 * @param activityStatus
	 */
	private void setActivityStatus(SpecimenCollectionGroup group, String activityStatus) {
		if (StringUtils.isBlank(activityStatus)) {
			group.setActivityStatus(ACTIVITY_STATUS);
		}
		ensureValidPermissibleValue(activityStatus, ACTIVITY_STATUS_FIELD);
		group.setActivityStatus(activityStatus);
	}

	/**
	 * Sets the collection status
	 * @param group SpecimenCollectionGroup
	 * @param collectionStatus collection status
	 */
	private void setCollectionStatus(SpecimenCollectionGroup group, String collectionStatus) {
		if (StringUtils.isBlank(collectionStatus)) {
			group.setCollectionStatus(COLLECTION_STATUS_PENDING);
		}
		ensureValidPermissibleValue(collectionStatus, SCG_COLL_STATUS);
		group.setCollectionStatus(collectionStatus);
	}

	/**
	 * This will create anticipated specimens
	 * @param group
	 * @param CollectionProtocolEvent
	 */
	private void createAnticipatedSpecimens(SpecimenCollectionGroup group, CollectionProtocolEvent CollectionProtocolEvent) {
		final Collection<Specimen> specimenCollection = new HashSet<Specimen>();
		Collection<SpecimenRequirement> specimenRequirements = daoFactory.getCollectionProtocolDao()
				.getSpecimenRequirements(CollectionProtocolEvent.getId());
		for (SpecimenRequirement specimenRequirement : specimenRequirements) {
			Specimen specimen = specimenFactory.createSpecimen(specimenRequirement, group);
			specimenCollection.add(specimen);
		}
	}

}
