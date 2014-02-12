
package com.krishagni.catissueplus.core.services.impl;

import com.krishagni.catissueplus.core.events.registration.AllConsentsSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.AllRegistrationsSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.events.registration.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.events.registration.ReqConsentsSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.ReqRegistrationEvent;
import com.krishagni.catissueplus.core.events.registration.ReqRegistrationSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.UpdateRegistrationEvent;
import com.krishagni.catissueplus.core.repository.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.core.services.CollectionProtocolService;

public class CollectionProtocolServiceImpl implements CollectionProtocolService {

	@Override
	public AllRegistrationsSummaryEvent getAllRegistrations(ReqRegistrationSummaryEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AllConsentsSummaryEvent getConsents(ReqConsentsSummaryEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegistrationCreatedEvent createRegistration(ReqRegistrationEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegistrationUpdatedEvent updateResgistration(UpdateRegistrationEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRegistrationDao(CollectionProtocolRegistrationDao dao) {
		// TODO Auto-generated method stub
		
	}


}
