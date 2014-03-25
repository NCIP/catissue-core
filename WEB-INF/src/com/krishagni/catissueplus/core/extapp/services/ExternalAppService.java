
package com.krishagni.catissueplus.core.extapp.services;

import com.krishagni.catissueplus.core.common.util.ObjectType;

public interface ExternalAppService {

	public String notifyInsert(ObjectType objectType, Object domainObj, String studyId);

	public String notifyUpdate(ObjectType objectType, Object domainObj, String studyId);

}
