
package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ParticipantDao extends Dao<Participant> {

	public Participant getParticipant(Long id);

	public boolean isSsnUnique(String socialSecurityNumber);

	public boolean isPmiUnique(String siteName, String mrn);

	public List<Participant> getMatchingParticipants(DetachedCriteria detachedCriteria);
}
