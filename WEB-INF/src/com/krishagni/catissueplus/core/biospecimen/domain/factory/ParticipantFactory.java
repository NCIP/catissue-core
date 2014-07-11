
package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantPatchDetail;

public interface ParticipantFactory {

	public Participant createParticipant(ParticipantDetail details);

	public Participant patchParticipant(Participant oldParticipant, ParticipantPatchDetail participantDetail);

}
