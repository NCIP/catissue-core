package com.krishagni.catissueplus.core.common.repository;

import com.krishagni.catissueplus.core.common.domain.UpgradeLog;

public interface UpgradeLogDao extends Dao<UpgradeLog> {
	public UpgradeLog getLatestVersion();
}