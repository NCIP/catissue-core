
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ParticipantDaoImpl extends AbstractDao<Participant> implements ParticipantDao {

	@Override
	public Participant getParticipant(Long id) {
		return (Participant) sessionFactory.getCurrentSession().get(Participant.class, id);
	}

	@Override
	public boolean isSsnUnique(String socialSecurityNumber) {
		// TODO Auto-generated method stub
		return true;
	}

}
