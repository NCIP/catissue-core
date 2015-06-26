
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;

public interface CollectionProtocolRegistrationFactory {
	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistrationDetail details);
	
	public CollectionProtocolRegistration createCpr(CollectionProtocolRegistration existing, ConsentDetail consentDetails);
}
