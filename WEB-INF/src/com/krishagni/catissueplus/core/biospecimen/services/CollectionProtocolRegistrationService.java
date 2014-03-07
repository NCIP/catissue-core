
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;

public interface CollectionProtocolRegistrationService {

	public AllSpecimenCollGroupsSummaryEvent getSpecimenCollGroupsList(
			ReqSpecimenCollGroupSummaryEvent reqSpecimenCollGroupSummaryEvent);

	public RegistrationCreatedEvent createRegistration(CreateRegistrationEvent event);

	public RegistrationDeletedEvent delete(DeleteRegistrationEvent event);
//	public RegistrationUpdatedEvent updateResgistration(UpdateRegistrationEvent event);
//
//	public void delete(DeleteParticipantEvent event);
//
//	public void delete(DeleteRegistrationEvent event);

}
