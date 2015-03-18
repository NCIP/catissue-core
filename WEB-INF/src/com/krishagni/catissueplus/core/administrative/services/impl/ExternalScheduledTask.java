package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;

public class ExternalScheduledTask implements ScheduledTask {

	@Override
	public void doJob(ScheduledJobRun jobRun) throws Exception {
		executeProcess(jobRun);
	}
	
	private void executeProcess(ScheduledJobRun jobRun) {
		String command = jobRun.getScheduledJob().getCommand();
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
