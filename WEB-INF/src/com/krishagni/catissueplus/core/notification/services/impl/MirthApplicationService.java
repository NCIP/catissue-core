
package com.krishagni.catissueplus.core.notification.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import com.krishagni.catissueplus.core.common.util.ObjectType;
import com.krishagni.catissueplus.core.notification.services.CrudService;
import com.krishagni.catissueplus.core.notification.services.ExternalAppService;

public class MirthApplicationService implements ExternalAppService {

	private Map<ObjectType, CrudService> mirthSvcs = new HashMap<ObjectType, CrudService>();

	private SessionFactory sessionFactory;

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
	public String notifyInsert(ObjectType objectType, Object domainObj) {
		String result = mirthSvcs.get(objectType).insert(domainObj);
		return result;
	}

	@Override
	public String notifyUpdate(ObjectType objectType, Object domainObj) {
		String studyId = "test";
		String result = mirthSvcs.get(objectType).update(domainObj);
		return result;
	}

	public String getMappedStudyId(Long cpId, String appName) {
		SQLQuery query = sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"select study_id from catissue_cp_extapp_study_rel cpstudyrel join catissue_external_application extapp on cpstudyrel.app_id = extapp.identifier where cpstudyrel.cp_id =:cpId and extapp.app_name=:appName");
		query.setLong("cpId", cpId);
		query.setString("appName", appName);
		List<String> result = query.list();
		String studyId = null;
		for (String object : result) {
			studyId = object;
		}
		return studyId;
	}

}
