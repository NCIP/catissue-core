
package com.krishagni.catissueplus.core.notification.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.common.util.ObjectType;
import com.krishagni.catissueplus.core.notification.events.NotificationResponse;
import com.krishagni.catissueplus.core.notification.services.CrudService;
import com.krishagni.catissueplus.core.notification.services.ExternalAppService;

public class MirthApplicationService implements ExternalAppService {

	private Map<ObjectType, CrudService> mirthSvcs = new HashMap<ObjectType, CrudService>();

	private SessionFactory sessionFactory;

	private static final String GET_MAPPED_STUDY = "select study_id from catissue_cp_extapp_study_rel cpstudyrel join catissue_external_application extapp on cpstudyrel.app_id = extapp.identifier where cpstudyrel.cp_id =:cpId and extapp.app_name=:appName";

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public MirthApplicationService() {
		mirthSvcs.put(ObjectType.PARTICIPANT, MirthParticipantService.getInstance());
	}

	@Override
	public NotificationResponse notifyInsert(ObjectType objectType, Object domainObj) {
		return mirthSvcs.get(objectType).insert(domainObj);
	}

	@Override
	public NotificationResponse notifyUpdate(ObjectType objectType, Object domainObj) {
		return mirthSvcs.get(objectType).update(domainObj);
	}

	@SuppressWarnings("unchecked")
	public String getMappedStudyId(Long cpId, String appName) {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(GET_MAPPED_STUDY);
		query.setLong("cpId", cpId);
		query.setString("appName", appName);

		List<String> result = query.list();
		String studyId = null;
		for (String object : result) {
			studyId = object;
		}
		return studyId;
	}

	@Override
	public NotificationResponse notifyDelete(ObjectType objectType, Object domainObj) {
		//TODO: implement delete 
		return NotificationResponse.fail("Delete operation is not supported");
	}

}
