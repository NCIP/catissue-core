package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentResponses;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;

public interface ConsentResponsesFactory {
	public ConsentResponses createConsentResponses(CollectionProtocolRegistration cpr, ConsentDetail detail);
}
