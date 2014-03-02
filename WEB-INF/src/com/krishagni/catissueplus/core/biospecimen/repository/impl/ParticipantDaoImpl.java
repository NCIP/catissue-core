
package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;


public class ParticipantDaoImpl extends AbstractDao<Participant> implements ParticipantDao {

	private String ACTIVITY_STATUS_DISABLED = "Disabled";
	@Override
	public Participant getParticipant(Long id) {
		return (Participant)sessionFactory.getCurrentSession().get(Participant.class, id);
	}

	@Override
	public void delete(Long id) {
		String hql = "update "+ Participant.class.getName()+" participant set participant.activityStatus='"+ACTIVITY_STATUS_DISABLED+"' where participant.id=:id";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setLong("id", id);
		query.executeUpdate();
	}

}
