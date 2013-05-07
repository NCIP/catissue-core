
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
