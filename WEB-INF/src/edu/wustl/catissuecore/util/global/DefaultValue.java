package edu.wustl.catissuecore.util.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

/**
 * This class is the Setter class for hash map.
 * CatissueCoreServeletContextListener.java class call validateDefaultValues() method, this method will
 * validate default Values from Configuration file i.e caTissueCore_Properties.xml for enumerated dropdowns and populate
 * defaultValue map which will have key value pair if validation fails for some value then null will be mapped to corresponding key.
 
 * @author virender_mehta
 * @Reviewer: Sachin Lale
 * Bug ID: defaultValueConfiguration_BugID
 * Patch ID:defaultValueConfiguration_BugID_3
 * See also:defaultValueConfiguration_BugID_1,2
 */
public class DefaultValue
{
	
    private static HashMap defaultValueMap = new HashMap();
    
    /**
     * This method is getter method with parameter as Key and it will return value mappend to the key
     * @param defaultConstant
     * @return dafaultValueMap.get(defaultConstant);
     */
    public static Object getDefaultValue(String defaultConstant)
	{
		return defaultValueMap.get(defaultConstant);
	}
    
    /**
     * This method is setter method 
     * It will map value with the key
     * @param key
     * @param value
     */
	public static void setDefaultValue(String key, String value)
	{
		defaultValueMap.put(key, value);
	}
	
	/**
     * Description: Validate and Configure default value for enumerated dropdowns.
     */
	public static void validateDefaultValues()
    {
    	String className = null;
    	List permissibleValueList = new ArrayList();
    	List finalPermissibleValueList = new ArrayList();
    	for(int iCount=0;iCount<Constants.defaultValueKeys.length;iCount++)
    	{
    		String defaultValue = XMLPropertyHandler.getValue(Constants.defaultValueKeys[iCount][0]);
    		if((Constants.defaultValueKeys[iCount][0]).equals(Constants.DEFAULT_SPECIMEN))
    		{
    			className = defaultValue;
    		}
    		if((Constants.defaultValueKeys[iCount][0]).equals(Constants.DEFAULT_SPECIMEN_TYPE))
    		{
    			//get the Specimen class and type from the cde
    			CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
    			Set setPV = specimenClassCDE.getPermissibleValues();
    			Iterator itr = setPV.iterator();
    			while (itr.hasNext())
    			{
    				Object obj = itr.next();
    				PermissibleValue pv = (PermissibleValue) obj;
    				String tmpStr = pv.getValue();
    				// if current Permissible value is of selected class name
    				if(className.equalsIgnoreCase(tmpStr))
    				{
    					Logger.out.debug(tmpStr);
    					// get specimen sub type permissible values of selected specimen class 
        				Set subTypelist = pv.getSubPermissibleValues();
        				Logger.out.debug("list1 " + subTypelist);
        				Iterator itr1 = subTypelist.iterator();
        				while (itr1.hasNext())
        				{
        					Object obj1 = itr1.next();
        					PermissibleValue subTypeValue = (PermissibleValue) obj1;
        					// set specimen type
        					permissibleValueList.add(new NameValueBean(subTypeValue.getValue(), subTypeValue.getValue()));
        				}
        				Collections.sort(permissibleValueList);
    				}
    				
    			} // class and values set
    		}
    		else
    		{
    			permissibleValueList = Utility.getListFromCDE(Constants.defaultValueKeys[iCount][1]);
    		}
    		//permissibleValueList is Namevalue list.
    		//Converting Namevalue List into Value List.
    		finalPermissibleValueList.add(Constants.SELECT_OPTION);
    		for(int i=0;i<permissibleValueList.size();i++) 
    		{
    			NameValueBean nvb = (NameValueBean)permissibleValueList.get(i);
    			finalPermissibleValueList.add(nvb.getName());
    		}
    		//If List contain default value then key,Value pair is set in default value map else empty string is set for that key
    		if(finalPermissibleValueList.contains(defaultValue))
    		{
    		 		DefaultValue.setDefaultValue(Constants.defaultValueKeys[iCount][0],defaultValue);
    		}
    		else
    		{
    		 		DefaultValue.setDefaultValue(Constants.defaultValueKeys[iCount][0],"");
    		   		Logger.out.error("Default Value set for '"+Constants.defaultValueKeys[iCount][0]+"' is not in the CDEList");
    		}
    	}
    }
}
