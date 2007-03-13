/**
 * <p>Title: Utility Class>
 * <p>Description:  Utility Class contain general methods used through out the application. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.util.global;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.TissueSpecimenReviewEventParameters;
import edu.wustl.catissuecore.domain.TransferEventParameters;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.logger.Logger;

/**
 * Utility Class contain general methods used through out the application.
 * @author kapil_kaveeshwar
 */
public class Utility extends edu.wustl.common.util.Utility
{

	public static Map getSpecimenTypeMap()
	{
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator itr = setPV.iterator();

		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_CLASS, null);
		Map subTypeMap = new HashMap();
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

		while (itr.hasNext())
		{
			List innerList = new ArrayList();
			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			String tmpStr = pv.getValue();
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));

			Set list1 = pv.getSubPermissibleValues();
			Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

			while (itr1.hasNext())
			{
				Object obj1 = itr1.next();
				PermissibleValue pv1 = (PermissibleValue) obj1;
				//Setting Specimen Type
				String tmpInnerStr = pv1.getValue();
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}

			subTypeMap.put(pv.getValue(), innerList);
		}

		return subTypeMap;
	}

	public static List getSpecimenTypes(String specimenClass)
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		List typeList = (List) specimenTypeMap.get(specimenClass);

		return typeList;
	}

	public static String getSpecimenClassName(Specimen specimen)
	{
		if (specimen instanceof CellSpecimen)
		{
			return Constants.CELL;
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			return Constants.MOLECULAR;
		}
		else if (specimen instanceof FluidSpecimen)
		{
			return Constants.FLUID;
		}
		else if (specimen instanceof TissueSpecimen)
		{
			return Constants.TISSUE;
		}

		return null;
	}

	public static int getEventParametersFormId(SpecimenEventParameters eventParameter)
	{
		if (eventParameter instanceof CheckInCheckOutEventParameter)
		{
			return Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof CollectionEventParameters)
		{
			return Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof EmbeddedEventParameters)
		{
			return Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof FixedEventParameters)
		{
			return Constants.FIXED_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof FrozenEventParameters)
		{
			return Constants.FROZEN_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof ReceivedEventParameters)
		{
			return Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof TissueSpecimenReviewEventParameters)
		{
			return Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID;
		}
		else if (eventParameter instanceof TransferEventParameters)
		{
			return Constants.TRANSFER_EVENT_PARAMETERS_FORM_ID;
		}

		return -1;
	}

	//Aniruddha : Added for enhancement - Specimen Aliquoting [Bug Id : 560]
	/**
	 * Returns true if qunatity is of type double else false.
	 * @param className Name of specimen class
	 * @param type Type of specimen
	 * @return true if qunatity is of type double else false.
	 */
	public static boolean isQuantityDouble(String className, String type)
	{
		if (Constants.CELL.equals(className))
		{
			return false;
		}
		else if (Constants.TISSUE.equals(className))
		{
			if (Constants.MICRODISSECTED.equals(type) || Constants.FROZEN_TISSUE_SLIDE.equals(type)
					|| Constants.FIXED_TISSUE_SLIDE.equals(type)
					|| Constants.FROZEN_TISSUE_BLOCK.equals(type)
					|| Constants.FIXED_TISSUE_BLOCK.equals(type)
					|| Constants.NOT_SPECIFIED.equals(type))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return true;
		}
	}

	//Aniruddha : Added for enhancement - Specimen Aliquoting
	/**
	 * Returns the unit of specimen quantity.
	 * @param className Name of specimen class
	 * @param type Type of specimen
	 * @return the unit of specimen quantity.
	 */
	public static String getUnit(String className, String type)
	{
		if (className == null || type == null || className.equals("-1"))
		{
			return "";
		}

		if (Constants.CELL.equals(className))
		{
			return Constants.UNIT_CC;
		}
		else if (Constants.FLUID.equals(className))
		{
			return Constants.UNIT_ML;
		}
		else if (Constants.MOLECULAR.equals(className))
		{
			return Constants.UNIT_MG;
		}
		else if (Constants.TISSUE.equals(className))
		{
			if (Constants.FIXED_TISSUE_BLOCK.equals(type)
					|| Constants.FROZEN_TISSUE_BLOCK.equals(type)
					|| Constants.FIXED_TISSUE_SLIDE.equals(type)
					|| Constants.FROZEN_TISSUE_SLIDE.equals(type)
					|| Constants.NOT_SPECIFIED.equals(type))
			{
				return Constants.UNIT_CN;
			}
			else if (Constants.MICRODISSECTED.equals(type))
			{
				return Constants.UNIT_CL;
			}
			else
			{
				return Constants.UNIT_GM;
			}
		}
		return "";
	}

	//Aniruddha : Added for enhancement - Specimen Aliquoting
	/**
	 * Returns the particular specimen object as per the specimen class.
	 * @param className Name of specimen class
	 * @return the particular specimen object as per the specimen class.
	 */
	public static Specimen getSpecimen(Specimen specimen)
	{
		if (specimen instanceof CellSpecimen)
		{
			return new CellSpecimen();
		}
		else if (specimen instanceof FluidSpecimen)
		{
			return new FluidSpecimen();
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			return new MolecularSpecimen();
		}
		else if (specimen instanceof TissueSpecimen)
		{
			return new TissueSpecimen();
		}

		return new Specimen();
	}

	public static List getSpecimenClassTypes()
	{
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator itr = setPV.iterator();

		List specimenClassTypeList = new ArrayList();

		while (itr.hasNext())
		{

			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			String tmpStr = pv.getValue();
			specimenClassTypeList.add(tmpStr);

		} // class and values set

		return specimenClassTypeList;

	}
	
	
	 /* this Function gets the list of all storage types as argument and  
     * create a list in which nameValueBean is stored with Type and Identifier of storage type.
     * and returns this list
     */ 
    public static List getStorageTypeList(List list)
    {
    	NameValueBean typeAny=null;
    	List storageTypeList=new ArrayList();
    	Iterator typeItr=list.iterator();
    	
    	while(typeItr.hasNext())
    	{
    		StorageType type=(StorageType)typeItr.next();
    		if(type.getId().longValue()==1)
    		{
    			typeAny=new NameValueBean(Constants.HOLDS_ANY,type.getId());
    		}
    		else
    		{
    			storageTypeList.add(new NameValueBean(type.getName(),type.getId()));
    		}
    	}
    	Collections.sort(storageTypeList);
    	if(typeAny!=null)
    	{
    		storageTypeList.add(0,typeAny);
    	}	
    	return storageTypeList;
    	
    }
    /* this Function gets the list of all storage types as argument and  
     * create a list in which nameValueBean is stored with Type and Identifier of storage type.
     * and returns this list
     */ 
    public static List getStorageTypeList(List list,boolean includeAny)
    {
    	NameValueBean typeAny=null;
    	List storageTypeList=new ArrayList();
    	Iterator typeItr=list.iterator();
    	while(typeItr.hasNext())
    	{
    		StorageType type=(StorageType)typeItr.next();
    		if(type.getId().longValue()==1)
    		{
    			typeAny=new NameValueBean(Constants.HOLDS_ANY,type.getId());
    		}
    		else
    		{
    			storageTypeList.add(new NameValueBean(type.getName(),type.getId()));
    		}
    	}
    	Collections.sort(storageTypeList);
    	if(includeAny)
    	{
    		if(typeAny!=null)
    		{
    			storageTypeList.add(0,typeAny);
    		}	
    	}
    	else
    	{
    		storageTypeList.add(0,new NameValueBean(Constants.SELECT_OPTION,"-1"));
    	}
    	return storageTypeList;
    	
    }
    /* this Function gets the list of all Specimen Class Types as argument and  
     * create a list in which nameValueBean is stored with Name and Identifier of specimen Class Type.
     * and returns this list
     */
    
    public static List getSpecimenClassTypeListWithAny()
    {
    	CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
    	Set setPV = specimenClassCDE.getPermissibleValues();
    	Iterator itr = setPV.iterator();
    
    	List specimenClassTypeList =  new ArrayList();
    	specimenClassTypeList.add(new NameValueBean("--All--","-1"));
    	
    	while(itr.hasNext())
    	{
    		//List innerList =  new ArrayList();
    		Object obj = itr.next();
    		PermissibleValue pv = (PermissibleValue)obj;
    		String tmpStr = pv.getValue();
    		Logger.out.info("specimen class:"+tmpStr);
    		specimenClassTypeList.add(new NameValueBean( tmpStr,tmpStr));
    					
    	} // class and values set
    	
    	return specimenClassTypeList;

    	
    }
    
    /* This function gets the list of all collection protocols as argument and  
     * create a list in which nameValueBean is stored with Title and Identifier of Collection Protocol.
     * and returns this list
     */ 
    public static List getCollectionProtocolList(List list)
    {
    	List collectionProtocolList=new ArrayList();
    	   	 
    	Iterator cpItr=list.iterator();
    	while(cpItr.hasNext())
    	{
    		CollectionProtocol cp=(CollectionProtocol)cpItr.next();
    		collectionProtocolList.add(new NameValueBean(cp.getTitle(),cp.getId()));
    	}
    	Collections.sort(collectionProtocolList);
    	collectionProtocolList.add(0,new NameValueBean(Constants.HOLDS_ANY,"-1"));
    	return collectionProtocolList;
    }
    
    /* this Function gets the list of all specimen Array types as argument and  
     * create a list in which nameValueBean is stored with specimen array and Identifier of specimen Array type.
     * and returns this list
     */ 
    public static List getSpecimenArrayTypeList(List list)
    {
    	NameValueBean typeAny=null;
    	List spArrayTypeList=new ArrayList();
    	Iterator typeItr=list.iterator();
    	
    	while(typeItr.hasNext())
    	{
    		SpecimenArrayType type=(SpecimenArrayType)typeItr.next();
    		if(type.getId().longValue()==2)
    		{
    			typeAny=new NameValueBean(Constants.HOLDS_ANY,type.getId());
    		}
    		else
    		{
    			spArrayTypeList.add(new NameValueBean(type.getName(),type.getId()));
    		}
    	}
    	Collections.sort(spArrayTypeList);
    	if(typeAny!=null)
    	{
    		spArrayTypeList.add(0,typeAny);
    	}	
    	return spArrayTypeList;
    	
    }

	private static String pattern = "MM-dd-yyyy";

	/**
	 * @param date String representation of date.
	 * @param pattern Date pattern to be used.
	 * @return Month of the given date.
	 * @author mandar_deshmukh 
	 */
	public static int getMonth(String date, String pattern)
	{
		int month = 0;
		month = getCalendar(date, pattern).get(Calendar.MONTH);
		month = month + 1;
		return month;
	}

	public static int getMonth(String date)
	{
		int month = 0;

		month = getCalendar(date, pattern).get(Calendar.MONTH);
		month = month + 1;
		return month;
	}

	/**
	 * @param date String representation of date.
	 * @param pattern Date pattern to be used.
	 * @return Day of the given date.
	 * @author mandar_deshmukh 
	 */
	public static int getDay(String date, String pattern)
	{
		int day = 0;
		day = getCalendar(date, pattern).get(Calendar.DAY_OF_MONTH);
		return day;
	}

	public static int getDay(String date)
	{
		int day = 0;
		day = getCalendar(date, pattern).get(Calendar.DAY_OF_MONTH);
		return day;
	}

	/**
	 * @param date String representation of date.
	 * @param pattern Date pattern to be used.
	 * @return Year of the given date.
	 * @author mandar_deshmukh 
	 */
	public static int getYear(String date, String pattern)
	{
		int year = 0;
		year = getCalendar(date, pattern).get(Calendar.YEAR);
		return year;
	}

	public static int getYear(String date)
	{
		int year = 0;
		year = getCalendar(date, pattern).get(Calendar.YEAR);
		return year;
	}

	/* Method to validate the date given by the user and return a calendar object for the date instance.
	 * It returns a calendar object based on the date provided. If invalid date is provided it returns the current calendar instance.
	 */
	private static Calendar getCalendar(String date, String pattern)
	{
		try
		{
			SimpleDateFormat dateformat = new SimpleDateFormat(pattern);
			Date givenDate = dateformat.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(givenDate);
			return calendar;
		}
		catch (Exception e)
		{
			Logger.out.error(e);
			Calendar calendar = Calendar.getInstance();
			return calendar;
		}
	}

	public static void main(String[] args)
	{
		String s = "ds fsbsdjf hsfsdfdsh f,sd fsdfjbhsdj sdf,sdf s,sd fds,sd ffs,sd f\"sd fs \"sd fsdF \"sf";
		Object s1 = toNewGridFormat(s);
		System.out.println("Original String : "+ s);
		System.out.println("Updated String : "+ s1);
		
//		//		Utility u = new Utility();
//		String dt = "18-10-06";
//		String pt = "dd-MM-yy";
//
//		System.out.println(Utility.getMonth(dt, pt) + "/" + Utility.getDay(dt, pt) + "/"
//				+ Utility.getYear(dt, pt));
//
//		dt = "28-11-06";
//		pt = "dd-MM-yy";
//		System.out.println(Utility.getMonth(dt, pt) + "/" + Utility.getDay(dt, pt) + "/"
//				+ Utility.getYear(dt, pt));
//
//		dt = "18-21-06";
//		pt = "MM-dd-yy";
//		System.out.println(Utility.getMonth(dt, pt) + "/" + Utility.getDay(dt, pt) + "/"
//				+ Utility.getYear(dt, pt));
//
//		dt = "18-asa-06";
//		pt = "dd-MM-yy";
//		System.out.println(Utility.getMonth(dt, pt) + "/" + Utility.getDay(dt, pt) + "/"
//				+ Utility.getYear(dt, pt));
	}
	
	/**
	 * This method returns a list of string values for a given CDE.
	 * 
	 * @param cdeName
	 * @return
	 */
	public static List getListForCDE(String cdeName) {
		CDE cde = CDEManager.getCDEManager().getCDE(cdeName);
		List valueList = new ArrayList();
		
		if(cde!=null)
		{
			Iterator iterator = cde.getPermissibleValues().iterator();
			while(iterator.hasNext())
			{
				PermissibleValue permissibleValue = (PermissibleValue)iterator.next();
				
				valueList.addAll(loadPermissibleValue(permissibleValue));
			}
		}
		
		Collections.sort(valueList);
		valueList.add(0,Constants.SELECT_OPTION);
		return valueList;
	}
	
	/**
	 * returns list of all subPVs under this PV, recursively.
	 * 
	 * @param permissibleValue
	 * @return
	 */
	private static List loadPermissibleValue(PermissibleValue permissibleValue)
	{
		List pvList = new ArrayList();
		String value = permissibleValue.getValue();
		pvList.add(value);
		
		Iterator iterator = permissibleValue.getSubPermissibleValues().iterator();
		while(iterator.hasNext())
		{
			PermissibleValue subPermissibleValue = (PermissibleValue)iterator.next();
			List subPVList = loadPermissibleValue(subPermissibleValue);
			pvList.addAll(subPVList);
		}
		return pvList;
	}
	
	//Mandar : 29Nov06
    /**
     * Changes the format of the string compatible to New Grid Format, 
     * removing escape characters and special characters from the string
     * Also replaces comma with space as comma is used as a delimiter.
     * @param obj - Unformatted obj to be printed in Grid Format
     * @return obj - Foratted obj to print in Grid Format
     */
    public static Object toNewGridFormat(Object obj)
    {
    	obj = toGridFormat(obj);
        if(obj instanceof String)
        {
            String objString=(String)obj;
            StringBuffer tokenedString=new StringBuffer();
            
            StringTokenizer tokenString=new StringTokenizer(objString,","); 
            
            while(tokenString.hasMoreTokens())
            {
               tokenedString.append(tokenString.nextToken()+" ");
            }
            String gridFormattedStr=new String(tokenedString);
            obj=gridFormattedStr;
        }
 
        return obj;
    }
 
 //Consent tracking(Virender Mehta)   
    /**
	 * Prepare Respopnse List
	 * @param opr If Operation = Edit then "Withdraw" is added in the List
	 * @return listOfResponces
	 */
   public static List responceList(String addeditOperation)
	{
		List listOfResponces=new ArrayList();
		listOfResponces.add(new NameValueBean(Constants.NOT_SPECIFIED,Constants.NOT_SPECIFIED));
		listOfResponces.add(new NameValueBean(Constants.BOOLEAN_YES,Constants.BOOLEAN_YES));
		listOfResponces.add(new NameValueBean(Constants.BOOLEAN_NO,Constants.BOOLEAN_NO));
		if(addeditOperation.equalsIgnoreCase(Constants.EDIT))
		{
			listOfResponces.add(new NameValueBean(Constants.WITHDRAWN,Constants.WITHDRAWN));
		}
		return listOfResponces;  	
	}
   
   public static Long toLong(String string) throws NumberFormatException
   {
	   if((string!=null)&&(string.trim()!=""))
	   {
		   return new Long(string);
	   }
	   return null;
   }
   
   /**
	 * @param entity_name_participant
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	public static Long getEntityId(String entityName) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if(entityName!=null)
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			EntityInterface entity;
			entity = entityManager.getEntityByName(entityName);
			if(entity!=null)
			{
				return entity.getId();
			}
		}
		return new Long(0);
	}
// Consent tracking(Virender Mehta) 


 /**
     * This method creates a comma separated string of numbers representing column width.
     *
     */
        public static String getColumnWidth(List columnNames)
        {
        	String colWidth=getColumnWidth((String)columnNames.get(0));

        	for(int col=1; col<columnNames.size(); col++)
        	{
        		String columnName=(String)columnNames.get(col);
       			colWidth = colWidth +","+ getColumnWidth(columnName);
        	}
        	return colWidth;
        }

        private static String getColumnWidth(String columnName)
        {
    		if (columnName.trim().equals("ID"))
    		{
    			return "0";
    		}
    		else
    		{
    			return "100";
    		}
        }

}