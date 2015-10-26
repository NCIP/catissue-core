package com.krishagni.catissueplus.core.audit.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class AuditDaoImpl extends AbstractDao<UserApiCallLog> implements AuditDao {

	@Override
	@SuppressWarnings("unchecked")
	public Date getUserLastApiCallTime(Long userId, String token) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(UserApiCallLog.class, "al")
				.add(Restrictions.conjunction()
					.add(Restrictions.eq("al.user.id", userId))
					.add(Restrictions.eq("al.authToken", token)))
				.setProjection(Projections.max("al.callStartTime"));
		
		List<Date> list = criteria.list();
		return list.isEmpty() ? null : list.get(0);
	}
	
}
