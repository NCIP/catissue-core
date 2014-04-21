package com.krishagni.catissueplus.core.biospecimen.matching;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;


public interface ParticipantLookupLogic {

	List<Participant> getMatchingParticipants(ParticipantDetail participantDetail);
}
