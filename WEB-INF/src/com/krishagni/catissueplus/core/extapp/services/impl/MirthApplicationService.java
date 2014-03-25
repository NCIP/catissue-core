
package com.krishagni.catissueplus.core.extapp.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.common.util.ObjectType;
import com.krishagni.catissueplus.core.extapp.services.CrudService;
import com.krishagni.catissueplus.core.extapp.services.ExternalAppService;

public class MirthApplicationService implements ExternalAppService {

	private Map<ObjectType, CrudService> mirthSvcs = new HashMap<ObjectType, CrudService>();

	public MirthApplicationService() {
		mirthSvcs.put(ObjectType.PARTICIPANT, MirthParticipantService.getInstance());
	}

	@Override
	public String notifyInsert(ObjectType objectType, Object domainObj, String studyId) {
		String result = mirthSvcs.get(objectType).insert(domainObj, studyId);
		return result;
	}

	@Override
	public String notifyUpdate(ObjectType objectType, Object domainObj, String studyId) {
		String result = mirthSvcs.get(objectType).update(domainObj, studyId);
		return result;
	}

}
