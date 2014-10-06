
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;
import com.krishagni.catissueplus.core.notification.events.FailedNotificationObjectsEvent;
import com.krishagni.catissueplus.core.notification.events.NotificationDetails;
import com.krishagni.catissueplus.core.notification.events.NotificationObjectsEvent;
import com.krishagni.catissueplus.core.notification.events.NotifiedExternalAppEvent;

public interface ExternalAppNotificationService {

	public void notifyExternalApps();

	public void notifyFailedNotifications();

	//TODO: below methods should be private and wont be exposed in interface 

	public NotifiedExternalAppEvent notifyExternalApps(NotificationDetails notifEvent);

	public NotificationObjectsEvent getNotificationObjects();

	public FailedNotificationObjectsEvent getFailedNotificationObjects();

	public NotifiedExternalAppEvent notifyFailedNotifications(ExtAppNotificationStatus extAppNotificationStatus);

}
