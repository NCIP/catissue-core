
package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.MatchedParticipant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ParticipantService {
	public ResponseEvent<List<MatchedParticipant>> getMatchingParticipants(RequestEvent<ParticipantDetail> req);
	
	public ResponseEvent<ParticipantDetail> getParticipant(RequestEvent<Long> req);

	public ResponseEvent<ParticipantDetail> createParticipant(RequestEvent<ParticipantDetail> req);

	public ResponseEvent<ParticipantDetail> updateParticipant(RequestEvent<ParticipantDetail> req);
	
	public ResponseEvent<ParticipantDetail> patchParticipant(RequestEvent<ParticipantDetail> req);
	
	public ResponseEvent<ParticipantDetail>  delete(RequestEvent<Long> req);

	//
	// Internal APIs
	//
	public void createParticipant(Participant participant);
	
	public void updateParticipant(Participant existing, Participant newParticipant);
	
	public ParticipantDetail saveOrUpdateParticipant(ParticipantDetail participant);
}
