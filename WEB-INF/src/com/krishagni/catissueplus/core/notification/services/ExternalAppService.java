
package com.krishagni.catissueplus.core.notification.services;

import com.krishagni.catissueplus.core.common.util.ObjectType;

public interface ExternalAppService {


	public enum Status {
		SUCCESS, FAIL
	};
	public Status notifyInsert(ObjectType objectType, Object domainObj);

	public Status notifyUpdate(ObjectType objectType, Object domainObj);
	
	public Status notifyDelete(ObjectType objectType, Object domainObj);
	

}
