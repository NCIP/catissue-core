package com.krishagni.catissueplus.core.init;

import java.util.Date;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.domain.UpgradeLog;

public class UpgradeLogUpdater implements InitializingBean {
	private DaoFactory daoFactory;
	
	private Properties appProps = new Properties();
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setAppProps(Properties appProps) {
		this.appProps = appProps;
	}

	@Override
	@PlusTransactional
	public void afterPropertiesSet() throws Exception {
		insertUpgradeLog();
	}
	
	private void insertUpgradeLog() {
		String version = appProps.getProperty("buildinfo.version");
		UpgradeLog existing = daoFactory.getUpgradeLogDao().getLatestVersion();
		if (existing == null || !existing.getVersion().equals(version)) {
			UpgradeLog log = new UpgradeLog();
			log.setUpgradeDate(new Date());
			log.setVersion(version);
			
			daoFactory.getUpgradeLogDao().saveOrUpdate(log);
		}
	}
}