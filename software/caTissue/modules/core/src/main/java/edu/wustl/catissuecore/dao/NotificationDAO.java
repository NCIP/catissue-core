package edu.wustl.catissuecore.dao;

import java.util.Collection;

import edu.wustl.catissuecore.domain.ccts.Notification;

/**
 * @author Denis G. Krylov
 * 
 */
public interface NotificationDAO extends GenericDAO {

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

	public Notification getById(long id);

	/**
	 * Picks a {@link Notification} from the queue for processing. Selected
	 * {@link Notification} will be the one with an oldest receipt date and
	 * having either PENDING or PROCESSING status. If the {@link Notification}
	 * is PENDING, its status will be automatically changed to PROCESSING.
	 * 
	 * @return
	 */
	public Notification pickNotificationForProcessing();

}