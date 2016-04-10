package com.krishagni.openspecimen.rde.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.openspecimen.rde.domain.Session;
import com.krishagni.openspecimen.rde.repository.SessionDao;

public class SessionDaoImpl extends AbstractDao<Session> implements SessionDao {
	
	@Override
	public Class<?> getType() {
		return Session.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Session> getSessions(User user) {
		Criteria criteria = getCurrentSession().createCriteria(Session.class, "s");
		
		if (user != null) {
			criteria.createAlias("s.user", "user").add(Restrictions.eq("user.id", user.getId()));
		}
		
		return criteria.list();
	}
}
