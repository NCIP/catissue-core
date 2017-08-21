
package com.krishagni.catissueplus.core.common.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

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
		
		if (!(obj instanceof BaseEntity)) {
			return;
		}
		
		BaseEntity entity = (BaseEntity) obj;
		if (CollectionUtils.isEmpty(entity.getOnSaveProcs())) {
			return;
		}

		entity.getOnSaveProcs().forEach(Runnable::run);
	}

	@Override
	public void delete(T obj) {
		sessionFactory.getCurrentSession().delete(obj);
	}
	
	@Override
	public T getById(Long id) {
		return getById(id, null);
	}

	public T getById(Long id, String activeCondition) {
		List<T> result = getByIds(Collections.singleton(id), activeCondition);
		return result.isEmpty() ? null : result.iterator().next();
	}

	public List<T> getByIds(Collection<Long> ids) {
		return getByIds(ids, null);
	}

	@SuppressWarnings("unchecked")
	public List<T> getByIds(Collection<Long> ids, String activeCondition) {
		String hql = "from " + getType().getName() + " t0 where t0.id in (:ids)";

		if (activeCondition != null) {
			hql += " and " + activeCondition;
		}

		return sessionFactory.getCurrentSession().createQuery(hql).setParameterList("ids", ids).list();
	}

	public Class<?> getType() {
		throw new UnsupportedOperationException("Override the dao method getType() to use getById()");
	}
	
	public void flush() {
		sessionFactory.getCurrentSession().flush();
	}
	
	protected void applyIdsFilter(Criteria criteria, String attrName, List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return;
		}
		
		/*
		 * All of this because oracle doesn't allow `in` parameter size to be more than 1000
		 * so the parameter item list needs to be chunked out.
		 */
		Junction or = Restrictions.disjunction();
		if (ids.size() > 1000) {
			while (ids.size() > 1000) {
				List<?> subList = ids.subList(0, 1000);
				or.add(Restrictions.in(attrName, subList));
				ids.subList(0, 1000).clear();
			}
		}
		
		if (ids.size() > 0) {
			or.add(Restrictions.in(attrName, ids));
		}
		
		criteria.add(or);
	}

	protected Session getCurrentSession() {
		Session session = sessionFactory.getCurrentSession();
		session.enableFilter("activeEntity");
		return session;
	}

	protected Map<String, Object> getObjectIds(String propName, String key, Object value) {
		List<Long> rows = getCurrentSession().createCriteria(getType())
			.setProjection(Projections.projectionList().add(Projections.property("id")))
			.add(Restrictions.eq(key, value))
			.list();

		if (CollectionUtils.isEmpty(rows)) {
			return Collections.emptyMap();
		}

		return Collections.singletonMap(propName, rows.iterator().next());
	}
}
