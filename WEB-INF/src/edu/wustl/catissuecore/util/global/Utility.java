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

import org.apache.struts.action.ActionForm;

import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
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
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.CDEBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
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
        	/*
        	 *  Patch ID: Bug#3090_31
			 *	Description: The first column which is just a checkbox, used to select the rows, was always given
			 *				 a width of 100. Now width of 20 is set for the first column.
			 *				 Also, width of 100 was being applied to each column of the grid, which increasing the total width
			 *				 of the grid. Now the width of each column is set to 80.
			 */
        	String columnWidth = null;
        	if (columnName.trim().equals("ID"))
    		{
        		columnWidth= "0";
    		}
        	else if(columnName.trim().equals(""))
    		{
    			columnWidth="20";
    		}
    		else
    		{
    			columnWidth= "80";
    		}
        	return columnWidth;
        }
        /**
         * This method set TissueList with only Leaf node.
         * @return tissueList
         */
       public static List tissueSiteList()
       {
    	   	CDE cde = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_TISSUE_SITE);
	    	CDEBizLogic cdeBizLogic = (CDEBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.CDE_FORM_ID);
	    	List tissueList = new ArrayList(); 
	    	// set first index as --select-- option to display  in combo.
	    	tissueList.add(new NameValueBean(Constants.SELECT_OPTION,""+Constants.SELECT_OPTION_VALUE));
	    	// get the filtered tissue list which is a leaf node
	    	cdeBizLogic.getFilteredCDE(cde.getPermissibleValues(),tissueList);
	    	return tissueList;
       }
       
       /**
        * Name : Vijay Pande
        * Bug ID: 4119
        * Patch ID: 4119_1 
        * See also: 1-2
        * Description: Signature of method is changed to pass only required parameters instread of whole object, so that the method become  more maintainable
        */
       /**
         * Method to check type and class compatibility of specimen as a part of validation process. 
	     * @param specimenClass specimen class
	     * @param specimenType type of specimen
	     * @return boolean returns (true/false) depending on type and class are compatible or not
	     * @throws DAOException 
	     */
	    public static boolean validateSpecimenTypeClass(String specimenClass, String specimenType) throws DAOException
	    {
	   		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);	
	   		if (specimenClass==null || !Validator.isEnumeratedValue(specimenClassList, specimenClass))
	   		{
	   			throw new DAOException(ApplicationProperties.getValue("protocol.class.errMsg"));
	   		}
	   		if (!Validator.isEnumeratedValue(Utility.getSpecimenTypes(specimenClass), specimenType))
	   		{
	   			throw new DAOException(ApplicationProperties.getValue("protocol.type.errMsg"));
	   		}
	   		/* Patch ends here  */
	   		return true;
	    }
	  /**
	   * This method will return Permissible Value List from CDE depending on the listType 
	   * @param listType
	   * @return
	   */
       public static List getListFromCDE(String listType)
       {
    	   List CDEList = CDEManager.getCDEManager().getPermissibleValueList(listType, null);
    	   return CDEList;
       }

       /**
        * Patch ID: Entered_Events_Need_To_Be_Visible_16
        * See also: 1-5
        * Description: getter setter for  the eventsToolTipText and eventsToolTipMap
        */ 
       /**
        * This method generated the toolTip for events in HTML format for the given NewSpecimen form
        * @param specimenForm for which toolTip has to generate for events
        * @return toolTipText in required format
        * @throws Exception generic exception
        */
       public static String getToolTipText(NewSpecimenForm specimenForm)throws Exception
       {
    	   String toolTipText="";
    	   if(specimenForm!=null)
    	   {
    		   toolTipText=new String("<HTML><table border='0'><tr><td colspan='2'><b>CollectedEvents</b></td></tr><tr><td>");
    		   toolTipText+="Collector: <i>";
    		   toolTipText+=Utility.getUserNameById(specimenForm.getCollectionEventUserId());
    		   toolTipText+="</i></td><td>Date: <i>";
    		   toolTipText+=specimenForm.getCollectionEventdateOfEvent();
    		   toolTipText+="</i></td></tr><tr><td>Procedure: <i>";
    		   toolTipText+=specimenForm.getCollectionEventCollectionProcedure();
    		   toolTipText+="</i></td><td>Container: <i>";
    		   toolTipText+=specimenForm.getCollectionEventContainer();

    		   toolTipText+="</i></td></tr><tr><td colspan='2'><b>Received Events</b></td></tr>";
    		   toolTipText+="<tr><td>Reciever: <i>";
    		   toolTipText+=Utility.getUserNameById(specimenForm.getReceivedEventUserId());
    		   toolTipText+="</i></td><td>Date: <i>";
    		   toolTipText+=specimenForm.getReceivedEventDateOfEvent();
    		   toolTipText+="</i></td></tr><tr><td colspan='2'>Quality: <i>";
    		   toolTipText+=specimenForm.getReceivedEventReceivedQuality();
    		   toolTipText+="</td></tr></HTML>";
    	   }	
    	   return toolTipText;
       }

       /**
        * This method returns the user name of user of given id (format of name: LastName,FirstName)
        * @param userId user id of which user name has to return
        * @return userName in the given format
        * @throws Exception generic exception
        */
       public static String getUserNameById(Long userId)throws Exception
       {
    	   String className=User.class.getName();
    	   String colName=Constants.SYSTEM_IDENTIFIER;
    	   String colValue=userId.toString();
    	   String userName="";
    	   UserBizLogic bizLogic = (UserBizLogic) BizLogicFactory.getInstance().getBizLogic(User.class.getName());
    	   List userList=bizLogic.retrieve(className, colName, colValue);
    	   if(userList!=null && userList.size()>0)
    	   {
    		   User user=(User)userList.get(0);
    		   userName=user.getLastName();
    		   userName+=",";
    		   userName+=user.getFirstName();
    	   }
    	   return userName;
       }
       
       /**
        * moved from NewMultipleSecimenAction class to Utility class
        * @param form NewSpecimenForm
        */
       /*
   	 * This method sets the default selection of list boxes to Default values.
   	 * @author mandar_deshmukh
   	 *
   	 */
   	public static void setDefaultListSelection(ActionForm form)
   	{
   		if(form!=null)
   		{
   			/**
   	         * Patch ID:defaultValueConfiguration_BugID_5
   	         * See also:defaultValueConfiguration_BugID_1,2,3,4
   	         * Description: Configuration for default value for Collection Procedure, Container and Quality
   	         */
   			String collectionProcedure = (String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_COLLECTION_PROCEDURE);
   			String container = (String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_CONTAINER);
   			String receivedQuality=(String)DefaultValueManager.getDefaultValue(Constants.DEFAULT_RECEIVED_QUALITY);
   			
   			if(((NewSpecimenForm) form).getCollectionEventCollectionProcedure() == null)
   				((NewSpecimenForm) form).setCollectionEventCollectionProcedure(collectionProcedure);
   			
   			if(((NewSpecimenForm) form).getCollectionEventContainer() == null)
   				((NewSpecimenForm) form).setCollectionEventContainer(container);
   			
   			if(((NewSpecimenForm) form).getReceivedEventReceivedQuality() == null)
   				((NewSpecimenForm) form).setReceivedEventReceivedQuality(receivedQuality);
   		}
   	}

    /**
     * This method generates the default toolTip text for event buttons when the NewSpecimenForm do not contains events information
     * This is required in the init case only
     * @return toolTip for event button
     */
    public static String getDefaultEventsToolTip()
    {
 	   NewSpecimenForm specimenForm=new NewSpecimenForm();
 	   try
 	   {	
 		   long userId=0;
 		   Utility.setDefaultListSelection(specimenForm);
 		   UserBizLogic userBizLogic = (UserBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
 		   List userList = userBizLogic.getUsers(Constants.ADD);
 		   if(userList!=null && userList.size()>1)
 		   {
 			   NameValueBean nvb=(NameValueBean)userList.get(1);
 			   userId=new Long(nvb.getValue());
 		   }
 		   specimenForm.setCollectionEventUserId(userId);
 		   specimenForm.setReceivedEventUserId(userId);
 		   specimenForm.setCollectionEventdateOfEvent(Utility.parseDateToString((new Date()),Constants.DATE_PATTERN_MM_DD_YYYY));
 		   specimenForm.setReceivedEventDateOfEvent(Utility.parseDateToString((new Date()),Constants.DATE_PATTERN_MM_DD_YYYY));
 		   String toolTip=Utility.getToolTipText(specimenForm);
 		   return toolTip;
 	   }
 	   catch(Exception ex)
 	   {
 		   Logger.out.error("Error in getEventsToolTip method of Applet:"+ex.getMessage());
 	   }
 	   return "null";
    }
    /** -- patch ends here -- */
}