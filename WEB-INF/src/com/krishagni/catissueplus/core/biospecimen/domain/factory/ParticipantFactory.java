package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetails;

import edu.wustl.catissuecore.domain.Participant;


public interface ParticipantFactory {
	
	public Participant createParticipant(ParticipantDetails details);

}
