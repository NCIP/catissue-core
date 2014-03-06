
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.errors.CatissueException.reportError;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CreateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetails;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetailsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

public class ParticipantServiceImpl implements ParticipantService {

	//TODO: Handle privileges
	private DaoFactory daoFactory;

	private final String SSN = "social security number";

	/**
	 * Participant factory to create/update and perform all validations on participant details 
	 */
	private ParticipantFactory participantFactory;

	private CollectionProtocolRegistrationService registrationSvc;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public ParticipantFactory getParticipantFactory() {
		return participantFactory;
	}

	public void setParticipantFactory(ParticipantFactory participantFactory) {
		this.participantFactory = participantFactory;
	}

	public CollectionProtocolRegistrationService getRegistrationSvc() {
		return registrationSvc;
	}

	public void setRegistrationSvc(CollectionProtocolRegistrationService registrationSvc) {
		this.registrationSvc = registrationSvc;
	}

	@Override
	public ParticipantDetailsEvent getParticipant(ReqParticipantDetailEvent event) {
		Participant participant = daoFactory.getParticipantDao().getParticipant(event.getParticipantId());
		return ParticipantDetailsEvent.ok(ParticipantDetails.fromDomain(participant));
	}

	@Override
	public ParticipantCreatedEvent createParticipant(CreateParticipantEvent event) {
		try {
			Participant participant = participantFactory.createParticipant(event.getParticipantDetails());
			ensureUniqueSsn(participant.getSocialSecurityNumber());
			daoFactory.getParticipantDao().saveOrUpdate(participant);
			return ParticipantCreatedEvent.ok(ParticipantDetails.fromDomain(participant));
		}
		catch (CatissueException ce) {
			return ParticipantCreatedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
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
	public ParticipantUpdatedEvent updateParticipant(UpdateParticipantEvent event) {
		try {
			Long participantId = event.getParticipantDto().getId();
			Participant oldParticipant = daoFactory.getParticipantDao().getParticipant(participantId);
			if (oldParticipant == null) {
				return ParticipantUpdatedEvent.notFound(participantId);
			}
			Participant participant = participantFactory.createParticipant(event.getParticipantDto());
			validateSsn(oldParticipant.getSocialSecurityNumber(), participant.getSocialSecurityNumber());
			oldParticipant.update(participant);
			daoFactory.getParticipantDao().saveOrUpdate(oldParticipant);
			return ParticipantUpdatedEvent.ok(ParticipantDetails.fromDomain(oldParticipant));
		}
		catch (CatissueException ce) {
			return ParticipantUpdatedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
		}
		catch (Exception e) {
			return ParticipantUpdatedEvent.serverError(e);
		}
	}

	private void validateSsn(String oldSsn, String newSsn) {
		if ((isBlank(oldSsn) && !isBlank(newSsn))) {
			ensureUniqueSsn(newSsn);
		}
		else if (!isBlank(oldSsn) && !isBlank(newSsn) && !oldSsn.equals(newSsn)) {
			ensureUniqueSsn(newSsn);
		}

	}

	private void ensureUniqueSsn(String ssn) {
		if (!daoFactory.getParticipantDao().isSsnUnique(ssn)) {
			reportError(ParticipantErrorCode.DUPLICATE_SSN, SSN);
		}
	}

	@Override
	public ParticipantDeletedEvent delete(DeleteParticipantEvent event) {
//		try {
//			if (event.isIncludeChildren()) {
//				registrationSvc.delete(event);
//			}
//			else if (daoFactory.getParticipantDao().checkActiveChildren(event.getId())) {
//				throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
//			}
//			daoFactory.getParticipantDao().delete(event.getId());
//			return ParticipantDeletedEvent.ok();
//		}
//		catch (CatissueException ce) {
//			return ParticipantDeletedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
//		}
//		catch (Exception e) {
//			return ParticipantDeletedEvent.serverError(e);
//		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.krishagni.catissueplus.core.services.ParticipantService#listPedigree(com.krishagni.catissueplus.core.events.participants.ReqParticipantDetailEvent)
	 */
	@Override
	public Object listPedigree(ReqParticipantDetailEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Updates the given relation
	 */
	@Override
	public Object updateRelation() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * this will create the new relations for the given patients
	 */
	@Override
	public Object createRelation() {
		// TODO Auto-generated method stub
		return null;
	}

}
