
package com.krishagni.catissueplus.core.notification.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.audit.domain.Audit;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.ObjectType;
import com.krishagni.catissueplus.core.common.util.Operation;
import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;
import com.krishagni.catissueplus.core.notification.events.NotificationDetails;
import com.krishagni.catissueplus.core.notification.repository.ExternalAppNotificationDao;

import edu.wustl.common.util.XMLPropertyHandler;

public class ExternalAppNotificationDaoImpl extends AbstractDao<ExtAppNotificationStatus>
		implements
			ExternalAppNotificationDao {

	private static String GET_NOTIFICATION_DETAILS = Audit.class.getName() + ".getNotificationObjects";

	private static String GET_FAILED_NOTIFICATIONS = ExtAppNotificationStatus.class.getName()
			+ ".getFailedNotificationObjects";

	private static String GET_EXPIRED_FAILED_NOTIFICATIONS = ExtAppNotificationStatus.class.getName()
			+ ".getExpiredFailedNotificationObjects";

	private static String MAX_NO_OF_ATTEMPTS_PROPERTY = "NoOfAttemptsForFailNotifications";

	private static String STATUS = "status";

	private static String MAX_NO_OF_ATTEMPTS = "maxNoOfAttempts";

	@Override
	@SuppressWarnings("unchecked")
	public List<NotificationDetails> getNotificationObjects() {
		List<NotificationDetails> notifEventList = new ArrayList<NotificationDetails>();
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_NOTIFICATION_DETAILS);
		List<Object[]> result = query.list();
		for (Object[] object : result) {
			NotificationDetails notifEvent = new NotificationDetails();
			notifEvent.setAuditId(Long.parseLong(object[0].toString()));
			notifEvent.setObjectType(ObjectType.valueOf(object[1].toString()));
			notifEvent.setObjectId(Long.parseLong(object[2].toString()));
			notifEvent.setOperation(Operation.valueOf((String) object[3]));
			notifEventList.add(notifEvent);
		}

		return notifEventList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ExtAppNotificationStatus> getFailedNotificationObjects() {

		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_FAILED_NOTIFICATIONS);
		query.setString(STATUS, "FAIL");
		int maxAttempts = Integer.parseInt(XMLPropertyHandler.getValue(MAX_NO_OF_ATTEMPTS_PROPERTY).trim());
		query.setInteger(MAX_NO_OF_ATTEMPTS, maxAttempts);
		List<ExtAppNotificationStatus> result = query.list();
		return result;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ExtAppNotificationStatus> getExpiredNotificationObjects() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_EXPIRED_FAILED_NOTIFICATIONS);
		query.setString(STATUS, "FAIL");
		int maxAttempts = Integer.parseInt(XMLPropertyHandler.getValue(MAX_NO_OF_ATTEMPTS_PROPERTY).trim());
		query.setInteger(MAX_NO_OF_ATTEMPTS, maxAttempts);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date startDate = cal.getTime();
		query.setTimestamp("date", startDate);
		List<ExtAppNotificationStatus> result = query.list();
		return result;

	}
}
