
package edu.wustl.catissuecore.printserviceclient;

import java.util.List;

import edu.wustl.common.domain.AbstractDomainObject;
import gov.nih.nci.security.authorization.domainobjects.User;

/**
 * This Interface is used to declare method for label printing.
 * @author falguni_sachde
 */
public interface LabelPrinter
{

	/**
	 * This method has implementation for printing  AbstractDomainObject .
	 * @param domainObject This is the object to be print
	 * @param ipAddress This is the IP address of calling application.
	 * @param objUser This is the User object of the current logged in User.
	 * @param printerType printer Type
	 * @param printerLoc printer Lock.
	 * @return boolean
	 */
	boolean printLabel(AbstractDomainObject domainObject, String ipAddress, User objUser,
			String printerType, String printerLoc);

	/**
	 * This method has implementation for printing  list of AbstractDomainObject .
	 * @param domainObject This is the List of objects to be print
	 * @param ipAddress This is the IP address of calling application.
	 * @param userObj This is the User object of the current logged in User.
	 * @param printerType printer Type
	 * @param printerLoc printer Lock.
	 * @return boolean
	 */
	boolean printLabel(List<AbstractDomainObject> domainObject, String ipAddress, User userObj,
			String printerType, String printerLoc);
}
