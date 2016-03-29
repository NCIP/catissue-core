package com.krishagni.openspecimen.rde.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.openspecimen.rde.domain.SpecimenCollectionSession;
import com.krishagni.openspecimen.rde.repository.SpecimenCollSessionDao;

public class SpecimenCollSessionDaoImpl extends AbstractDao<SpecimenCollectionSession> implements SpecimenCollSessionDao {
	
	@Override
	public Class<?> getType() {
		return SpecimenCollectionSession.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SpecimenCollectionSession> getSessions(User user) {
		Criteria  criteria = sessionFactory.getCurrentSession()
				.createCriteria(SpecimenCollectionSession.class, "s");
		
		if (user != null) {
			criteria.add(Restrictions.eq("s.user.id", user.getId()));
		}
		
		return criteria.list();
	}
}
