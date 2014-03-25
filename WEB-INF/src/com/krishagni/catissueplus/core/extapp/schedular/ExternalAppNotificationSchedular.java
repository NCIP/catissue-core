
package com.krishagni.catissueplus.core.extapp.schedular;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.common.CaTissueAppContext;
import com.krishagni.catissueplus.core.extapp.services.ExternalAppNotificationService;

import edu.wustl.common.util.XMLPropertyHandler;

public class ExternalAppNotificationSchedular implements Runnable {

	private static final String SCH_TIME_INTERVAL = "extAppSchTimeIntervalInMinutes";

	public static void scheduleExtAppNotifSchedulerJob() throws Exception {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(100);
		int schTimeInterval = Integer.parseInt(XMLPropertyHandler.getValue(SCH_TIME_INTERVAL).trim());
		executor.scheduleWithFixedDelay(new ExternalAppNotificationSchedular(), 0, schTimeInterval, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		ApplicationContext caTissueContext = CaTissueAppContext.getInstance();
		ExternalAppNotificationService extApp = (ExternalAppNotificationService) caTissueContext
				.getBean("extAppNotificationService");
		extApp.notificationService();

	}

}
