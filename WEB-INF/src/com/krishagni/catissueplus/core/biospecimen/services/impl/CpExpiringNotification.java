
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.biospecimen.ConfigParams;

@Configurable
public class CpExpiringNotification implements ScheduledTask {
	
	private static final String CP_EXPIRING_NOTIFICATION_TMPL = "cp_expiring_notification";
	
	@Autowired
	private DaoFactory daoFactory;
	
	@Autowired
	private EmailService emailService;
	
	@Override
	@PlusTransactional
	public void doJob(ScheduledJobRun jobRun) throws Exception {
		Date intervalStartDate = getIntervalStartDate();
		int notificationDays = getNotificationDays();
		Date intervalEndDate = getIntervalEndDate(intervalStartDate, notificationDays);
		int repeatInterval = getRepeatInterval();
		
		List<CollectionProtocol> cps = daoFactory.getCollectionProtocolDao()
			.getExpiringCps(intervalStartDate, intervalEndDate);
		for (CollectionProtocol cp : cps) {
		  if (isEligibleForReminder(cp, intervalStartDate, notificationDays, repeatInterval)) {
		    sendExpiryReminder(cp);
		  }
		}
	}
	
	private Date getIntervalStartDate() {
		return Utility.chopTime(new Date());
	}
	
	private int getNotificationDays() {
		return ConfigUtil.getInstance().getIntSetting(ConfigParams.MODULE, ConfigParams.CP_EXPIRY_REM_NOTIF, 0);
	}

	private Date getIntervalEndDate(Date intervalStartDate, int notificationDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(intervalStartDate);

		cal.add(Calendar.DATE, notificationDays);

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}
	
	private int getRepeatInterval() {
		return ConfigUtil.getInstance().getIntSetting(ConfigParams.MODULE, ConfigParams.CP_EXPIRY_REM_REPT_INTER, 0);
	}
	
	private Boolean isEligibleForReminder(CollectionProtocol cp, Date intervalStartDate,
			int notificationDays, int repeatInterval) {

		long daysBeforeExpiry = Utility.daysBetween(intervalStartDate, Utility.chopTime(cp.getEndDate()));
		return daysBeforeExpiry == 0 || (notificationDays - daysBeforeExpiry) % repeatInterval == 0;
	}
	
	private void sendExpiryReminder(CollectionProtocol cp) {
		Map<String, Object> emailProps = new HashMap<String, Object>();
		emailProps.put("$subject", new String[] {cp.getShortTitle()});
		emailProps.put("cp", cp);
		String[] rcpts = {cp.getPrincipalInvestigator().getEmailAddress()};
		emailService.sendEmail(CP_EXPIRING_NOTIFICATION_TMPL, rcpts, emailProps);
	}
}
