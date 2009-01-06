package edu.wustl.catissuecore.util.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.PermissibleValueImpl;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

/**
 * This class is the Setter class for hash map.
 * CatissueCoreServeletContextListener.java class call validateDefaultValues() method, this method will
 * validate default Values from Configuration file i.e caTissueCore_Properties.xml for enumerated dropdowns and populate
 * defaultValue map which will have key value pair if validation fails for some value then empty String will be mapped to corresponding key.
 
 * @author virender_mehta
 * @Reviewer: Sachin Lale
 * Bug ID: defaultValueConfiguration_BugID
 * Patch ID:defaultValueConfiguration_BugID_3
 * See also:defaultValueConfiguration_BugID_1,2
 */
public class DefaultValueManager
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
	private static void setDefaultValue(String key, String value)
	{
		defaultValueMap.put(key, value);
	}
	
	/**
     * Description: Validate and Configure default value for enumerated dropdowns.
     */
	public static void validateAndInitDefaultValueMap()
    {
    	String specimenClassName = null;
    	List permissibleValueList = new ArrayList();
    	for(int iCount=0;iCount<Constants.defaultValueKeys.length;iCount++)
    	{
    		String defaultValue = XMLPropertyHandler.getValue(Constants.defaultValueKeys[iCount][0]);
    		NameValueBean defaultValueBean=null; 
    		//converting defaultValue into NameValue object.
    		if(defaultValue!=null)
    		{
    			defaultValueBean = new NameValueBean(defaultValue,defaultValue);
    		}
    		if((Constants.defaultValueKeys[iCount][0]).equals(Constants.DEFAULT_SPECIMEN))
    		{
    			specimenClassName = defaultValue;
    		}
    		if((Constants.defaultValueKeys[iCount][0]).equals(Constants.DEFAULT_SPECIMEN_TYPE))
    		{
    			//Get the Specimen Type List. 
    			permissibleValueList = Utility.getSpecimenTypes(specimenClassName);
    		}
    		if((Constants.defaultValueKeys[iCount][0]).equals(Constants.DEFAULT_CLINICAL_DIAGNOSIS))
    		{
    			String sourceObjectName =PermissibleValueImpl.class.getName();
        		String[] selectColumnName ={ "value"};
        		// String[] whereColumnName ={ "value" };
        		// String[] whereColumnCondition =  { "=" };
        		// Object[] whereColumnValue ={Constants.SPECIMEN_TYPE_NOT_SPECIFIED};
        		
        		String[] whereColumnName ={ "value" ,"cde.publicId"}; 
        		String[] whereColumnCondition =  { "=","=" };
        		Object[] whereColumnValue ={Constants.NOT_SPECIFIED,"Clinical_Diagnosis_PID"};
        		
        		String joinCondition = null;
        		List clinicalDiagnosisList = new ArrayList();
        		IBizLogic bizLogic = new DefaultBizLogic();
        		try {
        			Iterator<String> iterator = bizLogic.retrieve(sourceObjectName,selectColumnName,whereColumnName, whereColumnCondition,whereColumnValue, joinCondition).iterator();
        			
        			if(iterator.hasNext())
        			{
        				String clinicaDiagnosisvalue=iterator.next();
        				permissibleValueList.add(new NameValueBean(clinicaDiagnosisvalue,clinicaDiagnosisvalue));
        			}
        		}
        		catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		else
    		{
    			permissibleValueList = Utility.getListFromCDE(Constants.defaultValueKeys[iCount][1]);
    		}
    		//added for bug 10750
    		if((Constants.defaultValueKeys[iCount][0]).equals(Constants.DEFAULT_PRINTER_LOCATION))
    		{
    			permissibleValueList = new ArrayList<NameValueBean>();
    			if(Variables.printerLocationList!=null&&Variables.printerLocationList.size()>0)
    			{	
    			  defaultValueBean = Variables.printerLocationList.get(0);
    			  permissibleValueList.add(defaultValueBean); 
    			}
    			
    		}
    		//added for bug 10750
    		if((Constants.defaultValueKeys[iCount][0]).equals(Constants.DEFAULT_PRINTER_TYPE))
    		{
    			permissibleValueList = new ArrayList<NameValueBean>();
    			if(Variables.printerTypeList!=null&&Variables.printerTypeList.size()>0)
    			{	
    			  defaultValueBean = Variables.printerTypeList.get(0);
    			  permissibleValueList.add(defaultValueBean); 
    			}
    			
    		}
    		//If List contain default value then key,Value pair is set in default value map else empty string is set for that key
    		if(permissibleValueList!=null&&permissibleValueList.contains(defaultValueBean))
    		{
    		 	DefaultValueManager.setDefaultValue(Constants.defaultValueKeys[iCount][0],defaultValueBean.getValue());
    		}
    		else
    		{
    		 	DefaultValueManager.setDefaultValue(Constants.defaultValueKeys[iCount][0],"");
    		   	Logger.out.error("Default Value set for '"+Constants.defaultValueKeys[iCount][0]+"' is not in the CDEList");
    		}
    	}
    }
}
