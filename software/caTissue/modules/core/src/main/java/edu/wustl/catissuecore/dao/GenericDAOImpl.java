/**
 * 
 */
package edu.wustl.catissuecore.dao;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author Denis G. Krylov
 * 
 */
@Transactional
public class GenericDAOImpl extends HibernateDaoSupport implements GenericDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.dao.GenericDAO#save(edu.wustl.common.domain.
	 * AbstractDomainObject)
	 */
	@Override
	public void save(AbstractDomainObject domainObject) {
		getHibernateTemplate().save(domainObject);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wustl.catissuecore.dao.GenericDAO#findByExample(edu.wustl.common.
	 * domain.AbstractDomainObject)
	 */
	@Override
	public <T extends AbstractDomainObject> Collection<T> findByExample(
			final T domainObject) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria criteria = session.createCriteria(domainObject
						.getClass());
				criteria.add(Example.create(domainObject));
				return criteria.list();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wustl.catissuecore.dao.GenericDAO#getCountByExample(edu.wustl.common
	 * .domain.AbstractDomainObject)
	 */
	@Override
	public <T extends AbstractDomainObject> int getCountByExample(
			final T domainObject) {
		return (Integer) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = session.createCriteria(domainObject
								.getClass());
						criteria.add(Example.create(domainObject))
								.setProjection(Projections.rowCount());
						return criteria.list().get(0);
					}
				});
	}



	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.dao.GenericDAO#update(edu.wustl.common.domain.AbstractDomainObject)
	 */
	@Override
	public void update(AbstractDomainObject domainObject) {
		getHibernateTemplate().update(domainObject);

	}

	@Override
	public void initialize(Object domainObject) {
		try {
			getHibernateTemplate().lock(domainObject, LockMode.NONE);
		} catch (DataAccessException e) {
		}
		getHibernateTemplate().initialize(domainObject);
	}

	@Override
	public void refresh(AbstractDomainObject domainObject) {
		getHibernateTemplate().refresh(domainObject);
	}

	@Override
	public void clear() {
		getHibernateTemplate().clear();
	}

	@Override
	public void delete(AbstractDomainObject domainObject) {
		getHibernateTemplate().delete(domainObject);		
	}

	@Override
	public <T extends AbstractDomainObject> T getById(long id, Class<T> cls) {
        T obj = (T) getHibernateTemplate().get(cls, id);
        return obj;
	}
	
    public void synchronizeWithDatabase() {        
        getHibernateTemplate().flush();
    }
    
    @Override
	public void bulkUpdate(String queryString) {
    	getHibernateTemplate().bulkUpdate(queryString);
    }
	

}
