
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.common.util.ObjectType;

public interface ExternalAppService {

	public String notifyInsert(ObjectType objectType, Object domainObj);

	public String notifyUpdate(ObjectType objectType, Object domainObj);

}
