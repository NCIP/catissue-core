/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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