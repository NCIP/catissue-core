
package com.krishagni.catissueplus.core.notification.services;

import java.util.List;

import com.krishagni.catissueplus.core.notification.events.NotificationDto;

public interface ExternalAppNotificationService {

	public void notifyExternalApps();

	public void notifyExternalApps(NotificationDto notifEvent);

	public List<NotificationDto> getNotificationObjects();

}
