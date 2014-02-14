
package com.krishagni.catissueplus.core.services;

import com.krishagni.catissueplus.core.events.participants.CreateParticipantEvent;
import com.krishagni.catissueplus.core.events.participants.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.events.participants.ParticipantDetailsEvent;
import com.krishagni.catissueplus.core.events.participants.ParticipantUpdatedEvent;
import com.krishagni.catissueplus.core.events.participants.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.events.participants.ReqParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.events.participants.UpdateParticipantEvent;
import com.krishagni.catissueplus.core.events.registration.AllParticipantsSummaryEvent;
import com.krishagni.catissueplus.core.repository.ParticipantDao;

public interface ParticipantService {

	public AllParticipantsSummaryEvent getallParticipants(ReqParticipantsSummaryEvent event);

	public ParticipantDetailsEvent getParticipant(ReqParticipantDetailEvent event);

	public ParticipantCreatedEvent createParticipant(CreateParticipantEvent event);

	public ParticipantUpdatedEvent updateParticipant(UpdateParticipantEvent event);

	public Object listPedigree(ReqParticipantDetailEvent event);

	public Object updateRelation();

	public Object createRelation();
}
