
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CreateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetails;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetailsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

public class ParticipantServiceImpl implements ParticipantService {

	//TODO: Handle privileges
	private DaoFactory daoFactory;

	private ParticipantFactory participantFactory;

	@Override
	public ParticipantDetailsEvent getParticipant(ReqParticipantDetailEvent event) {
		Participant participant = daoFactory.getParticipantDao().getParticipant(event.getParticipantId());
		return ParticipantDetailsEvent.ok(ParticipantDetails.fromDomain(participant));
	}

	@Override
	public ParticipantCreatedEvent createParticipant(CreateParticipantEvent event) {
			try {
				Participant participant = participantFactory.createParticipant(event.getParticipantDetails());
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
				Participant participant = participantFactory.createParticipant(event.getParticipantDto());
				Participant existingParticipant = daoFactory.getParticipantDao().getParticipant(participant.getId());
				existingParticipant.update(participant);
				daoFactory.getParticipantDao().saveOrUpdate(existingParticipant);
				return ParticipantUpdatedEvent.ok(ParticipantDetails.fromDomain(existingParticipant));
			}
			catch (CatissueException ce) {
				return ParticipantUpdatedEvent.invalidRequest(ce.getMessage() + " : " + ce.getErroneousFields());
			}
			catch (Exception e) {
				return ParticipantUpdatedEvent.serverError(e);
			}
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
