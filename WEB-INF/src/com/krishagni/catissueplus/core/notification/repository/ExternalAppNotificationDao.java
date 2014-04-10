
package com.krishagni.catissueplus.core.notification.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;
import com.krishagni.catissueplus.core.notification.events.NotificationDetails;

public interface ExternalAppNotificationDao extends Dao<ExtAppNotificationStatus> {

	public List<NotificationDetails> getNotificationObjects();

	public List<ExtAppNotificationStatus> getFailedNotificationObjects();

	public List<ExtAppNotificationStatus> getExpiredNotificationObjects();

}
