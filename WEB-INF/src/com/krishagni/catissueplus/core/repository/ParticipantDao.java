package com.krishagni.catissueplus.core.repository;

import java.util.List;

import edu.wustl.catissuecore.domain.Participant;


public interface ParticipantDao extends Dao<Participant>{

	public Participant getParticipant(Long id);
	public List<Participant> getAllParticipants();
}
