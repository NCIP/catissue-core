package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;



public interface ParticipantFactory {
	
	public Participant createParticipant(ParticipantDetail details, ObjectCreationException exception);

}
