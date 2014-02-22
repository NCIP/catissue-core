package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.Participant;


public interface ParticipantDao extends Dao<Participant>{

	public Participant getParticipant(Long id);
	public List<Participant> getAllParticipants();
}
