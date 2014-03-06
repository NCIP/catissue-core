
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;

public interface SpecimenCollectionGroupFactory {

	public SpecimenCollectionGroup createScg(CollectionProtocolRegistration registration,
			CollectionProtocolEvent collectionProtocolEvent);

}
