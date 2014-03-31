
package com.krishagni.catissueplus.core.notification.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.audit.domain.Audit;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.ObjectType;
import com.krishagni.catissueplus.core.common.util.Operation;
import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;
import com.krishagni.catissueplus.core.notification.events.NotificationDto;
import com.krishagni.catissueplus.core.notification.repository.ExternalAppNotificationDao;

public class ExternalAppNotificationDaoImpl extends AbstractDao<ExtAppNotificationStatus>
		implements
			ExternalAppNotificationDao {

	private static String GET_NOTIFICATION_DETAILS = Audit.class.getName() + ".getNotificationObjects";

	public List<NotificationDto> getNotificationObjects() {
		List<NotificationDto> notifEventList = new ArrayList<NotificationDto>();
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_NOTIFICATION_DETAILS);
		List<Object[]> result = query.list();
		for (Object[] object : result) {
			NotificationDto notifEvent = new NotificationDto();
			notifEvent.setAuditId(Long.parseLong(object[0].toString()));
			notifEvent.setObjectType(ObjectType.valueOf(object[1].toString()));
			notifEvent.setObjectId(Long.parseLong(object[2].toString()));
			notifEvent.setOperation(Operation.valueOf((String) object[3]));
			notifEventList.add(notifEvent);
		}

		return notifEventList;
	}
}
