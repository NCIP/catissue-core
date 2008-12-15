package edu.wustl.catissuecore.printserviceclient;

import java.util.List;

import gov.nih.nci.security.authorization.domainobjects.User;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * This Interface is used to declare method for label printing
 * @author falguni_sachde
 */
public interface LabelPrinter {

	/**
	 * This method has implemenation for printing  AbstractDomainObject .
	 * @param abstractDomainObject This is the object to be print
	 * @param ipAddress This is the IP address of calling application.
	 * @param userObj This is the User object of the current loggedin User.
	 * @return boolean
	 */
	//public boolean printLabel(AbstractDomainObject abstractDomainObject, String ipAddress, User userObj);	
	public boolean printLabel(AbstractDomainObject abstractDomainObject, String ipAddress, User userObj,String printerType,String printerLocation);
	
	/**
	 * This method has implemenation for printing  list of AbstractDomainObject .
	 * @param abstractDomainObject This is the List of objects to be print
	 * @param ipAddress This is the IP address of calling application.
	 * @param userObj This is the User object of the current loggedin User.
	 * @return boolean
	 */
	//public boolean printLabel(List<AbstractDomainObject> abstractDomainObject, String ipAddress, User userObj);	
	
	public boolean printLabel(List<AbstractDomainObject> abstractDomainObject, String ipAddress, User userObj,String printerType,String printerLocation);	
}
