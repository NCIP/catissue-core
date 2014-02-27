
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.events.CreateScgEvent;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

public interface SpecimenCollectionGroupFactory {

	public SpecimenCollectionGroup createScg(CollectionProtocolEvent collectionProtocolEvent);

	public SpecimenCollectionGroup createScg(CreateScgEvent event);
}
