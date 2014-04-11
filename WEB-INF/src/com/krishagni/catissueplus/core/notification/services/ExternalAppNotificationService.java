
package com.krishagni.catissueplus.core.notification.services;

import java.util.List;

import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;
import com.krishagni.catissueplus.core.notification.events.NotificationDetails;

public interface ExternalAppNotificationService {

	public void notifyExternalApps();

	public void notifyFailedNotifications();

	//TODO: below methods should be private and wont be exposed in interface 

	public void notifyExternalApps(NotificationDetails notifEvent);

	public List<NotificationDetails> getNotificationObjects();

	public List<ExtAppNotificationStatus> getFailedNotificationObjects();

	public void notifyFailedNotifications(ExtAppNotificationStatus extAppNotificationStatus);

}
