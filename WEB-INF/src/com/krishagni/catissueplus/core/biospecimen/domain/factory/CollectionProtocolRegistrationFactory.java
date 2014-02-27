
package com.krishagni.catissueplus.core.biospecimen.domain.factory;



import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetails;


public interface CollectionProtocolRegistrationFactory {

	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetails details);
}
