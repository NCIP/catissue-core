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
 * This is observable interface.
 * @author sandeep_ranade
 */

public interface Observable
{

	/**
	 * Method to register the object with observer.
	 * @param obr object to register
	 */
	void register(Observer obr);
}
