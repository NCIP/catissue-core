package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob.DayOfWeek;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob.RepeatSchedule;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob.Type;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.ScheduledJobErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ScheduledJobFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.ScheduledJobDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class ScheduledJobFactoryImpl implements ScheduledJobFactory {

	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public ScheduledJob createScheduledJob(ScheduledJobDetail detail) {

		ScheduledJob job = new ScheduledJob();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		setName(detail, job, ose);
		setRepeatSchedule(detail, job, ose);
		setStartAndEndDates(detail, job, ose);
		setActivityStatus(detail, job, ose);
		setType(detail, job, ose);
		setRecipients(detail, job, ose);
		
		job.setRtArgsProvided(detail.getRtArgsProvided());
		job.setRtArgsHelpText(detail.getRtArgsHelpText());
			
		ose.checkAndThrow();
		return job;
	}
	
	private void setName(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(ScheduledJobErrorCode.NAME_REQUIRED);
			return;
		}
		
		job.setName(name);
	}

	//
	// this method requires to be invoked after setting repeat schedule
	//
	private void setStartAndEndDates(ScheduledJobDetail detail,	ScheduledJob job, OpenSpecimenException ose) {
		if (job.getRepeatSchedule() == null || job.getRepeatSchedule().equals(RepeatSchedule.ONDEMAND)) {
			return;
		}
		
		if (detail.getStartDate() == null) {
			ose.addError(ScheduledJobErrorCode.START_DATE_REQUIRED);
			return;
		}
		
		if (detail.getEndDate() != null && detail.getEndDate().before(detail.getStartDate())) {
			ose.addError(ScheduledJobErrorCode.END_DATE_BEFORE_START_DATE);
			return;
		}
		
		job.setStartDate(detail.getStartDate());
		job.setEndDate(detail.getEndDate());
		
	}
	
	private void setCreatedBy(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		User createdBy = getUser(detail.getCreatedBy());
		if (createdBy == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		job.setCreatedBy(createdBy);
	}
	
	private void setActivityStatus(ScheduledJobDetail detail, ScheduledJob job,	OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}

		if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}

		job.setActivityStatus(activityStatus);
	}
	
	private void setRepeatSchedule(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		String repeatSchedule = detail.getRepeatSchedule();
		if (StringUtils.isBlank(repeatSchedule)) {
			ose.addError(ScheduledJobErrorCode.REPEAT_SCHEDULE_REQUIRED);
			return;
		}
		
		try {
			job.setRepeatSchedule(RepeatSchedule.valueOf(detail.getRepeatSchedule()));
		} catch (IllegalArgumentException ile) {
			ose.addError(ScheduledJobErrorCode.INVALID_REPEAT_SCHEDULE, detail.getRepeatSchedule());
			return;
		}
		
		if (job.getRepeatSchedule().equals(RepeatSchedule.ONDEMAND)) {
			job.setScheduledMinute(0);
			return;
		}
		
		setScheduledMinute(detail, job, ose);
		setScheduledHour(detail, job, ose);
		setScheduledDayOfWeek(detail, job, ose);
		setScheduledDayOfMonth(detail, job, ose);
	}

	private void setScheduledMinute(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		Integer scheduledMinute = detail.getScheduledMinute();
		if(job.getRepeatSchedule() == RepeatSchedule.MINUTELY){
			return;
		}
		if (scheduledMinute == null || scheduledMinute < 0 || scheduledMinute > 59) {
			ose.addError(ScheduledJobErrorCode.INVALID_SCHEDULED_TIME);
			return;
		}
		
		job.setScheduledMinute(scheduledMinute);
	}
	
	private void setScheduledHour(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		if (job.getRepeatSchedule() == RepeatSchedule.MINUTELY || job.getRepeatSchedule() == RepeatSchedule.HOURLY) {
			return;
		}
		
		Integer scheduledHour = detail.getScheduledHour();
		if (scheduledHour == null || scheduledHour < 0 || scheduledHour > 23) {
			ose.addError(ScheduledJobErrorCode.INVALID_SCHEDULED_TIME);
			return;
		}
		
		job.setScheduledHour(detail.getScheduledHour());
	}
	
	private void setScheduledDayOfWeek(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		if (job.getRepeatSchedule() != RepeatSchedule.WEEKLY) {
			return;
		}
		
		DayOfWeek dow = null;
		try {
			dow = DayOfWeek.valueOf(detail.getScheduledDayOfWeek());
		} catch (Exception e) {
			ose.addError(ScheduledJobErrorCode.INVALID_SCHEDULED_TIME);
			return;
		} 
	
		job.setScheduledDayOfWeek(dow);
	}
	
	private void setScheduledDayOfMonth(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		if (job.getRepeatSchedule() != RepeatSchedule.MONTHLY) {
			return;
		}
		
		Integer dayOfMonth = detail.getScheduledDayOfMonth();
		
		if (dayOfMonth == null || dayOfMonth < 1 || dayOfMonth > 31) {
			ose.addError(ScheduledJobErrorCode.INVALID_SCHEDULED_TIME);
			return;	
		}
	
		job.setScheduledDayOfMonth(dayOfMonth);
	}
	
	private void setType(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		Type type = null;
		try {
			type = Type.valueOf(detail.getType());
		} catch (Exception e) {
			ose.addError(ScheduledJobErrorCode.INVALID_TYPE);
			return;
		}
		
		job.setType(type);
		
		if (type == Type.EXTERNAL) {
			setCommand(detail, job, ose);
		} else {
			setTaskImplFqn(detail, job, ose);
		}
	}
	
	private void setCommand(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		String command = detail.getCommand();
		if (StringUtils.isBlank(command)) {
			ose.addError(ScheduledJobErrorCode.EXTERNAL_COMMAND_REQUIRED);
			return;
		}
		
		job.setCommand(command);
	}
	
	private void setTaskImplFqn(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		String fqn = detail.getTaskImplFqn();
		if (StringUtils.isBlank(fqn)) {
			ose.addError(ScheduledJobErrorCode.TASK_IMPL_FQN_REQUIRED);
			return;
		}
		
		try {
			Class.forName(fqn);
		} catch (Exception e) {
			ose.addError(ScheduledJobErrorCode.INVALID_TASK_IMPL_FQN);
			return;
		}
		
		job.setTaskImplfqn(fqn);
	}
	
	private void setRecipients(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		if (CollectionUtils.isEmpty(detail.getRecipients())) {
			return; 
		}
		
		List<Long> userIds = new ArrayList<Long>();
		for (UserSummary user : detail.getRecipients()) {
			userIds.add(user.getId());
		}
		
		List<User> recipients = daoFactory.getUserDao().getUsersByIds(userIds);
		if (recipients.size() != userIds.size()) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		job.setRecipients(new HashSet<User>(recipients));
	}
	
	private User getUser(UserSummary userSummary) {
		User user = null;
		Long userId = null;
		String loginName = null;
		String domain = null;

		if (userSummary != null) {
			userId = userSummary.getId();
			loginName = userSummary.getLoginName();
			domain = userSummary.getDomain();
		}

		if (userId != null) {
			user = daoFactory.getUserDao().getById(userSummary.getId());
		} else if (StringUtils.isNotBlank(loginName) && StringUtils.isNotBlank(domain)) {
			user = daoFactory.getUserDao().getUser(userSummary.getLoginName(),	userSummary.getDomain());
		}

		return user;
	}
}
