package edu.wustl.catissuecore.reportloader;
/**
 * @author sandeep_ranade
 * This is observable interface 
 */

public interface Observable
{
	/**
	 * @param obr
	 * Method to register the object with observer
	 */
	void register(Observer  obr);
	
}
