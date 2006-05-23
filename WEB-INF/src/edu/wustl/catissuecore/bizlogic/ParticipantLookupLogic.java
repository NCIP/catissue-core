package edu.wustl.catissuecore.bizlogic;

import java.util.List;

import edu.wustl.catissuecore.domain.Participant;


public interface ParticipantLookupLogic
{
	List participantLookup(Participant participant) throws Exception;
}
