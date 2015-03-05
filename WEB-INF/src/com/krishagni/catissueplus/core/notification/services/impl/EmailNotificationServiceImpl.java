
package com.krishagni.catissueplus.core.notification.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.VelocityClassLoaderManager;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;
import com.krishagni.catissueplus.core.notification.schedular.ExternalAppNotificationSchedular;
import com.krishagni.catissueplus.core.notification.services.EmailNotificationService;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

public class EmailNotificationServiceImpl implements EmailNotificationService {

	private static final Logger LOGGER = Logger.getCommonLogger(ExternalAppNotificationSchedular.class);

	private static String FAILED_NOTIFICATION_EMAIL_TMPL = "failed_notification";

	private static final String KEY_EMAIL_ADMIN_EMAIL_ADDRESS = "email.administrative.emailAddress";

	private static final String adminEmailAddress = XMLPropertyHandler.getValue(KEY_EMAIL_ADMIN_EMAIL_ADDRESS);

	private static final String FAILED_NOTIFICATION_CSV_TMPL_FILE_PATH = "../ng-file-templates/failed_notification_csv.vm";

	private static final String FILE_NAME = "FailedNotifications_";

	private static final String FILE_EXTENTION_CSV = ".csv";
	
	private DaoFactory daoFactory;
	
	private EmailService emailService;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> sendFailedNotificationReport() {
		try {
			List<ExtAppNotificationStatus> expiredNotifications = daoFactory.getExternalAppNotificationDao()
					.getExpiredNotificationObjects();
			if (expiredNotifications != null && !expiredNotifications.isEmpty()) {
				File reportFile = getExpiredNotificationReportFile(expiredNotifications);
				File[] attachements = new File[] {reportFile};
				
				Map<String, Object> props = new HashMap<String, Object>();
				emailService.sendEmail(FAILED_NOTIFICATION_EMAIL_TMPL, new String[]{adminEmailAddress}, attachements, props);
				reportFile.delete();
			}
			
			return ResponseEvent.response(true);
		} catch (Exception ex) {
			return ResponseEvent.serverError(ex);
		}
	}

	private File getExpiredNotificationReportFile(List<ExtAppNotificationStatus> expiredNotifications) throws Exception,
			IOException, FileNotFoundException {
		Map<String, Object> failedNotificationDetails = new HashMap<String, Object>();
		failedNotificationDetails.put("failedNotifications", expiredNotifications);
		String filedata = null;
		filedata = VelocityClassLoaderManager.getInstance().evaluate(failedNotificationDetails,
				FAILED_NOTIFICATION_CSV_TMPL_FILE_PATH);
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String currentDate = dateFormat.format(new Date());
		String filename = FILE_NAME + currentDate + FILE_EXTENTION_CSV;
		File file = new File(filename);
		file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		out.write(filedata.getBytes());
		out.close();
		return file;
	}
}