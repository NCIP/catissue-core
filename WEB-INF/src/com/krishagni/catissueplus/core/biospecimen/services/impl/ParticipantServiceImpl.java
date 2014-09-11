
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.ParticipantMedicalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CreateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.MatchParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMatchedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SubRegistrationDetailsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.RegistrationInfo;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSubRegistrationDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.matching.ParticipantLookupLogic;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class ParticipantServiceImpl implements ParticipantService {

	//TODO: Handle privileges
	private DaoFactory daoFactory;

	private final String SSN = "social security number";

	private static final String PMI = "participant medical identifier";

	/**
	 * Participant factory to create/update and perform all validations on participant details 
	 */
	private ParticipantFactory participantFactory;
	
	private ParticipantLookupLogic participantLookupLogic;

	
	public void setParticipantLookupLogic(ParticipantLookupLogic participantLookupLogic) {
		this.participantLookupLogic = participantLookupLogic;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setParticipantFactory(ParticipantFactory participantFactory) {
		this.participantFactory = participantFactory;
	}

	@Override
	@PlusTransactional
	public ParticipantDetailEvent getParticipant(ReqParticipantDetailEvent event) {
		Participant participant = daoFactory.getParticipantDao().getParticipant(event.getParticipantId());
		return ParticipantDetailEvent.ok(ParticipantDetail.fromDomain(participant));
	}

	@Override
//	@Audit(operation=Operation.INSERT,object=ParticipantServiceImpl.class)
	@PlusTransactional
	public ParticipantCreatedEvent createParticipant(CreateParticipantEvent event) {
		try {
//			List<Participant> list = participantLookupLogic.getMatchingParticipants(event.getParticipantDetail());
			Participant participant = participantFactory.createParticipant(event.getParticipantDetail());
			ObjectCreationException errorHandler = new ObjectCreationException();

			ensureUniqueSsn(participant.getSocialSecurityNumber(), errorHandler);
			ensureUniquePMI(participant.getPmiCollection(), errorHandler);
			errorHandler.checkErrorAndThrow();
			daoFactory.getParticipantDao().saveOrUpdate(participant);
			return ParticipantCreatedEvent.ok(ParticipantDetail.fromDomain(participant));
		}
		catch (ObjectCreationException ce) {
			return ParticipantCreatedEvent.invalidRequest(ParticipantErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return ParticipantCreatedEvent.serverError(e);
		}
	}

	/* 
	 * This will update the participant details.
	 * @see com.krishagni.catissueplus.core.services.ParticipantService#updateParticipant(com.krishagni.catissueplus.core.events.participants.UpdateParticipantEvent)
	 */
	@Override
//	@Audit(operation=Operation.INSERT,object=ParticipantServiceImpl.class)
	@PlusTransactional
	public ParticipantUpdatedEvent updateParticipant(UpdateParticipantEvent event) {
		try {
			Long participantId = event.getParticipantId();
			Participant oldParticipant = daoFactory.getParticipantDao().getParticipant(participantId);
			if (oldParticipant == null) {
				return ParticipantUpdatedEvent.notFound(participantId);
			}
			Participant participant = participantFactory.createParticipant(event.getParticipantDetail());
			ObjectCreationException errorHandler = new ObjectCreationException();
			validateSsn(oldParticipant.getSocialSecurityNumber(), participant.getSocialSecurityNumber(), errorHandler);
//			ensureUniquePMI(participant.getPmiCollection(), errorHandler);
			checkForUniquePMI(oldParticipant.getPmiCollection(), participant.getPmiCollection(), errorHandler);
			errorHandler.checkErrorAndThrow();

			oldParticipant.update(participant);
			daoFactory.getParticipantDao().saveOrUpdate(oldParticipant);
			return ParticipantUpdatedEvent.ok(ParticipantDetail.fromDomain(oldParticipant));
		}
		catch (ObjectCreationException ce) {
			return ParticipantUpdatedEvent.invalidRequest(ParticipantErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return ParticipantUpdatedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ParticipantUpdatedEvent patchParticipant(PatchParticipantEvent event) {
		try {
			Long participantId = event.getId();
			Participant oldParticipant = daoFactory.getParticipantDao().getParticipant(participantId);
			if (oldParticipant == null) {
				return ParticipantUpdatedEvent.notFound(participantId);
			}
			Map<String, ParticipantMedicalIdentifier> oldPmiCollection = oldParticipant.getPmiCollection();
			Participant participant = participantFactory.patchParticipant(oldParticipant, event.getParticipantDetail());
			
			ObjectCreationException errorHandler = new ObjectCreationException();;
			validateSsn(oldParticipant.getSocialSecurityNumber(), participant.getSocialSecurityNumber(), errorHandler);
			checkForUniquePMI(oldPmiCollection, participant.getPmiCollection(), errorHandler);
//			ensureUniquePMI(participant.getPmiCollection(), errorHandler);
			errorHandler.checkErrorAndThrow();
			
			oldParticipant.update(participant);
			daoFactory.getParticipantDao().saveOrUpdate(oldParticipant);
			return ParticipantUpdatedEvent.ok(ParticipantDetail.fromDomain(oldParticipant));
		}
		catch (ObjectCreationException ce) {
			return ParticipantUpdatedEvent.invalidRequest(ParticipantErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return ParticipantUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ParticipantDeletedEvent delete(DeleteParticipantEvent event) {
		try {
			Participant participant = daoFactory.getParticipantDao().getParticipant(event.getId());
			if (participant == null) {
				return ParticipantDeletedEvent.notFound(event.getId());
			}
			participant.delete(event.isIncludeChildren());
			daoFactory.getParticipantDao().saveOrUpdate(participant);
			return ParticipantDeletedEvent.ok();
		}
		catch (CatissueException ce) {
			return ParticipantDeletedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
		}
		catch (Exception e) {
			return ParticipantDeletedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ParticipantMatchedEvent getMatchingParticipants(MatchParticipantEvent event) {
		List<Participant> list = participantLookupLogic.getMatchingParticipants(event.getParticipantDetail()); 
//				daoFactory.getParticipantDao().getMatchingParticipants(event.getParticipantDetail());
		List<ParticipantDetail> details = new ArrayList<ParticipantDetail>();
		for (Participant participant : list) {
			details.add(ParticipantDetail.fromDomain(participant));
		}
		return ParticipantMatchedEvent.ok(details);
	}
	
	@Override
	@PlusTransactional
	public SubRegistrationDetailsEvent getSubRegistrationDetails(ReqSubRegistrationDetailEvent event) {
		List<RegistrationInfo> registrationsInfo = new ArrayList<RegistrationInfo>();
		
		List<CollectionProtocolRegistration> registrations = new ArrayList<CollectionProtocolRegistration>();
		registrations = daoFactory.getCprDao().getSubRegDetailForParticipantAndCp(event.getParticipantId(),event.getCpId());
		for (CollectionProtocolRegistration cpr : registrations) {
			registrationsInfo.add(RegistrationInfo.fromDomain(cpr));
		}
		return SubRegistrationDetailsEvent.ok(registrationsInfo);
	}
	
	private void validateSsn(String oldSsn, String newSsn, ObjectCreationException errorHandler) {
		if ((isBlank(oldSsn) && !isBlank(newSsn))) {
			ensureUniqueSsn(newSsn, errorHandler);
		}
		else if (!isBlank(oldSsn) && !isBlank(newSsn) && !oldSsn.equals(newSsn)) {
			ensureUniqueSsn(newSsn, errorHandler);
		}
	}

	private void ensureUniqueSsn(String ssn, ObjectCreationException errorHandler) {
		if (!daoFactory.getParticipantDao().isSsnUnique(ssn)) {
			errorHandler.addError(ParticipantErrorCode.DUPLICATE_SSN, SSN);
		}
	}

	private void ensureUniquePMI(Map<String, ParticipantMedicalIdentifier> pmiCollection,
			ObjectCreationException errorHandler) {
		//TODO: need to handle for update
		for (Entry<String, ParticipantMedicalIdentifier> entry : pmiCollection.entrySet()) {
			String siteName = entry.getKey();
			String mrn = entry.getValue().getMedicalRecordNumber();
			if (!daoFactory.getParticipantDao().isPmiUnique(siteName, mrn)) {
				errorHandler.addError(ParticipantErrorCode.DUPLICATE_PMI, PMI);
			}

		}
	}
	
	private void checkForUniquePMI(Map<String, ParticipantMedicalIdentifier> oldPmiCollection,
					Map<String, ParticipantMedicalIdentifier> newPmiCollection, ObjectCreationException errorHandler) {
		Map<String,ParticipantMedicalIdentifier> map = new HashMap<String, ParticipantMedicalIdentifier>();
					for (Entry<String, ParticipantMedicalIdentifier> entry : newPmiCollection.entrySet()) {
						{
							if(!oldPmiCollection.containsKey(entry.getKey())){
										map.put(entry.getKey(), entry.getValue());
							}
							else{
								ParticipantMedicalIdentifier pmi = oldPmiCollection.get(entry.getKey());
								pmi.setMedicalRecordNumber(entry.getValue().getMedicalRecordNumber());
								entry.setValue(pmi);
//								entry.getValue().setId(oldPmiCollection.get(entry.getKey()).getId());
							}
						}
					}
					if (!map.isEmpty()) {
						ensureUniquePMI(map, errorHandler);
				}
			}

}
