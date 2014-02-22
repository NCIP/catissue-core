package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.wustl.catissuecore.domain.Participant;


public class ParticipantSummary extends ResponseEvent{

	public static ParticipantSummary fromDomain(Participant participant)
	{
		ParticipantSummary participantSummary = new ParticipantSummary();
		return participantSummary;
		
	}
}
