/**
 * 
 */
package edu.wustl.catissue.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.wustl.catissuecore.domain.ccts.Notification;
import gov.nih.nci.cabig.ctms.dao.AbstractDomainObjectDao;

/**
 * Data Access Object for {@link Notification}s.
 * 
 * @author Denis G. Krylov
 * 
 */
@Transactional
public class NotificationDAOImpl extends AbstractDomainObjectDao<Notification>
		implements NotificationDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wustl.catissue.dao.NotificationDAO#save(edu.wustl.catissuecore.domain
	 * .ccts.Notification)
	 */
	public void save(Notification notification) {
		getHibernateTemplate().save(notification);
	}

	@Override
	public Class<Notification> domainClass() {
		return Notification.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wustl.catissue.dao.NotificationDAO#findByExample(edu.wustl.catissuecore
	 * .domain.ccts.Notification)
	 */
	public Collection<Notification> findByExample(Notification notification) {
		return getHibernateTemplate().findByExample(domainClass().getName(),
				notification);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissue.dao.NotificationDAO#getCountByExample(edu.wustl.
	 * catissuecore.domain.ccts.Notification)
	 */
	public int getCountByExample(final Notification notification) {
		return (Integer) getHibernateTemplate().executeWithNativeSession(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = session
								.createCriteria(domainClass());
						criteria.add(Example.create(notification))
								.setProjection(Projections.rowCount());
						return criteria.list().get(0);
					}
				});
	}

}
