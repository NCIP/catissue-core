
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.notification.events.NotificationResponse;

public interface CrudService {

	public NotificationResponse insert(Object domainObj);

	public NotificationResponse update(Object domainObj);

	public NotificationResponse delete(Object domainObj);

}
