package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTaskListener;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTaskManager;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;

public class ScheduledTaskManagerImpl implements ScheduledTaskManager, ScheduledTaskListener {
	private static final Logger logger = Logger.getLogger(ScheduledTaskManagerImpl.class);

	private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
	
	private static Map<Long, ScheduledFuture<?>> scheduledJobs = new HashMap<Long, ScheduledFuture<?>>();
	
	private static final String JOB_FINISHED_TEMPLATE = "scheduled_job_finished";
	
	private static final String JOB_FAILED_TEMPLATE = "scheduled_job_failed";
	
	private DaoFactory daoFactory;
	
	private EmailService emailSvc;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setEmailSvc(EmailService emailSvc) {
		this.emailSvc = emailSvc;
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// Scheduled Task Manager API Start
	//
	//////////////////////////////////////////////////////////////////////////
	
	@Override
	@PlusTransactional
	public void schedule(Long jobId) {
		ScheduledJob job = getScheduledJob(jobId);
		if (job == null) {
			logger.error("No job found with ID = " + jobId);
			return;
		}
		
		schedule(job);
	}
	
	@Override
	@PlusTransactional
	public void schedule(ScheduledJob job) {
		if (job.isOnDemand()) {
			return;
		}

		User user = daoFactory.getUserDao().getSystemUser();
		runJob(user, job, null, getNextScheduleInMin(job));
	}

	@Override
	@PlusTransactional
	public void run(ScheduledJob job, String args) {
		runJob(AuthUtil.getCurrentUser(), job, args, 0L);
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
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, int intervalInMinutes) {
		return executorService.scheduleWithFixedDelay(task, intervalInMinutes, intervalInMinutes, TimeUnit.MINUTES);
	}

	//////////////////////////////////////////////////////////////////////////
	//
	// Scheduled Job Listener API implementation
	//
	//////////////////////////////////////////////////////////////////////////
	
	@Override
	@PlusTransactional
	public ScheduledJobRun started(ScheduledJob job, String args, User user) {
		try {
			ScheduledJobRun jobRun = new ScheduledJobRun();
			jobRun.inProgress(getScheduledJob(job.getId()));
			jobRun.setRunBy(user);
			jobRun.setRtArgs(args);
			
			daoFactory.getScheduledJobDao().saveOrUpdateJobRun(jobRun);
			initializeLazilyLoadedEntites(jobRun);
			return jobRun;
		} catch (Exception e) {
			logger.error("Error creating job run. Job name: " + job.getName(), e);
			throw new RuntimeException(e);
		}
		
	}

	@Override
	@PlusTransactional
	public void completed(ScheduledJobRun jobRun) {
		ScheduledJobRun dbRun = daoFactory.getScheduledJobDao().getJobRun(jobRun.getId());
		dbRun.completed();
		sendFinishedEmail(dbRun);
		scheduledJobs.remove(dbRun.getScheduledJob().getId());
		schedule(dbRun.getScheduledJob().getId());
	}

	@Override
	@PlusTransactional
	public void failed(ScheduledJobRun jobRun, Exception e) {
		ScheduledJobRun dbRun = daoFactory.getScheduledJobDao().getJobRun(jobRun.getId());
		dbRun.failed(e);
		sendFailureEmail(dbRun);
		scheduledJobs.remove(dbRun.getScheduledJob().getId());
		schedule(dbRun.getScheduledJob().getId());
	}
		
	private void runJob(User user, ScheduledJob job, String args, Long minutesLater) {
		if (isJobQueued(job)) {
			cancel(job);
		}

		if (!job.isActiveJob()) {
			return;
		}
		
		ScheduledTaskWrapper taskWrapper = new ScheduledTaskWrapper(job, args, user, this);
		ScheduledFuture<?> future = executorService.schedule(taskWrapper, minutesLater, TimeUnit.MINUTES);
		scheduledJobs.put(job.getId(), future);		
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
		long delay = (job.getNextRunOn().getTime() - System.currentTimeMillis()) / (1000 * 60);
		return delay < 0 ? 0 : delay;
	}
	
	private ScheduledJob getScheduledJob(Long jobId) {
		return daoFactory.getScheduledJobDao().getById(jobId);
	}

	private void sendFinishedEmail(ScheduledJobRun jobRun) {
		sendEmail(jobRun, JOB_FINISHED_TEMPLATE);
	}
	
	private void sendFailureEmail(ScheduledJobRun jobRun) {
		sendEmail(jobRun, JOB_FAILED_TEMPLATE);
	}
	
	private void sendEmail(ScheduledJobRun jobRun, String emailTmpl) {
		ScheduledJob job = jobRun.getScheduledJob();
		
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("job", job);
		props.put("jobRun", jobRun);
		props.put("appUrl",  ConfigUtil.getInstance().getAppUrl());		
		props.put("$subject", new String[] {job.getName()});

		List<String> recipients = new ArrayList<String>();		
		if (job.isOnDemand()) {
			props.put("fname", jobRun.getRunBy().getFirstName());
			props.put("lname", jobRun.getRunBy().getLastName());
			recipients.add(jobRun.getRunBy().getEmailAddress());
		} else {
			props.put("fname", job.getCreatedBy().getFirstName());
			props.put("lname", job.getCreatedBy().getLastName());			
		}
		
		for (User user : job.getRecipients()) {
			recipients.add(user.getEmailAddress());
		}
		
		if (recipients.isEmpty()) {
			return;
		}
		
		emailSvc.sendEmail(emailTmpl, recipients.toArray(new String[0]), props);
	}	
}
