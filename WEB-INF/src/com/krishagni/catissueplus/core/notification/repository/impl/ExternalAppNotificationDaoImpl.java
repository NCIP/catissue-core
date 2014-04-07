
package com.krishagni.catissueplus.core.notification.repository.impl;

import java.util.ArrayList;
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

	private static String NO_OF_RETRIES = "NoOfRetriesForFailNotifications";

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
		query.setString("status", "FAIL");
		int noOfRetries = Integer.parseInt(XMLPropertyHandler.getValue(NO_OF_RETRIES).trim());
		query.setInteger("maxNoOfRetries", noOfRetries);
		List<ExtAppNotificationStatus> result = query.list();
		return result;

	}
}
