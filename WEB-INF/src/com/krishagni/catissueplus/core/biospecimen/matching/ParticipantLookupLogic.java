package com.krishagni.catissueplus.core.biospecimen.matching;

import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipants;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;

public interface ParticipantLookupLogic {
	MatchedParticipants getMatchingParticipants(ParticipantDetail criteria);
}
