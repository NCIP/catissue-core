
package com.krishagni.catissueplus.core.extapp.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.audit.domain.Audit;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.ObjectType;
import com.krishagni.catissueplus.core.common.util.Operation;
import com.krishagni.catissueplus.core.extapp.domain.ExternalAppNotification;
import com.krishagni.catissueplus.core.extapp.domain.ExternalApplication;
import com.krishagni.catissueplus.core.extapp.events.NotificationDto;
import com.krishagni.catissueplus.core.extapp.repository.ExternalAppNotificationDao;

public class ExternalAppNotificationDaoImpl extends AbstractDao<ExternalAppNotification>
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
			notifEvent.setExtApp((ExternalApplication) object[4]);
			notifEvent.setStudyId((String) object[5]);
			notifEventList.add(notifEvent);
		}

		return notifEventList;
	}
}
