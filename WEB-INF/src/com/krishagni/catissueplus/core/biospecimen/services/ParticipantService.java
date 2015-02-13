
package com.krishagni.catissueplus.core.biospecimen.services;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipants;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ParticipantService {
	public ResponseEvent<MatchedParticipants> getMatchingParticipants(RequestEvent<ParticipantDetail> req);
	
	public ResponseEvent<ParticipantDetail> getParticipant(RequestEvent<Long> req);

	public ResponseEvent<ParticipantDetail> createParticipant(RequestEvent<ParticipantDetail> req);

	public ResponseEvent<ParticipantDetail> updateParticipant(RequestEvent<ParticipantDetail> req);
	
	public ResponseEvent<ParticipantDetail>  delete(RequestEvent<Long> req);

	public void createParticipant(Participant participant);
}
