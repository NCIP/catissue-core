
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.notification.services.ExternalAppService.Status;

public interface CrudService {

	public Status insert(Object domainObj);

	public Status update(Object domainObj);

	public Status delete(Object domainObj);

}
