package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJob.RepeatSchedule;
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

		job.setId(detail.getId());
		setName(detail,job,ose);
		setStartAndEndDates(detail, job, ose);
		setCreatedBy(detail, job, ose);
		setActivityStatus(detail, job, ose);
		setRepeatSchedule(detail, job, ose);
		setType(detail, job, ose);
		setCommand(detail, job, ose);
		setRecipients(detail, job, ose);
		job.setComments(detail.getComments());
		
		ose.checkAndThrow();
		return job;
	}
	
	private void setName(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		String name = detail == null ? null : detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(ScheduledJobErrorCode.NAME_REQUIRED);
			return;
		}
		
		job.setName(name);
	}

	private void setStartAndEndDates(ScheduledJobDetail detail,	ScheduledJob job, OpenSpecimenException ose) {
		if (detail.getStartDate() == null) {
			ose.addError(ScheduledJobErrorCode.START_DATE_REQUIRED);
			return;
		}
		
		if (detail.getEndDate() != null && detail.getEndDate().before(detail.getEndDate())) {
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
	
    private void setActivityStatus(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
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
                user = daoFactory.getUserDao().getUser(userSummary.getLoginName(), userSummary.getDomain());
        }

        return user;
    }
    
	private void setRepeatSchedule(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		String repeatSchedule = detail == null ? null : detail.getRepeatSchedule();
		if (repeatSchedule == null) {
			ose.addError(ScheduledJobErrorCode.INVALID_REPEAT_SCHEDULE);
			return;
		}
		
		RepeatSchedule repeat = null;
		try {
			repeat = RepeatSchedule.valueOf(detail.getRepeatSchedule());
		} catch (IllegalArgumentException ile) {
            ose.addError(ScheduledJobErrorCode.INVALID_REPEAT_SCHEDULE);
            return;
		}
		
		job.setRepeatSchedule(repeat);
	}
	
	private void setType(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		job.setType(detail.getType());
	}
	
	private void setCommand(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		job.setCommand(detail.getCommand());
	}
	
	private void setRecipients(ScheduledJobDetail detail, ScheduledJob job, OpenSpecimenException ose) {
		for (String recipient : detail.getRecipients()) {
			if (StringUtils.isEmpty(recipient)) {
				ose.addError(ScheduledJobErrorCode.INVALID_RECIPIENT);
				return;
			}
		}
		
		job.setRecipients(detail.getRecipients());
	}
}
