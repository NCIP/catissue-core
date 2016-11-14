package com.krishagni.catissueplus.core.biospecimen.matching;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;

public interface ParticipantLookupLogic {
	List<MatchedParticipant> getMatchingParticipants(ParticipantDetail criteria);
}
