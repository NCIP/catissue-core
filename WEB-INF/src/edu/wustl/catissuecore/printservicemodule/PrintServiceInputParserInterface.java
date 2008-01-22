package edu.wustl.catissuecore.printservicemodule;


/**
 * This Interface is the base interface for Printservice.Any Class which implements this interface will provide method to print object.
 * @author falguni_sachde
 *
 */
public interface PrintServiceInputParserInterface {

	
 /**
 * @param object Object for which label printing  will be applied
 * @return boolean Status
 * @throws Exception
 */
public boolean callPrintService(Object object)  throws Exception ;		
	
}
