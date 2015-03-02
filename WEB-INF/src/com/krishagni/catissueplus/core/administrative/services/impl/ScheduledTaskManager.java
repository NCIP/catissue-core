package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.krishagni.catissueplus.core.administrative.domain.factory.ScheduledJobErrorCode;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobListCriteria;
import com.krishagni.catissueplus.core.administrative.services.ScheduledJobService;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ScheduledTaskManager {
	private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
	
	private static Map<Long, ScheduledFuture> queuedTasks = new HashMap<Long, ScheduledFuture>();
	
	private static Map<String, String> taskTypeMap = new HashMap<String, String>();
	
	private static Map<String, Long> periodInMinutes = new HashMap<String, Long>();
	
	private static ScheduledJobService jobSvc;
	
	static {
		taskTypeMap.put("system", SystemTaskRunner.class.getName());
		jobSvc = OpenSpecimenAppCtxProvider.getAppCtx().getBean(ScheduledJobService.class);
		periodInMinutes.put("HOURLY" , 60L);
		periodInMinutes.put("DAILY"  , 60L * 24L);
		periodInMinutes.put("MONTHLY", 60L * 24L * 30L);
		periodInMinutes.put("WEEKLY" , 60L * 24L * 7L);
		periodInMinutes.put("YEARLY" , 60L * 24L * 365L);
		
		/*
		 * Monitors the scheduled threads and re-schedules broken ones.
		 * Looks up every 30 minutes
		 */
		executorService.scheduleAtFixedRate(new ScheduledJobsMonitor(), 2, 30, TimeUnit.MINUTES);
	}
	
	public static void registerJob(ScheduledJobDetail detail) {
		if (queuedTasks.get(detail.getId()) == null) {
			ScheduledJobRunner runner = new ScheduledJobRunner(getTask(detail), detail);
			ScheduledFuture future = null;
			
			if (detail.getRepeatSchedule().equals(("ONCE"))) {
				future = executorService.schedule(runner, getInitialDelay(detail), TimeUnit.MINUTES);
			} else { 
				future = executorService.scheduleAtFixedRate(runner, getInitialDelay(detail), 
						getPeriodInMinutes(detail), TimeUnit.MINUTES);
			}
			
			queuedTasks.put(detail.getId(), future);
		}
	}
	
	public static void cancelJob(Long jobId) {
		ScheduledFuture future = queuedTasks.get(jobId);
		
		try {
			future.cancel(false);
		} catch (Exception e) {
			
		}
	}
	
	public static void registerTaskType(String name, String fqn) {
		taskTypeMap.put(name, fqn);
	}
	
	public static void loadJobs() {
		RequestEvent<ScheduledJobListCriteria> req = new RequestEvent<ScheduledJobListCriteria>();
		req.setPayload(new ScheduledJobListCriteria());
		ResponseEvent<List<ScheduledJobDetail>> resp = jobSvc.getScheduledJobs(req);
		resp.throwErrorIfUnsuccessful();
		for (ScheduledJobDetail detail : resp.getPayload()) {
			if (!detail.isExpired()) {
				registerJob(detail);
			}
		}
	}
	
	public static void Refresh() {
		Set<Long> erroredJobs = new HashSet<Long>();
		
		for (Map.Entry<Long, ScheduledFuture> entry : queuedTasks.entrySet()) {
			try {
				entry.getValue().get(0, TimeUnit.MINUTES);
			} catch (ExecutionException ee) {
				erroredJobs.add(entry.getKey());
			} catch (Exception e) {
				
			}
		}

		if (erroredJobs.size() > 0) {
			for (Long jobId : erroredJobs) {
				queuedTasks.remove(jobId);
			}
			
			loadJobs();
		}
	}
	
	private static ScheduledTask getTask(ScheduledJobDetail detail) {
		try {
			String className = taskTypeMap.get(detail.getType());
			ScheduledTask task = (ScheduledTask)Class.forName(className).newInstance();
			return task;
		} catch (Exception e) {
			throw OpenSpecimenException.userError(ScheduledJobErrorCode.INVALID_TYPE);
		}
	}
	
	private static Long getInitialDelay(ScheduledJobDetail detail) {
		Long delay = (detail.getNextRunOn().getTime() - (new Date().getTime()))/( 1000 * 60 );
		delay = delay < 0 ? 0 : delay;
		return delay;
	}
	
	private static Long getPeriodInMinutes(ScheduledJobDetail detail) {
		return periodInMinutes.get(detail.getRepeatSchedule());
	}
}