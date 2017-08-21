package com.krishagni.catissueplus.core.biospecimen.services.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.common.PlusTransactional;

@Configurable
public class RefreshSpecimenDetailsJob implements ScheduledTask {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@PlusTransactional
	public void doJob(ScheduledJobRun jobRun)
	throws Exception {
		sessionFactory.getCurrentSession()
			.createSQLQuery("CALL refresh_specimen_stats()")
			.executeUpdate();
		
	}

}
