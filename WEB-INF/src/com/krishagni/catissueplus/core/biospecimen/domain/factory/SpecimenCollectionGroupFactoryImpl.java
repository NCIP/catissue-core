
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;

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

	@Override
	public SpecimenCollectionGroup createScg(CollectionProtocolRegistration cpr,
			CollectionProtocolEvent collectionProtocolEvent) {
		SpecimenCollectionGroup group = new SpecimenCollectionGroup();
		group.setCollectionProtocolEvent(collectionProtocolEvent);
		setClinicalDiagnosis(group, collectionProtocolEvent.getClinicalDiagnosis());
		setClinicalStatus(group, collectionProtocolEvent.getClinicalStatus());
		setCollectionStatus(group, COLLECTION_STATUS_PENDING);
		setActivityStatus(group, ACTIVITY_STATUS);
		setConsents(group, cpr);
		setSpecimenCollection(group, collectionProtocolEvent);
		return group;
	}

	private void setConsents(SpecimenCollectionGroup group, CollectionProtocolRegistration cpr) {
		Collection<ConsentTierStatus> consentTierStatusCollectionN = new HashSet<ConsentTierStatus>();
		final Collection<ConsentTierResponse> consentTierResponseCollection = cpr.getConsentTierResponseCollection();

		if (consentTierResponseCollection != null && !consentTierResponseCollection.isEmpty()) {
			final Iterator<ConsentTierResponse> iterator = consentTierResponseCollection.iterator();
			while (iterator.hasNext()) {
				final ConsentTierResponse consentTierResponse = (ConsentTierResponse) iterator.next();
				ConsentTierStatus consentTierstatusN = new ConsentTierStatus();

				final ConsentTier consentTier = new ConsentTier(consentTierResponse.getConsentTier());
				consentTierstatusN.setConsentTier(consentTier);
				consentTierstatusN.setStatus(consentTierResponse.getResponse());
				consentTierStatusCollectionN.add(consentTierstatusN);

			}
		}
		group.setConsentTierStatusCollection(consentTierStatusCollectionN);
	}

	private void setClinicalStatus(SpecimenCollectionGroup group, String clinicalStatus) {
		if (StringUtils.isBlank(clinicalStatus)) {
			reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, CLINICAL_STATUS);
		}
		ensureValidPermissibleValue(clinicalStatus, CLINICAL_STATUS);
		group.setClinicalStatus(clinicalStatus);

	}

	private void setClinicalDiagnosis(SpecimenCollectionGroup group, String clinicalDiagnosis) {
		if (StringUtils.isBlank(clinicalDiagnosis)) {
			reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, CLINICAL_DIAGNOSIS);
		}
		ensureValidPermissibleValue(clinicalDiagnosis, CLINICAL_DIAGNOSIS);
		group.setClinicalDiagnosis(clinicalDiagnosis);
	}

	private void setActivityStatus(SpecimenCollectionGroup group, String activityStatus) {
		if (StringUtils.isBlank(activityStatus)) {
			group.setActivityStatus(ACTIVITY_STATUS);
		}
		ensureValidPermissibleValue(activityStatus, ACTIVITY_STATUS_FIELD);
		group.setActivityStatus(activityStatus);
	}

	private void setCollectionStatus(SpecimenCollectionGroup group, String collectionStatus) {
		if (StringUtils.isBlank(collectionStatus)) {
			group.setCollectionStatus(COLLECTION_STATUS_PENDING);
		}
		ensureValidPermissibleValue(collectionStatus, SCG_COLL_STATUS);
		group.setCollectionStatus(collectionStatus);
	}

	private void ensureValidPermissibleValue(String value, String type) {
		PermissibleValuesManager pvManager = new PermissibleValuesManagerImpl();
		List<String> pvList = pvManager.getPermissibleValueList(type);
		if (pvList.contains(value)) {
			return;
		}
		reportError(ParticipantErrorCode.INVALID_ATTR_VALUE, type);
	}

	private void setSpecimenCollection(SpecimenCollectionGroup group, CollectionProtocolEvent cpe) {
		final Collection<Specimen> specimenCollection = new HashSet<Specimen>();
		Collection<SpecimenRequirement> specimenRequirements = daoFactory.getCollectionProtocolDao()
				.getSpecimenRequirements(cpe.getId());
		for (SpecimenRequirement specimenRequirement : specimenRequirements) {
			Specimen specimen = specimenFactory.createSpecimen(specimenRequirement, group);
			specimenCollection.add(specimen);
		}
	}

}
