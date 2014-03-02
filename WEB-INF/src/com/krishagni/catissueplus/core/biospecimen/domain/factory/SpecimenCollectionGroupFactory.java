
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

public interface SpecimenCollectionGroupFactory {

	public SpecimenCollectionGroup createScg(CollectionProtocolRegistration registration,
			CollectionProtocolEvent collectionProtocolEvent);

}
