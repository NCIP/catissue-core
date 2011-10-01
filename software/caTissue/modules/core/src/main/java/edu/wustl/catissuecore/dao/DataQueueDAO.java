package edu.wustl.catissuecore.dao;

import java.util.List;

import edu.wustl.catissuecore.domain.ccts.DataQueue;

/**
 * @author Denis G. Krylov
 * 
 */
public interface DataQueueDAO extends GenericDAO {

	/**
	 * Returns the number of pending items in the data queue.
	 * 
	 * @return
	 */
	int getPendingIncomingDataQueueItemsCount();

	/**
	 * Picks a {@link DataQueue} that is next in line for processing. Selected
	 * {@link DataQueue} is the oldest one that is in PENDING state.
	 * 
	 * @return
	 */
	DataQueue pickIncomingDataQueueItemForProcessing();
	
	/**
	 * Returns all pending items that point to the given Grid ID.
	 * @param gridId
	 * @return
	 */
	List<DataQueue> getPendingDataQueueItems(String gridId);

}