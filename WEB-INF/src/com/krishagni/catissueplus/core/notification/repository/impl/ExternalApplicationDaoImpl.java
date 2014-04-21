
package com.krishagni.catissueplus.core.notification.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.notification.domain.ExternalApplication;
import com.krishagni.catissueplus.core.notification.repository.ExternalApplicationDao;

public class ExternalApplicationDaoImpl extends AbstractDao<ExternalApplication> implements ExternalApplicationDao {

	private static String GET_ALL_EXTERNAL_APPS = ExternalApplication.class.getName() + ".getAllExternalApps";

	@Override
	public List<ExternalApplication> getAllExternalApps() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_EXTERNAL_APPS);
		List<ExternalApplication> extApps = query.list();
		return extApps;
	}

}
