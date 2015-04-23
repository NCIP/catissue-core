package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.hibernate.Hibernate;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTaskListener;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTaskManager;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

public class ScheduledTaskManagerImpl implements ScheduledTaskManager, ScheduledTaskListener {
	private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
	
	private static Map<Long, ScheduledFuture<?>> scheduledJobs = new HashMap<Long, ScheduledFuture<?>>();
	
	private DaoFactory daoFactory;
	
	private EmailService emailSvc;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setEmailSvc(EmailService emailSvc) {
		this.emailSvc = emailSvc;
	}

	@Override
	public void schedule(ScheduledJob job) {
		if (isJobQueued(job)) {
			cancel(job);
		} 
		
		if (!job.isActiveJob()) {
			return;
		}
		
		ScheduledTaskWrapper taskWrapper = new ScheduledTaskWrapper(job, this);
		ScheduledFuture<?> future = executorService.schedule(taskWrapper, getNextScheduleInMin(job), TimeUnit.MINUTES);
		scheduledJobs.put(job.getId(), future);
	}
	
	@Override
	public void cancel(ScheduledJob job) {
		ScheduledFuture<?> future = scheduledJobs.remove(job.getId());
		try {
			future.cancel(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ScheduledJobRun started(ScheduledJob job) {
		return createJobRun(job.getId());
	}

	@Override
	public void completed(ScheduledJobRun jobRun) {
		jobRun.completed();
		jobRun = updateJobRun(jobRun);
		scheduledJobs.remove(jobRun.getScheduledJob().getId());
		schedule(jobRun.getScheduledJob());
	}

	@Override
	public void failed(ScheduledJobRun jobRun, Exception e) {
		jobRun.failed(e);
		jobRun = updateJobRun(jobRun);
		sendFailureEmail(jobRun);
		scheduledJobs.remove(jobRun.getScheduledJob().getId());
		schedule(jobRun.getScheduledJob());
	}
	
	@PlusTransactional
	public ScheduledJobRun createJobRun(Long jobId) {
		try {
			ScheduledJobRun jobRun = new ScheduledJobRun();
			jobRun.inProgress(getScheduledJob(jobId));
			daoFactory.getScheduledJobDao().saveOrUpdateJobRun(jobRun);
			initializeLazilyLoadedEntites(jobRun);
			return jobRun;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@PlusTransactional
	public ScheduledJobRun updateJobRun(ScheduledJobRun jobRun) {
		try {
			ScheduledJobRun existing = daoFactory.getScheduledJobDao().getJobRun(jobRun.getId());
			existing.update(jobRun);
			daoFactory.getScheduledJobDao().saveOrUpdateJobRun(existing);
			initializeLazilyLoadedEntites(existing);
			return existing;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void initializeLazilyLoadedEntites(ScheduledJobRun jobRun) {
		Hibernate.initialize(jobRun.getScheduledJob());
		Hibernate.initialize(jobRun.getScheduledJob().getCreatedBy());
		Hibernate.initialize(jobRun.getScheduledJob().getRecipients());		
	}

	private boolean isJobQueued(ScheduledJob job) {
		return scheduledJobs.containsKey(job.getId());
	}
	
	private Long getNextScheduleInMin(ScheduledJob job) {
		Long delay = (job.getNextRunOn().getTime() - (new Date().getTime()))/( 1000 * 60 );
		delay = delay < 0 ? 0 : delay;
		return delay;
	}
	
	private ScheduledJob getScheduledJob(Long jobId) {
		return daoFactory.getScheduledJobDao().getById(jobId);
	}

	private void sendFailureEmail(ScheduledJobRun jobRun) {
		ScheduledJob job = jobRun.getScheduledJob();
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("fname", job.getCreatedBy().getFirstName());
		props.put("lname", job.getCreatedBy().getLastName());
		props.put("jobName", job.getName());
		props.put("jobRunId", jobRun.getId());
		props.put("appUrl",  ConfigUtil.getInstance().getAppUrl());
		props.put("$subject", new String[] {job.getName()});
		List<String> recipients = new ArrayList<String>();
		
		for (User user : job.getRecipients()) {
			recipients.add(user.getEmailAddress());
		}
		
		String[] recipientEmails = new String[recipients.size()];
		emailSvc.sendEmail(JOB_FAILED_TEMPLATE, recipients.toArray(recipientEmails), props);
	}
	
	public static String getExportDataDir() {
		String dir = new StringBuilder()
			.append(ConfigUtil.getInstance().getDataDir()).append(File.separator)			
			.append("scheduled-jobs").append(File.separator)
			.toString();
		
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			if (!dirFile.mkdirs()) {
				throw new RuntimeException("Error couldn't create directory for exporting scheduled jobs data");
			}
		}
		
		return dir;
	}
	
	private static final String JOB_FAILED_TEMPLATE = "scheduled_job_failed";
}