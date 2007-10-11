package edu.wustl.catissuecore.print;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import gov.nih.nci.security.authorization.domainobjects.User;

/**
 * This Class is used to define method for label printing
 * @author falguni_sachde
 */
public class LabelPrinterImpl implements LabelPrinter {

	public boolean printLabel(SpecimenCollectionGroup scgObj, String ipAddress,User userObj) 
	{
		
		return false;
	}

	public boolean printLabel(Specimen specimenObj, String ipAddress,User userObj) 
	{
	
		return false;
	}

	public boolean printLabel(Specimen[] specimenObj, String ipAddress,	User userObj) 
	{
		
	
		return false;
	}

}
