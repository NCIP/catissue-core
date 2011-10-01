/**
 * 
 */
package edu.wustl.catissuecore.dao;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Transactional;

import edu.wustl.catissuecore.domain.ccts.Notification;
import edu.wustl.catissuecore.domain.ccts.ProcessingStatus;

/**
 * Data Access Object for {@link Notification}s.
 * 
 * @author Denis G. Krylov
 * 
 */
@Transactional
public class NotificationDAOImpl extends GenericDAOImpl implements
		NotificationDAO {

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.dao.NotificationDAO#getNotifications(int,
	 * int)
	 */
	@Transactional(readOnly = true)
	public Collection<Notification> getNotifications(final int firstResult,
			final int maxResults) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				Criteria criteria = session.createCriteria(Notification.class);
				criteria.addOrder(Order.desc("dateSent")).addOrder(
						Order.asc("id"));
				criteria.setFirstResult(firstResult);
				criteria.setMaxResults(maxResults);
				return criteria.list();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.dao.NotificationDAO#getById(int)
	 */
	@Override
	@Transactional(readOnly = true)
	public Notification getById(long id) {
		final Notification notification = (Notification) getHibernateTemplate()
				.get(Notification.class, id);
		if (notification != null) {
			notification.getProcessingLog().size();
		}
		return notification;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.wustl.catissuecore.dao.NotificationDAO#pickNotificationForProcessing
	 * ()
	 */
	@Override
	public Notification pickNotificationForProcessing() {
		List<Notification> list = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = session
								.createCriteria(Notification.class);
						criteria.add(
								Restrictions.in("processingStatus",
										new ProcessingStatus[] {
												ProcessingStatus.PENDING,
												ProcessingStatus.PROCESSING }))
								.addOrder(Order.asc("dateSent"))
								.setFirstResult(0).setMaxResults(1);
						return criteria.list();
					}
				});
		Notification notification = null;
		if (CollectionUtils.isNotEmpty(list)) {
			notification = list.get(0);
			if (notification.getProcessingStatus() == ProcessingStatus.PENDING) {
				notification.setProcessingStatus(ProcessingStatus.PROCESSING);
				getHibernateTemplate().update(notification);
			}
			notification.getProcessingLog().size();
		}
		return notification;
	}

}
