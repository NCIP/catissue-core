
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.common.util.ObjectType;
import com.krishagni.catissueplus.core.notification.events.NotificationResponse;

public interface ExternalAppService {

	public NotificationResponse notifyInsert(ObjectType objectType, Object domainObj);

	public NotificationResponse notifyUpdate(ObjectType objectType, Object domainObj);

	public NotificationResponse notifyDelete(ObjectType objectType, Object domainObj);

}
