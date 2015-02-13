
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface EmailNotificationService {

	public ResponseEvent<Boolean> sendFailedNotificationReport();

}
