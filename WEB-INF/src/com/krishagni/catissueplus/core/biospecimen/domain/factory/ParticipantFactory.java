package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetails;



public interface ParticipantFactory {
	
	public Participant createParticipant(ParticipantDetails details);

}
