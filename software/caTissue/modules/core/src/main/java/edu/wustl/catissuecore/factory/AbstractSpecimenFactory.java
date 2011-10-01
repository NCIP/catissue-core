
package edu.wustl.catissuecore.factory;

import java.util.HashSet;
import java.util.LinkedHashSet;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry;
import edu.wustl.catissuecore.util.global.Constants;

public abstract class AbstractSpecimenFactory<S extends Specimen> implements InstanceFactory<S>
{

	abstract public S createClone(S t);

	abstract public S createObject();

	public void initDefaultValues(S obj)
	{
		obj.setSpecimenEventCollection(new HashSet<SpecimenEventParameters>());
		obj.setChildSpecimenCollection(new LinkedHashSet<AbstractSpecimen>());
		obj.setBiohazardCollection(new HashSet<Biohazard>());
		obj.setExternalIdentifierCollection(new HashSet<ExternalIdentifier>());
		//obj.setConsentWithdrawalOption(Constants.WITHDRAW_RESPONSE_NOACTION);
		//obj.setApplyChangesTo(Constants.APPLY_NONE);
		obj.setSpecimenRecordEntryCollection(new HashSet<SpecimenRecordEntry>());
		obj.setIsAvailable(Boolean.FALSE);
	}

}
