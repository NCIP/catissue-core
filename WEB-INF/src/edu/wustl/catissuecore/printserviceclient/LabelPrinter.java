package edu.wustl.catissuecore.printserviceclient;

import java.util.List;

import gov.nih.nci.security.authorization.domainobjects.User;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * This Interface is used to declare method for label printing
 * @author falguni_sachde
 */
public interface LabelPrinter {

	public boolean printLabel(AbstractDomainObject abstractDomainObject, String ipAddress, User userObj);	
	
	public boolean printLabel(List<AbstractDomainObject> abstractDomainObject, String ipAddress, User userObj);	
}
