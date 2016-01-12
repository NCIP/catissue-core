package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.common.domain.UpgradeLog;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.UpgradeLogDao;

public class UpgradeLogDaoImpl extends AbstractDao<UpgradeLog> implements UpgradeLogDao {

	@Override
	@SuppressWarnings("unchecked")
	public UpgradeLog getLatestVersion() {
		List<UpgradeLog> log = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_LATEST_VERSION)
				.setMaxResults(1)
				.list();
		
		return log.isEmpty() ? null : log.get(0);
	}
	
	private static final String FQN = UpgradeLog.class.getName();
	
	private static final String GET_LATEST_VERSION = FQN + ".getLatestVersion";
}