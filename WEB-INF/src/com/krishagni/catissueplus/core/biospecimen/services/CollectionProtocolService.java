
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllConsentsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.AllRegistrationsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqAllCollectionProtocolsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqConsentsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegistrationSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.CollectionProtocolRegistrationDao;

public interface CollectionProtocolService {

	public AllCollectionProtocolsEvent getAllProtocols(ReqAllCollectionProtocolsEvent req);

	public AllRegistrationsSummaryEvent getAllRegistrations(ReqRegistrationSummaryEvent event);

	public ParticipantsSummaryEvent getRegisteredParticipantList(ReqParticipantsSummaryEvent reqParticipantsSummaryEvent);

	public AllConsentsSummaryEvent getConsents(ReqConsentsSummaryEvent event);

	public RegistrationCreatedEvent createRegistration(CreateRegistrationEvent event);

	public RegistrationUpdatedEvent updateResgistration(UpdateRegistrationEvent event);

	public void setRegistrationDao(CollectionProtocolRegistrationDao dao);

}
