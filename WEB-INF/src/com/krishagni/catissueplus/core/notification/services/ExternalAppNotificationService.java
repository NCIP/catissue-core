
package com.krishagni.catissueplus.core.notification.services;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;
import com.krishagni.catissueplus.core.notification.events.NotificationDetails;

public interface ExternalAppNotificationService {

	public void notifyExternalApps();

	public void notifyFailedNotifications();

	//TODO: below methods should be private and wont be exposed in interface 

	public ResponseEvent<Boolean> notifyExternalApps(NotificationDetails notifEvent);

	public ResponseEvent<List<NotificationDetails>> getNotificationObjects();

	public ResponseEvent<List<ExtAppNotificationStatus>> getFailedNotificationObjects();

	public ResponseEvent<Boolean> notifyFailedNotifications(ExtAppNotificationStatus extAppNotificationStatus);

}
