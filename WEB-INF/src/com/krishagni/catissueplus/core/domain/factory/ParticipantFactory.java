package com.krishagni.catissueplus.core.domain.factory;

import com.krishagni.catissueplus.core.events.participants.ParticipantDetails;

import edu.wustl.catissuecore.domain.Participant;


public interface ParticipantFactory {
	
	public Participant createParticipant(ParticipantDetails details);

}
