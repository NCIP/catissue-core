package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.common.domain.UnhandledException;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.UnhandledExceptionDao;
import com.krishagni.catissueplus.core.common.repository.UnhandledExceptionListCriteria;

public class UnhandledExceptionDaoImpl extends AbstractDao<UnhandledException> implements UnhandledExceptionDao {
	
	@Override
	public Class<?> getType() {
		return UnhandledException.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<UnhandledException> getUnhandledExceptions(UnhandledExceptionListCriteria listCrit) {
		return getUnhandledExceptionListQuery(listCrit)
			.addOrder(Order.desc("e.timestamp"))
			.setFirstResult(listCrit.startAt())
			.setMaxResults(listCrit.maxResults())
			.list();
	}
	
	private Criteria getUnhandledExceptionListQuery(UnhandledExceptionListCriteria listCrit) {
		Criteria query = getCurrentSession().createCriteria(UnhandledException.class, "e");
		return addSearchCondition(query, listCrit);
	}
	
	private Criteria addSearchCondition(Criteria query, UnhandledExceptionListCriteria listCrit) {
		addInstituteRestriction(query, listCrit);
		addDateRestriction(query, listCrit);
		return query;
	}
	
	private Criteria addInstituteRestriction(Criteria query, UnhandledExceptionListCriteria listCrit) {
		if (listCrit.instituteId() != null) {
			query.createAlias("e.user", "user")
				.createAlias("user.institute", "institute")
				.add(Restrictions.eq("institute.id", listCrit.instituteId()));
		}
		
		return query;
	}
	
	private Criteria addDateRestriction(Criteria query, UnhandledExceptionListCriteria listCrit) {
		if (listCrit.fromDate() != null) {
			query.add(Restrictions.ge("e.timestamp", listCrit.fromDate()));
		}
		
		if (listCrit.toDate() != null) {
			query.add(Restrictions.le("e.timestamp", listCrit.toDate()));
		}
		
		return query;
	}

}
