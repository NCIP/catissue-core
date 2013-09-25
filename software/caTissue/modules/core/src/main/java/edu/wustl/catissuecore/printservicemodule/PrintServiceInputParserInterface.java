/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


package edu.wustl.catissuecore.printservicemodule;

/**
 * This Interface is the base interface for Print service.
 * Any Class which implements this interface will provide method to print object.
 * @author falguni_sachde
 *
 */
public interface PrintServiceInputParserInterface
{

	/**
	* @param object Object for which label printing  will be applied
	* @return boolean Status
	* @throws Exception Exception
	*/
	boolean callPrintService(Object object) throws Exception;

}
