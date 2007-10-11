package edu.wustl.catissuecore.print;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import gov.nih.nci.security.authorization.domainobjects.User;

/**
 * This Interface is used to declare method for label printing
 * @author falguni_sachde
 */
public interface LabelPrinter {

	public boolean printLabel(SpecimenCollectionGroup scgObj, String ipAddress, User userObj);
	
	public boolean printLabel(Specimen specimenObj, String ipAddress, User userObj);
	
	public boolean printLabel(Specimen[] specimenObj, String ipAddress, User userObj);

}
