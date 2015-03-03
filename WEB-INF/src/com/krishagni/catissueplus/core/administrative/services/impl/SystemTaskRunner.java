package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.Set;

import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobInstanceDetail;
import com.krishagni.catissueplus.core.administrative.services.ScheduledJobService;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SystemTaskRunner implements ScheduledTask {

	private ScheduledJobService jobSvc;
	
	@Override
	public void doJob(ScheduledJobDetail jobDetail) throws Exception {
		jobSvc = OpenSpecimenAppCtxProvider.getAppCtx().getBean(ScheduledJobService.class);

		ScheduledJobInstanceDetail jobInstance = null;
		jobInstance = createJobInstance(jobDetail);
		
		Long jobInstanceId = jobInstance.getId();
		executeProcess(jobDetail, jobInstanceId);
		
		//TODO: implement when new email service is in place
	//	String dropFilePath = getDropFilePath(jobInstanceId);
	//	sendEmail(jobDetail.getRecipients(), dropFilePath);
	}
	
	private void executeProcess(ScheduledJobDetail jobDetail, Long jobInstanceId) {
		String command = new String(jobDetail.getCommand());
		command += " " + jobInstanceId.toString();
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void sendEmail(Set<String> recipients, String dropFilePath) {
		//TODO: code this when the email service is ready
	}
	
	private String getDropFilePath(Long jobInstanceId) {
		RequestEvent<Long> req = new RequestEvent<Long>();
		req.setPayload(jobInstanceId);
		ResponseEvent<ScheduledJobInstanceDetail> resp = jobSvc.getJobInstance(req);
		resp.throwErrorIfUnsuccessful();
		
		ScheduledJobInstanceDetail detail = resp.getPayload();
		if (detail.getStatus().equals("SUCCEEDED")) {
			return detail.getLogFilePath();
		}
		
		return null;
	}
	
	private ScheduledJobInstanceDetail createJobInstance(ScheduledJobDetail jobDetail) {
		ScheduledJobInstanceDetail instance = new ScheduledJobInstanceDetail();
		instance.setScheduledJob(jobDetail);
		instance.setStatus("PENDING");
		
		ResponseEvent<ScheduledJobInstanceDetail> resp = jobSvc.createScheduledJobInstance(new RequestEvent<ScheduledJobInstanceDetail>(null, instance));
		resp.throwErrorIfUnsuccessful();
		instance = resp.getPayload();
		return instance;
	}
}
