
package com.krishagni.catissueplus.core.biospecimen.domain.factory;



import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetails;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;

public interface CollectionProtocolRegistrationFactory {

	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetails details);
}
