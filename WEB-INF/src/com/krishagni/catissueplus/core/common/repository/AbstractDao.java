
package com.krishagni.catissueplus.core.common.repository;

import java.util.List;

import org.hibernate.SessionFactory;

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
			sessionFactory.getCurrentSession().flush();
		}
	}

	@Override
	public void delete(T obj) {
		sessionFactory.getCurrentSession().delete(obj);
	}
	
	@Override
	public T getById(Long id) {
		return getById(id, "activityStatus = 'Active'");
	}
	
	@SuppressWarnings("unchecked")
	public T getById(Long id, String activeCondition) {
		String hql = "from " + getType().getName() + " where identifier = :id";
		
		if (activeCondition != null) {
			hql += " and " + activeCondition;
		}
		
		List<T> result = sessionFactory.getCurrentSession()
				.createQuery(hql)
				.setLong("id", id)
				.list();
		
		return result.isEmpty() ? null : result.iterator().next();
	}
	
	public Class getType() {
		throw new UnsupportedOperationException("Override the dao method getType() to use getById()");
	}
		 
}
