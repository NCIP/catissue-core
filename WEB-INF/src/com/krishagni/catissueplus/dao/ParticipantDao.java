
package com.krishagni.catissueplus.dao;

import java.util.List;

import com.krishagni.catissueplus.events.participants.ParticipantInfo;

import edu.wustl.catissuecore.domain.Participant;

public interface ParticipantDao extends Dao<Participant> {

	public List<ParticipantInfo> getParticipantsInfoList(Long cpId, String query);

	public List<ParticipantInfo> getParticipantsInfoList(Long cpId);

	public Participant getParticipant(Long id);

}
