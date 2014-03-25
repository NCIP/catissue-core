
package com.krishagni.catissueplus.core.extapp.services;

import java.util.List;

import com.krishagni.catissueplus.core.extapp.events.NotificationDto;

public interface ExternalAppNotificationService {

	public void notifyExternalApps(NotificationDto notifEvent);

	public void notificationService();

	public List<NotificationDto> getNotificationObjects();

}
