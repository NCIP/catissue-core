
package com.krishagni.catissueplus.core.notification.services;

import java.util.List;

import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;
import com.krishagni.catissueplus.core.notification.events.NotificationDetails;

public interface ExternalAppNotificationService {

	public void notifyExternalApps();

	public void notifyExternalApps(NotificationDetails notifEvent);

	public List<NotificationDetails> getNotificationObjects();

	public void notifyFailedNotifications();

	public List<ExtAppNotificationStatus> getFailedNotificationObjects();

	public void notifyFailedNotifications(ExtAppNotificationStatus extAppNotificationStatus);

}
