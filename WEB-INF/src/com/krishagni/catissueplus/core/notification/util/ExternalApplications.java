
package com.krishagni.catissueplus.core.notification.util;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CaTissueAppContext;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.notification.domain.ExternalApplication;

public class ExternalApplications {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	private static List<ExternalApplication> externalApplications;

	public List<ExternalApplication> getAllExternalApplications() {
		if (externalApplications == null) {
			ApplicationContext caTissueContext = CaTissueAppContext.getInstance();
			ExternalApplications extApps = (ExternalApplications) caTissueContext.getBean("externalApplications");
			externalApplications = extApps.getAllExternalApps();
		}
		return externalApplications;

	}

	@PlusTransactional
	private List<ExternalApplication> getAllExternalApps() {
		return daoFactory.getExternalApplicationDao().getAllExternalApps();

	}
}
