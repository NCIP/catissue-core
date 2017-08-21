
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

@Configurable
public class DpExpiringNotification implements ScheduledTask {
	
	private static final String DP_EXPIRING_NOTIFICATION_TMPL = "dp_expiring_notification";
	
	private static final String ADMINISTRATIVE_MODULE = "administrative";
	
	private static final String DP_EXPIRY_REM_NOTIF = "dp_expiry_rem_notif";
	
	private static final String DP_EXPIRY_REM_REPT_INTER = "dp_expiry_rem_rept_inter";
	
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
		
		daoFactory.getDistributionProtocolDao().getExpiringDps(intervalStartDate, intervalEndDate).stream()
			.filter(dp -> isEligibleForReminder(dp, intervalStartDate, notificationDays, repeatInterval))
			.forEach(this::sendExpiryReminder);
	}
	
	private Date getIntervalStartDate() {
		return Utility.chopTime(new Date());
	}
	
	private int getNotificationDays() {
		return ConfigUtil.getInstance().getIntSetting(ADMINISTRATIVE_MODULE, DP_EXPIRY_REM_NOTIF, 0);
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
		return ConfigUtil.getInstance().getIntSetting(ADMINISTRATIVE_MODULE, DP_EXPIRY_REM_REPT_INTER, 0);
	}
	
	private Boolean isEligibleForReminder(DistributionProtocol dp, Date intervalStartDate,
			int notificationDays, int repeatInterval) {

		long daysBeforeExpiry = Utility.daysBetween(intervalStartDate, Utility.chopTime(dp.getEndDate()));
		return daysBeforeExpiry == 0 || (notificationDays - daysBeforeExpiry) % repeatInterval == 0;
	}
	
	private void sendExpiryReminder(DistributionProtocol dp) {
		Map<String, Object> emailProps = new HashMap<String, Object>();
		emailProps.put("$subject", new String[] {dp.getShortTitle()});
		emailProps.put("dp", dp);
		emailService.sendEmail(DP_EXPIRING_NOTIFICATION_TMPL, getReceipients(dp), emailProps);
	}

	private String[] getReceipients(DistributionProtocol dp) {
		List<String> rcpts = dp.getCoordinators().stream()
			.map(user -> user.getEmailAddress())
			.collect(Collectors.toList());
		rcpts.add(0, dp.getPrincipalInvestigator().getEmailAddress());
		return rcpts.toArray(new String[0]);
	}

}
