
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.events.CreateParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.MatchParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDeletedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMatchedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantUpdatedEvent;
import com.krishagni.catissueplus.core.biospecimen.events.PatchParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.SubRegistrationDetailsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqParticipantDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSubRegistrationDetailEvent;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateParticipantEvent;

public interface ParticipantService {

	public ParticipantDetailEvent getParticipant(ReqParticipantDetailEvent event);

	public ParticipantCreatedEvent createParticipant(CreateParticipantEvent event);

	public ParticipantUpdatedEvent updateParticipant(UpdateParticipantEvent event);
	
	public ParticipantDeletedEvent delete(DeleteParticipantEvent event);
//
//	public Object listPedigree(ReqParticipantDetailEvent event);
//
//	public Object updateRelation();
//
//	public Object createRelation();

	public ParticipantUpdatedEvent patchParticipant(PatchParticipantEvent event);

	public ParticipantMatchedEvent getMatchingParticipants(MatchParticipantEvent event);

	public SubRegistrationDetailsEvent getSubRegistrationDetails(ReqSubRegistrationDetailEvent event);
}
