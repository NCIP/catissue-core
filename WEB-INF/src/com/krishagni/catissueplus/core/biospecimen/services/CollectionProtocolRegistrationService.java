
package com.krishagni.catissueplus.core.biospecimen.services;

import java.io.File;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpEntityDeleteCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationsList;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.VisitsListCriteria;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface CollectionProtocolRegistrationService {
	public ResponseEvent<CollectionProtocolRegistrationDetail> getRegistration(RequestEvent<RegistrationQueryCriteria> req);
	
	public ResponseEvent<CollectionProtocolRegistrationDetail> createRegistration(RequestEvent<CollectionProtocolRegistrationDetail> req);
	
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<RegistrationQueryCriteria> req);
	
	public ResponseEvent<CollectionProtocolRegistrationDetail> deleteRegistration(RequestEvent<CpEntityDeleteCriteria> req);
	
	public ResponseEvent<List<VisitSummary>> getVisits(RequestEvent<VisitsListCriteria> req);
	
	public ResponseEvent<List<SpecimenDetail>> getSpecimens(RequestEvent<VisitSpecimensQueryCriteria> req);
	
	public ResponseEvent<ParticipantRegistrationsList> createRegistrations(RequestEvent<ParticipantRegistrationsList> req);
	
	public ResponseEvent<ParticipantRegistrationsList> updateRegistrations(RequestEvent<ParticipantRegistrationsList> req);
	
	//
	// TODO: Requires review
	// 
	public ResponseEvent<CollectionProtocolRegistrationDetail> updateRegistration(RequestEvent<CollectionProtocolRegistrationDetail> req);
	
	public ResponseEvent<File> getConsentForm(RequestEvent<RegistrationQueryCriteria> req);
	
	public ResponseEvent<String> uploadConsentForm(RequestEvent<FileDetail> req);

	public ResponseEvent<Boolean> deleteConsentForm(RequestEvent<RegistrationQueryCriteria> req);

	public ResponseEvent<ConsentDetail> saveConsents(RequestEvent<ConsentDetail> req);

	public ResponseEvent<ConsentDetail> getConsents(RequestEvent<RegistrationQueryCriteria> req);
}

