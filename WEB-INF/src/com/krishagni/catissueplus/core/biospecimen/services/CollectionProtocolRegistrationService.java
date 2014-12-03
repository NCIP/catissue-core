
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensEvent;
import com.krishagni.catissueplus.core.biospecimen.events.VisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.BulkRegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateBulkRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CreateRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqVisitsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateRegistrationEvent;

public interface CollectionProtocolRegistrationService {
	RegistrationEvent getRegistration(ReqRegistrationEvent req);
	
	RegistrationCreatedEvent createRegistration(CreateRegistrationEvent req);
	
	VisitsEvent getVisits(ReqVisitsEvent req);
	
	VisitSpecimensEvent getSpecimens(ReqVisitSpecimensEvent req);
	
	//
	// TODO: Requires review
	// 
	RegistrationDeletedEvent delete(DeleteRegistrationEvent event);

	RegistrationUpdatedEvent updateRegistration(UpdateRegistrationEvent event);

	RegistrationUpdatedEvent patchRegistration(PatchRegistrationEvent event);
	
	BulkRegistrationCreatedEvent createBulkRegistration(CreateBulkRegistrationEvent req);
}
