/*
 * Created on Nov 30, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package edu.wustl.common.util.global;

import java.util.HashMap;

/**
 * @author ajay_sharma
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * This classes is specific to common files. And contains all variables used by classes from 
 * common package.  
 * */
public class Constants 
{
	public static final String SELECT_OPTION = "-- Select --";
	public static final String CDE_CONF_FILE = "CDEConfig.xml";
	public static final String ANY = "Any";
	public static final String DELIMETER = ",";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String TAB_DELIMETER = "\t";
	
	public static final  HashMap STATIC_PROTECTION_GROUPS_FOR_OBJECT_TYPES = new HashMap();
	
	// Assign Privilege Constants.
	public static final boolean PRIVILEGE_ASSIGN = true;
	
//	 Mandar: Used for Date Validations in Validator Class
	public static final String DATE_SEPARATOR = "-";
	public static final String MIN_YEAR = "1900";
	public static final String MAX_YEAR = "9999";

	
	//Activity Status values
	public static final String ACTIVITY_STATUS_ACTIVE = "Active";

	public static final String ADD = "add";
	
	public static final String getCollectionProtocolPGName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "COLLECTION_PROTOCOL_";
	    }
	    return "COLLECTION_PROTOCOL_"+identifier;
	}
	
	public static final String getCollectionProtocolPIGroupName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "PI_COLLECTION_PROTOCOL_";
	    }
	    return "PI_COLLECTION_PROTOCOL_"+identifier;
	}
	
	public static final String getCollectionProtocolCoordinatorGroupName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "COORDINATORS_COLLECTION_PROTOCOL_";
	    }
	    return "COORDINATORS_COLLECTION_PROTOCOL_"+identifier;
	}
	
	public static final String getDistributionProtocolPGName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "DISTRIBUTION_PROTOCOL_";
	    }
	    return "DISTRIBUTION_PROTOCOL_"+identifier;
	}
	 
	public static final String getDistributionProtocolPIGroupName(Long identifier)
	{
	    if(identifier == null)
	    {
	        return "PI_DISTRIBUTION_PROTOCOL_";
	    }
	    return "PI_DISTRIBUTION_PROTOCOL_"+identifier;
	}
	public static final String getStorageContainerPGName()
	{
	    return "USER_";
	}
//	Aarti: Constants for security parameter required 
	//while retrieving data from DAOs
	public static final int INSECURE_RETRIEVE = 0;
	public static final int CLASS_LEVEL_SECURE_RETRIEVE = 1; 
	public static final int OBJECT_LEVEL_SECURE_RETRIEVE = 2; 
	
	public static final String CATISSUE_SPECIMEN = "CATISSUE_SPECIMEN";
	
}
