
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;

public interface ParticipantFactory {
	public Participant createParticipant(ParticipantDetail detail);
	
	public Participant createParticipant(Participant existing, ParticipantDetail detail);
}
