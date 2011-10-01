package edu.wustl.catissuecore.bizlogic.ccts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;

import edu.wustl.catissuecore.domain.ccts.DataQueue;
import edu.wustl.catissuecore.domain.ccts.EventType;
import edu.wustl.catissuecore.domain.ccts.Notification;
import edu.wustl.catissuecore.domain.ccts.ProcessingStatus;
import edu.wustl.common.util.logger.Logger;

/**
 * This class processes {@link Notification}s received from other applications.
 * For every notification in the queue, it determines an application that
 * originated the event and invokes its web services to obtain business payload.
 * Payload is stored in the {@link DataQueue} for further review by
 * administrators. <br>
 * <br>
 * Notifications are processed in the order they were received and in
 * non-concurrent manner. Please note: <b>non-concurrency rule is enforced by
 * Quartz configuration, and not here.</b> This class is coded under this
 * assumption.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class NotificationQueueProcessor {

	private static final Logger logger = Logger
			.getCommonLogger(NotificationQueueProcessor.class);

	private ICctsIntegrationBizLogic bizLogic;

	private List<ICctsEventProcessor> cctsEventProcessors = new ArrayList<ICctsEventProcessor>();

	/**
	 * Time in seconds. Processing of a notification will be attempted only for
	 * the given period starting from notification's receipt date.
	 */
	private int notificationTTL = 864000;

	/**
	 * Delay in seconds between attempts to process a notification.
	 */
	private int delayBetweenAttempts = 5;

	private boolean enabled = true;

	public void process() {
		if (isEnabled())
			try {
				Utils.restoreDefaultHttpsURLHandler();
				processInternal();
			} catch (Exception e) {
				logger.error(e.toString(), e);
			}
	}

	/**
	 * 
	 */
	private void processInternal() {
		Notification notification = bizLogic.pickNotificationForProcessing();
		if (notification != null) {
			logger.debug("Picked a CCTS notification for processing: "
					+ notification);
			Date lastAttemptDate = notification
					.getDateOfLastProcessingAttempt();
			if (lastAttemptDate == null
					|| new Date().after(DateUtils.addSeconds(lastAttemptDate,
							delayBetweenAttempts))) {
				Date receiptDate = notification.getDateReceived();
				if (!new Date().after(DateUtils.addSeconds(receiptDate,
						notificationTTL))) {
					process(notification);
				} else {
					logger.info("Notification has expired and will not be processed:"
							+ notification);
					expire(notification);
				}
			} else {
				logger.debug("DelayBetweenAttempts not reached; too early to process.");
			}
		} else {
			logger.debug("No CCTS notifications to process.");
		}
	}

	private void expire(Notification notification) {
		notification.setProcessingStatus(ProcessingStatus.CANCELLED);
		bizLogic.updateNotification(notification);
	}

	private void process(Notification notification) {
		EventType eventType = notification.getEventType();
		String gridId = notification.getObjectIdValue();
		ICctsEventProcessor processor = findSuitableProcessor(eventType);
		if (processor == null) {
			expire(notification);
			final String errMsg = "No processor found for event type: "
					+ eventType;
			logger.error(errMsg);
			throw new RuntimeException(errMsg);
		}

		try {
			String payload = processor.process(gridId, notification);
			notification.addSuccess(payload);
			notification.setProcessingStatus(ProcessingStatus.COMPLETED);
			bizLogic.updateNotification(notification);
			logger.debug("Notification successfully processed. Payload: "
					+ payload);
		} catch (ProcessingException e) {
			logger.error(e.toString(), e);
			notification.addFailure(e.getMessage() + ". ROOT CAUSE: "
					+ ExceptionUtils.getRootCauseMessage(e)
					+ ". FULL STACK TRACE: "
					+ ExceptionUtils.getFullStackTrace(e));
			bizLogic.updateNotification(notification);
		}

	}

	private ICctsEventProcessor findSuitableProcessor(EventType eventType) {
		for (ICctsEventProcessor processor : cctsEventProcessors) {
			if (processor.supports(eventType)) {
				return processor;
			}
		}
		return null;
	}

	/**
	 * @return the bizLogic
	 */
	public final ICctsIntegrationBizLogic getBizLogic() {
		return bizLogic;
	}

	/**
	 * @param bizLogic
	 *            the bizLogic to set
	 */
	public final void setBizLogic(ICctsIntegrationBizLogic bizLogic) {
		this.bizLogic = bizLogic;
	}

	/**
	 * @return the notificationTTL
	 */
	public final int getNotificationTTL() {
		return notificationTTL;
	}

	/**
	 * @param notificationTTL
	 *            the notificationTTL to set
	 */
	public final void setNotificationTTL(int notificationTTL) {
		this.notificationTTL = notificationTTL;
	}

	/**
	 * @return the delayBetweenAttempts
	 */
	public final int getDelayBetweenAttempts() {
		return delayBetweenAttempts;
	}

	/**
	 * @param delayBetweenAttempts
	 *            the delayBetweenAttempts to set
	 */
	public final void setDelayBetweenAttempts(int delayBetweenAttempts) {
		this.delayBetweenAttempts = delayBetweenAttempts;
	}

	/**
	 * @return the cctsEventProcessors
	 */
	public final List<ICctsEventProcessor> getCctsEventProcessors() {
		return cctsEventProcessors;
	}

	/**
	 * @param cctsEventProcessors
	 *            the cctsEventProcessors to set
	 */
	public final void setCctsEventProcessors(
			List<ICctsEventProcessor> cctsEventProcessors) {
		this.cctsEventProcessors = cctsEventProcessors;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
