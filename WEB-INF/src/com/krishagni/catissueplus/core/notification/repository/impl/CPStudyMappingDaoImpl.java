
package com.krishagni.catissueplus.core.notification.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.notification.domain.CollectionProtocolStudyMapping;
import com.krishagni.catissueplus.core.notification.repository.CPStudyMappingDao;

public class CPStudyMappingDaoImpl extends AbstractDao<CollectionProtocolStudyMapping> implements CPStudyMappingDao {

	@Override
	public Long getMappedCPId(String appName, String studyId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_MAPPED_CP_ID);
		query.setString("studyId", studyId);
		query.setString("appName", appName);
		List<Long> result = query.list();
		Long cpId = null;
		for (Long object : result) {
			cpId = object;
		}
		return cpId;

	}

	private static final String FQN = CollectionProtocolStudyMapping.class.getName();

	private static final String GET_MAPPED_CP_ID = FQN + ".getMappedCpId";

}
