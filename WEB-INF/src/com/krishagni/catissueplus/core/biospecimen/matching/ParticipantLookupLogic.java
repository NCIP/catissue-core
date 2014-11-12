package com.krishagni.catissueplus.core.biospecimen.matching;

import com.krishagni.catissueplus.core.biospecimen.events.MatchParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantMatchedEvent;

public interface ParticipantLookupLogic {
	ParticipantMatchedEvent getMatchingParticipants(MatchParticipantEvent req);
}
