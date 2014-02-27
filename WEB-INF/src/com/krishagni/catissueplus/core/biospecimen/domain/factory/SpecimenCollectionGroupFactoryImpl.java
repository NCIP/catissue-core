
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;
import com.krishagni.catissueplus.core.common.PermissibleValuesManager;
import com.krishagni.catissueplus.core.common.PermissibleValuesManagerImpl;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

public class SpecimenCollectionGroupFactoryImpl implements SpecimenCollectionGroupFactory {

	public final String COLLECTION_STATUS_PENDING = "Pending";

	public final String ACTIVITY_STATUS = "Active";
	
	public final String ACTIVITY_STATUS_TYPE = "Activity Status";
	
	public final String CLINICAL_DIAGNOSIS_TYPE ="Clinical Diagnosis";
	
	public final String SCG_COLL_STATUS_TYPE = "scg_collection_status";

	@Override
	public SpecimenCollectionGroup createScg(CollectionProtocolEvent collectionProtocolEvent) {
		SpecimenCollectionGroup group = new SpecimenCollectionGroup();
		group.setCollectionProtocolEvent(collectionProtocolEvent);
		setClinicalDiagnosis(group, collectionProtocolEvent.getClinicalDiagnosis());
		setClinicalStatus(group, collectionProtocolEvent.getClinicalStatus());
		setCollectionStatus(group, COLLECTION_STATUS_PENDING);
		setActivityStatus(group, ACTIVITY_STATUS);
		return group;
	}

	@Override
	public SpecimenCollectionGroup createScg(CreateScgEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setClinicalStatus(SpecimenCollectionGroup group, String clinicalStatus) {
		if (StringUtils.isBlank(clinicalStatus)) {
			reportError(null, null);
		}
		ensureValidPermissibleValue(clinicalStatus, clinicalStatus);
		group.setClinicalStatus(clinicalStatus);

	}

	private void setClinicalDiagnosis(SpecimenCollectionGroup group, String clinicalDiagnosis) {
		if (StringUtils.isBlank(clinicalDiagnosis)) {
			reportError(null, null);
		}
		ensureValidPermissibleValue(clinicalDiagnosis, CLINICAL_DIAGNOSIS_TYPE);
		group.setClinicalDiagnosis(clinicalDiagnosis);
	}

	private void setActivityStatus(SpecimenCollectionGroup group, String activityStatus) {
		if (StringUtils.isBlank(activityStatus)) {
			group.setActivityStatus(ACTIVITY_STATUS);
		}
		ensureValidPermissibleValue(activityStatus, ACTIVITY_STATUS_TYPE);
		group.setActivityStatus(activityStatus);
	}

	private void setCollectionStatus(SpecimenCollectionGroup group, String collectionStatus) {
		if (StringUtils.isBlank(collectionStatus)) {
			group.setCollectionStatus(COLLECTION_STATUS_PENDING);
		}
		ensureValidPermissibleValue(collectionStatus, SCG_COLL_STATUS_TYPE);
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

}
