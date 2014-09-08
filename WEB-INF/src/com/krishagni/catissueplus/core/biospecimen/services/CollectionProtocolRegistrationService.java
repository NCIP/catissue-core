
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateRegistrationEvent;

public interface CollectionProtocolRegistrationService {

	AllSpecimenCollGroupsSummaryEvent getSpecimenCollGroupsList(
			ReqSpecimenCollGroupSummaryEvent reqSpecimenCollGroupSummaryEvent);

	RegistrationCreatedEvent createRegistration(CreateRegistrationEvent event);

	RegistrationDeletedEvent delete(DeleteRegistrationEvent event);

	RegistrationUpdatedEvent updateRegistration(UpdateRegistrationEvent event);

	RegistrationUpdatedEvent patchRegistration(PatchRegistrationEvent event);

}
