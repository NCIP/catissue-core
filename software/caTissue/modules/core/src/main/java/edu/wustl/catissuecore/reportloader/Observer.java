/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


package edu.wustl.catissuecore.reportloader;

/**
 * @author sandeep_ranade
 * This interface represents observer
 */

public interface Observer
{

	/**
	 * @param obj object
	 */
	void notifyEvent(Object obj);
}
