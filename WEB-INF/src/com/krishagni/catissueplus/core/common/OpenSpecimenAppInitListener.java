package com.krishagni.catissueplus.core.common;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.krishagni.catissueplus.core.administrative.services.ScheduledJobService;

public class OpenSpecimenAppInitListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		loadAllScheduledJobs(event);
	}
	
	private void loadAllScheduledJobs(ContextRefreshedEvent event) {
		try {
			ScheduledJobService jobSvc = event.getApplicationContext().getBean(ScheduledJobService.class);
			jobSvc.loadAllJobs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
