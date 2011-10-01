package edu.wustl.catissuecore.factory;

import java.util.HashSet;
import java.util.LinkedHashSet;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry;
import edu.wustl.catissuecore.util.global.Constants;


public class SpecimenCollectionGroupFactory implements InstanceFactory<SpecimenCollectionGroup>
{

	private static SpecimenCollectionGroupFactory scgFactory;

	private SpecimenCollectionGroupFactory() {
		super();
	}

	public static synchronized SpecimenCollectionGroupFactory getInstance()
	{
		if(scgFactory == null) {
			scgFactory = new SpecimenCollectionGroupFactory();
		}
		return scgFactory;
	}

	public SpecimenCollectionGroup createClone(SpecimenCollectionGroup obj)
	{
		SpecimenCollectionGroup scg=createObject();
		scg.setCollectionProtocolEvent(obj.getCollectionProtocolEvent());
		scg.setActivityStatus(obj.getCollectionProtocolEvent().getActivityStatus());
		scg.setClinicalDiagnosis(obj.getCollectionProtocolEvent().getClinicalDiagnosis());
		scg.setClinicalStatus(obj.getCollectionProtocolEvent().getClinicalStatus());
		scg.setCollectionStatus(Constants.COLLECTION_STATUS_PENDING);
		return scg;
	}

	public SpecimenCollectionGroup createObject()
	{
		SpecimenCollectionGroup scg=new SpecimenCollectionGroup();
		initDefaultValues(scg);
		return scg;
	}

	public void initDefaultValues(SpecimenCollectionGroup scg)
	{
		scg.setScgRecordEntryCollection(new HashSet<SCGRecordEntry>());
		scg.setSpecimenCollection(new LinkedHashSet<Specimen>());
		//scg.setApplyEventsToSpecimens(false);
		//scg.setConsentWithdrawalOption(Constants.WITHDRAW_RESPONSE_NOACTION);
		//scg.setApplyChangesTo(Constants.APPLY_NONE);
		//scg.setStringOfResponseKeys("");
		scg.setSpecimenEventParametersCollection(new HashSet<SpecimenEventParameters>());
		//scg.setIsCPBasedSpecimenEntryChecked(Boolean.TRUE);
		//scg.setIsToInsertAnticipatorySpecimens(Boolean.TRUE);

	}


}
