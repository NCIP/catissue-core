
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.ensureValidPermissibleValue;
import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.Collection;
import java.util.HashSet;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenCollectionGroupFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;

public class SpecimenCollectionGroupFactoryImpl implements SpecimenCollectionGroupFactory {

	private SpecimenFactory specimenFactory;

	private final String CLINICAL_DIAGNOSIS = "clinical diagnosis";

	private final String CLINICAL_STATUS = "clinical status";

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
		setCollectionStatus(group);
		setActivityStatus(group);
		createAnticipatedSpecimens(group, collectionProtocolEvent);
		return group;
	}

	/**
	 * Sets clinical status value
	 * @param group
	 * @param clinicalStatus
	 */
	private void setClinicalStatus(SpecimenCollectionGroup group, String clinicalStatus) {
		if (isBlank(clinicalStatus)) {
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
		if (isBlank(clinicalDiagnosis)) {
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
	private void setActivityStatus(SpecimenCollectionGroup group) {
		group.setActive();
	}

	/**
	 * Sets the collection status
	 * @param group SpecimenCollectionGroup
	 * @param collectionStatus collection status
	 */
	private void setCollectionStatus(SpecimenCollectionGroup group) {
		group.setCollectionStatusPending();
	}

	/**
	 * This will create anticipated specimens
	 * @param group
	 * @param CollectionProtocolEvent
	 */
	private void createAnticipatedSpecimens(SpecimenCollectionGroup group, CollectionProtocolEvent CollectionProtocolEvent) {
		final Collection<Specimen> specimenCollection = new HashSet<Specimen>();
		Collection<SpecimenRequirement> specimenRequirements = CollectionProtocolEvent.getSpecimenRequirementCollection();
		for (SpecimenRequirement specimenRequirement : specimenRequirements) {
			//TODO: yet to handle the parent child relationship in specimen factory
			Specimen specimen = specimenFactory.createSpecimen(specimenRequirement, group);
			specimenCollection.add(specimen);
		}
	}

}
