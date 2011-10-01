package edu.wustl.catissuecore.bizlogic.ccts;

import java.util.Collection;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ccts.DataQueue;
import edu.wustl.catissuecore.domain.ccts.Notification;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.lookup.DefaultLookupResult;

/**
 * @author Denis G. Krylov
 * 
 */
public interface ICctsIntegrationBizLogic {

	/**
	 * Returns a {@link Collection} of {@link Notification} objects ordered by
	 * timestamp (descending) and ID. No filtering is applied; all notifications
	 * are returned within the given query limits.
	 * 
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	Collection<Notification> getNotifications(int firstResult, int maxResults);

	/**
	 * Returns the number of {@link Notification} stored in the queue,
	 * regardless of their processing status.
	 * 
	 * @return
	 */
	int getNotificationCount();

	/**
	 * Gets {@link Notification} by id.
	 * 
	 * @param msgId
	 * @return
	 */
	public abstract Notification getNotificationById(long msgId);

	/**
	 * Picks a {@link Notification} from the queue for processing. Selected
	 * {@link Notification} will be the one with an oldest receipt date and
	 * having either PENDING or PROCESSING status. If the {@link Notification}
	 * is PENDING, its status will be automatically changed to PROCESSING.
	 * 
	 * @return
	 */
	public Notification pickNotificationForProcessing();

	void updateNotification(Notification notification);

	/**
	 * Saves a {@link DataQueue} instance in the database.
	 * 
	 * @param dataQueueItem
	 */
	void addDataQueueItem(DataQueue dataQueueItem);

	/**
	 * Returns the number of pending items in the data queue.
	 * 
	 * @return
	 */
	int getPendingDataQueueItemsCount();

	/**
	 * Picks a {@link DataQueue} that is next in line for processing. Selected
	 * {@link DataQueue} is the oldest one that is in PENDING state. Returned
	 * {@link DataQueue} will be linked to a domain object, such as
	 * {@link Participant}, if such object exists.
	 * 
	 * @return
	 */
	DataQueue pickDataQueueItemForProcessing();

	/**
	 * Attempts to link the given {@link DataQueue} item to a domain object
	 * instance, such as {@link Participant}, by searching for a domain object
	 * using GRID ID contained in the {@link DataQueue}.
	 */
	void linkQueueItemToDomainObject(DataQueue item);

	List<IDomainObjectComparisonResult> getParticipantComparison(
			DataQueue queueItem, IErrorsReporter errorsReporter);

	/**
	 * Rejects the given {@link DataQueue} item.
	 * 
	 * @param item
	 */
	void rejectQueueItem(DataQueue item);

	void acceptQueueItem(DataQueue queueItem, String userName)
			throws BizLogicException;

	/**
	 * Adds the given {@link DataQueue} item. Looks for previous
	 * {@link DataQueue} items that are PENDING and have the same Grid ID and
	 * expires them by setting their status to REJECTED. This is done to help
	 * administrator avoid dealing with duplicate and obsolete data queue items.
	 * Then it attempts to auto-process the items in the queue via
	 * {@link #autoProcessDataQueue()}.
	 * 
	 * @param dataQueue
	 */
	void addDataQueueItemWithAutoProcess(DataQueue dataQueue);

	List<DefaultLookupResult> getParticipantMatchingResults(
			DataQueue queueItem, IErrorsReporter errorsReporter);

	void acceptQueueItem(DataQueue queueItem, String userName,
			Participant participant) throws BizLogicException;

	public boolean isEligibleForAutoProcessing(DataQueue queueItem);

	/**
	 * Attempts to automatically process {@link DataQueue} items that are
	 * eligible, i.e. do not require user intervention.
	 * 
	 * @throws BizLogicException
	 */
	public void autoProcessDataQueue() throws BizLogicException;

	public CollectionProtocolRegistration convertRegistration(
			DataQueue queueItem, final IErrorsReporter iErrorsReporter);

	List<IDomainObjectComparisonResult> getRegistrationComparison(
			DataQueue queueItem, IErrorsReporter errorsReporter);

	/**
	 * Checks whether a user is authorized to access information represented by
	 * the given {@link DataQueue} item.
	 * 
	 * @param sessionDataBean
	 * @param queueItem
	 * @return
	 * @throws BizLogicException
	 */
	boolean isAuthorized(SessionDataBean sessionDataBean, DataQueue queueItem)
			throws BizLogicException;

	/**
	 * Checks whether a user is authorized to access information represented by
	 * {@link DataQueue} in the beginning of the queue.
	 * 
	 * @param sessionDataBean
	 * @param queueItem
	 * @return
	 * @throws BizLogicException
	 */
	boolean isAuthorized(SessionDataBean sessionDataBean)
			throws BizLogicException;

	/**
	 * Transfers the given {@link CollectionProtocolRegistration} presumably to
	 * C3PR using the Registration service.
	 * 
	 * @param cpr
	 */
	public abstract void enrollStudySubject(CollectionProtocolRegistration cpr);

	/**
	 * Determines whether the given protocol registration needs to be submitted
	 * to the Registration service, and if it does, submits it.
	 * 
	 * @param entity CollectionProtocolRegistration
	 * @param oldState a snapshot of the registration's previous state as it existed prior to update. 
	 * @param newState a snapshot of the registration's current state as it exists now.
	 */
	void sendToRegistrationService(CollectionProtocolRegistration entity,
			Object[] oldState, Object[] newState);

}