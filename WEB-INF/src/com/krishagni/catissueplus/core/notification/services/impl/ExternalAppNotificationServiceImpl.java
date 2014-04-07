
package com.krishagni.catissueplus.core.notification.services.impl;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.audit.domain.Audit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.common.CaTissueAppContext;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.util.ObjectType;
import com.krishagni.catissueplus.core.common.util.Operation;
import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;
import com.krishagni.catissueplus.core.notification.domain.ExternalApplication;
import com.krishagni.catissueplus.core.notification.events.NotificationDetails;
import com.krishagni.catissueplus.core.notification.services.ExternalAppNotificationService;
import com.krishagni.catissueplus.core.notification.services.ExternalAppService;
import com.krishagni.catissueplus.core.notification.util.ExternalApplications;

import edu.wustl.common.util.logger.Logger;

@Service
public class ExternalAppNotificationServiceImpl implements ExternalAppNotificationService {

	private static final Logger LOGGER = Logger.getCommonLogger(ExternalAppNotificationServiceImpl.class);

	private static final String NOTIFICATION_EXCEPTION = "Exception while notifying External Application :";

	public enum NotificationStatus {
		NEW, PROCESSED, FAIL
	};

	public static final String SUCCESS = "Success";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	/**
	 * This method retrives all notification objects & notify each external
	 * application.
	 */
	@Override
	public void notifyExternalApps() {
		ApplicationContext caTissueContext = CaTissueAppContext.getInstance();
		ExternalAppNotificationService extAppNotifSvc = (ExternalAppNotificationService) caTissueContext
				.getBean("extAppNotificationService");
		List<NotificationDetails> notificationObjects = extAppNotifSvc.getNotificationObjects();
		for (NotificationDetails notifEvent : notificationObjects) {
			extAppNotifSvc.notifyExternalApps(notifEvent);
		}
	}

	@Override
	public void notifyFailedNotifications() {
		ApplicationContext caTissueContext = CaTissueAppContext.getInstance();
		ExternalAppNotificationService extAppNotifSvc = (ExternalAppNotificationService) caTissueContext
				.getBean("extAppNotificationService");
		List<ExtAppNotificationStatus> failNotificationObjects = extAppNotifSvc.getFailedNotificationObjects();
		for (ExtAppNotificationStatus failNotification : failNotificationObjects) {
			extAppNotifSvc.notifyFailedNotifications(failNotification);
		}
	}

	@Override
	@PlusTransactional
	public void notifyFailedNotifications(ExtAppNotificationStatus failNotification) {
		try {
			NotificationDetails notifDto = new NotificationDetails();
			notifDto.setAuditId(failNotification.getAudit().getId());
			notifDto.setObjectId(failNotification.getAudit().getObjectId());
			notifDto.setObjectType(ObjectType.valueOf(failNotification.getAudit().getObjectType()));
			notifDto.setOperation(Operation.valueOf(failNotification.getAudit().getOperation()));

			failNotification.setStatus(notify(notifDto, failNotification.getExternalApplication()));

			daoFactory.getExternalAppNotificationDao().saveOrUpdate(failNotification);
		}
		catch (Exception ex) {

			LOGGER.error(NOTIFICATION_EXCEPTION + failNotification.getAudit().getObjectType() + ex.getMessage());
		}
	}

	@Override
	@PlusTransactional
	public void notifyExternalApps(NotificationDetails notifDto) {
		try {
			ApplicationContext caTissueContext = CaTissueAppContext.getInstance();
			ExternalApplications extAppsBean = (ExternalApplications) caTissueContext.getBean("externalApplications");
			List<ExternalApplication> extApps = extAppsBean.getAllExternalApplications();

			for (ExternalApplication externalApplication : extApps) {
				String status = notify(notifDto, externalApplication);
				ExtAppNotificationStatus extAppNotif = new ExtAppNotificationStatus();
				Audit audit = new Audit();
				audit.setId(notifDto.getAuditId());
				extAppNotif.setAudit(audit);
				extAppNotif.setExternalApplication(externalApplication);
				extAppNotif.setStatus(status);
				daoFactory.getExternalAppNotificationDao().saveOrUpdate(extAppNotif);

			}

		}
		catch (Exception ex) {
			LOGGER.error(NOTIFICATION_EXCEPTION + notifDto.getObjectType() + ex.getMessage());
		}

	}

	private String notify(NotificationDetails notifDto, ExternalApplication externalApplication)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Object domainObj = getDomainObject(notifDto.getObjectType(), notifDto.getObjectId());
		Class<?> externalAppClass;
		externalAppClass = Class.forName(externalApplication.getServiceClass());
		ExternalAppService extApplication = (ExternalAppService) externalAppClass.newInstance();

		String result = null;
		String status = null;
		switch (notifDto.getOperation()) {
			case INSERT :
				result = extApplication.notifyInsert(notifDto.getObjectType(), domainObj);
				break;
			case UPDATE :
				result = extApplication.notifyUpdate(notifDto.getObjectType(), domainObj);
				break;
		}
		if (isSuccess(result)) {
			status = NotificationStatus.PROCESSED.toString();
		}
		else {
			status = NotificationStatus.FAIL.toString();
		}
		return status;
	}

	private boolean isSuccess(String result) {
		boolean isSuccess = false;
		if (result != null && SUCCESS.toString().equalsIgnoreCase(result)) {
			isSuccess = true;
		}
		return isSuccess;
	}

	@Override
	@PlusTransactional
	public List<NotificationDetails> getNotificationObjects() {
		return daoFactory.getExternalAppNotificationDao().getNotificationObjects();
	}

	@Override
	@PlusTransactional
	public List<ExtAppNotificationStatus> getFailedNotificationObjects() {
		return daoFactory.getExternalAppNotificationDao().getFailedNotificationObjects();
	}

	private Object getDomainObject(ObjectType objectType, Long objectId) {
		Object object = null;
		switch (objectType) {
			case PARTICIPANT :
				ParticipantDao dao = daoFactory.getParticipantDao();
				object = dao.getParticipant(objectId);
				break;
		}
		return object;
	}

}