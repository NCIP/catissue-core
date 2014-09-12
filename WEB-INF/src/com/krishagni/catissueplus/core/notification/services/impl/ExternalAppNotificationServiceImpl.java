
package com.krishagni.catissueplus.core.notification.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.audit.domain.Audit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.ParticipantDao;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.util.ObjectType;
import com.krishagni.catissueplus.core.common.util.Operation;
import com.krishagni.catissueplus.core.notification.domain.ExtAppNotificationStatus;
import com.krishagni.catissueplus.core.notification.domain.ExternalApplication;
import com.krishagni.catissueplus.core.notification.events.NotificationDetails;
import com.krishagni.catissueplus.core.notification.events.NotificationResponse;
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
		ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
		ExternalAppNotificationService extAppNotifSvc = (ExternalAppNotificationService) caTissueContext
				.getBean("extAppNotificationService");
		List<NotificationDetails> notificationObjects = extAppNotifSvc.getNotificationObjects();
		for (NotificationDetails notifEvent : notificationObjects) {
			extAppNotifSvc.notifyExternalApps(notifEvent);
		}
	}

	@Override
	public void notifyFailedNotifications() {
		ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
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
			NotificationDetails notifDetail = new NotificationDetails();
			notifDetail.setAuditId(failNotification.getAudit().getId());
			notifDetail.setObjectId(failNotification.getAudit().getObjectId());
			notifDetail.setObjectType(ObjectType.valueOf(failNotification.getAudit().getObjectType()));
			notifDetail.setOperation(Operation.valueOf(failNotification.getAudit().getOperation()));

			NotificationResponse notificationResponse = notify(notifDetail, failNotification.getExternalApplication());

			failNotification.setStatus(notificationResponse.getStatus().toString());
			failNotification.setComments(notificationResponse.getMessage());
			failNotification.setUpdatedDate(new Date());
			daoFactory.getExternalAppNotificationDao().saveOrUpdate(failNotification);
		}
		catch (Exception ex) {

			LOGGER.error(NOTIFICATION_EXCEPTION + failNotification.getAudit().getObjectType() + ex.getMessage());
		}
	}

	@Override
	@PlusTransactional
	public void notifyExternalApps(NotificationDetails notifDetail) {
		try {
			ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
			ExternalApplications extAppsBean = (ExternalApplications) caTissueContext.getBean("externalApplications");
			List<ExternalApplication> extApps = extAppsBean.getAllExternalApplications();

			for (ExternalApplication externalApplication : extApps) {
				NotificationResponse notificationResponse = notify(notifDetail, externalApplication);
				ExtAppNotificationStatus status = new ExtAppNotificationStatus();
				Audit audit = new Audit();
				audit.setId(notifDetail.getAuditId());
				status.setAudit(audit);
				status.setExternalApplication(externalApplication);
				status.setStatus(notificationResponse.getStatus().toString());
				status.setComments(notificationResponse.getMessage());
				status.setUpdatedDate(new Date());
				daoFactory.getExternalAppNotificationDao().saveOrUpdate(status);
			}

		}
		catch (Exception ex) {
			LOGGER.error(NOTIFICATION_EXCEPTION + notifDetail.getObjectType() + ex.getMessage());
		}

	}

	private NotificationResponse notify(NotificationDetails notifDetail, ExternalApplication externalApplication)
			throws Exception {
		Object domainObj = getDomainObject(notifDetail.getObjectType(), notifDetail.getObjectId());
		Class<?> externalAppClass;
		externalAppClass = Class.forName(externalApplication.getServiceClass());
		ExternalAppService extApplication = (ExternalAppService) externalAppClass.newInstance();

		NotificationResponse result = null;
		switch (notifDetail.getOperation()) {
			case INSERT :
				result = extApplication.notifyInsert(notifDetail.getObjectType(), domainObj);
				break;
			case UPDATE :
				result = extApplication.notifyUpdate(notifDetail.getObjectType(), domainObj);
				break;
			case DELETE :
				result = extApplication.notifyDelete(notifDetail.getObjectType(), domainObj);
				break;
		}

		return result;
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