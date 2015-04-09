
package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationsList;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface CollectionProtocolRegistrationService {
	ResponseEvent<CollectionProtocolRegistrationDetail> getRegistration(RequestEvent<RegistrationQueryCriteria> req);
	
	ResponseEvent<CollectionProtocolRegistrationDetail> createRegistration(RequestEvent<CollectionProtocolRegistrationDetail> req);
	
	ResponseEvent<List<VisitSummary>> getVisits(RequestEvent<VisitsListCriteria> req);
	
	ResponseEvent<List<SpecimenDetail>> getSpecimens(RequestEvent<VisitSpecimensQueryCriteria> req);
	
	//
	// TODO: Requires review
	// 
	ResponseEvent<CollectionProtocolRegistrationDetail> updateRegistration(RequestEvent<CollectionProtocolRegistrationDetail> req);

	ResponseEvent<ParticipantRegistrationsList> createBulkRegistration(RequestEvent<ParticipantRegistrationsList> req);
}
