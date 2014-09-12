
package com.krishagni.catissueplus.core.notification.schedular;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.notification.services.ExternalAppNotificationService;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

public class ExternalAppNotificationSchedular implements Runnable {

	private static final Logger LOGGER = Logger.getCommonLogger(ExternalAppNotificationSchedular.class);

	private static final String NOTIFICATION_EXCEPTION = "Error while Notifiying External Applications :";

	private static final String SCH_TIME_INTERVAL = "extApp.sch.time.interval.in.minutes";

	public static void scheduleExtAppNotifSchedulerJob() throws Exception {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(100);
		int schTimeInterval = 60;
		String schTimeIntervalProperty = XMLPropertyHandler.getValue(SCH_TIME_INTERVAL).trim();
		if (!CommonValidator.isBlank(schTimeIntervalProperty)) {
			schTimeInterval = Integer.parseInt(schTimeIntervalProperty);
		}
		executor.scheduleWithFixedDelay(new ExternalAppNotificationSchedular(), 0, schTimeInterval, TimeUnit.MINUTES);
	}

	@Override
	public void run() {
		try {
			ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
			ExternalAppNotificationService extApp = (ExternalAppNotificationService) caTissueContext
					.getBean("extAppNotificationService");
			extApp.notifyExternalApps();
		}
		catch (Exception ex) {
			LOGGER.error(NOTIFICATION_EXCEPTION + ex.getMessage());
		}

	}

}
