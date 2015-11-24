
package com.krishagni.catissueplus.core.common.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

public class AbstractDao<T> implements Dao<T> {

	protected SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void saveOrUpdate(T obj) {
		saveOrUpdate(obj, false);
	}
	
	@Override
	public void saveOrUpdate(T obj, boolean flush) {
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		if (flush) {
			flush();
		}
	}

	@Override
	public void delete(T obj) {
		sessionFactory.getCurrentSession().delete(obj);
	}
	
	@Override
	public T getById(Long id) {
		return getById(id, null);
	}
	
	@SuppressWarnings("unchecked")
	public T getById(Long id, String activeCondition) {
		String hql = "from " + getType().getName() + " t0 where t0.id = :id";
		
		if (activeCondition != null) {
			hql += " and " + activeCondition;
		}
		
		List<T> result = sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setLong("id", id)
				.list();
		
		return result.isEmpty() ? null : result.iterator().next();
	}
	
	public Class<?> getType() {
		throw new UnsupportedOperationException("Override the dao method getType() to use getById()");
	}
	
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}
	
	protected void applyIdsFilter(Criteria criteria, String attrName, Set<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return;
		}
		
		/*
		 * All of this because oracle doesn't allow `in` parameter size to be more than 1000
		 * so the parameter item list needs to be chunked out.
		 */
		List<Long> list = new ArrayList<Long>(ids);
		
		Junction or = Restrictions.disjunction();
		if (list.size() > 1000) {
			while (list.size() > 1000) {
				List<?> subList = list.subList(0, 1000);
				or.add(Restrictions.in(attrName, subList));
				list.subList(0, 1000).clear();
			}
		}
		
		if (list.size() > 0) {
			or.add(Restrictions.in(attrName, list));
		}
		
		criteria.add(or);
	}

	protected Session getCurrentSession() {
		Session session = sessionFactory.getCurrentSession();
		session.enableFilter("activeEntity");
		return session;
	}
}
