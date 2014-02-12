
package com.krishagni.catissueplus.core.services;

import com.krishagni.catissueplus.core.events.registration.AllConsentsSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.AllParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.AllRegistrationsSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.events.registration.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.events.registration.ReqConsentsSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.ReqRegistrationEvent;
import com.krishagni.catissueplus.core.events.registration.ReqRegistrationSummaryEvent;
import com.krishagni.catissueplus.core.events.registration.UpdateRegistrationEvent;
import com.krishagni.catissueplus.core.repository.CollectionProtocolRegistrationDao;

public interface CollectionProtocolService {

	public AllRegistrationsSummaryEvent getAllRegistrations(ReqRegistrationSummaryEvent event);

	public AllConsentsSummaryEvent getConsents(ReqConsentsSummaryEvent event);

	public RegistrationCreatedEvent createRegistration(ReqRegistrationEvent event);

	public RegistrationUpdatedEvent updateResgistration(UpdateRegistrationEvent event);
	
	public void setRegistrationDao(CollectionProtocolRegistrationDao dao);

}
