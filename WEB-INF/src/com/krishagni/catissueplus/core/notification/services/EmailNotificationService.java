
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.notification.events.FailedNotificationReportEvent;

public interface EmailNotificationService {

	public FailedNotificationReportEvent sendFailedNotificationReport();

}
