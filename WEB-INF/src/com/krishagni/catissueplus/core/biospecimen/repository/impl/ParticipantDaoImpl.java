
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import org.hibernate.Query;

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
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PARTICIPANT_ID_BY_SSN);
		query.setString("ssn", socialSecurityNumber);
		return query.list().isEmpty() ? true : false;
	}

	private static final String FQN = Participant.class.getName();

	private static final String GET_PARTICIPANT_ID_BY_SSN = FQN + ".getParticipantIdBySSN";
}
