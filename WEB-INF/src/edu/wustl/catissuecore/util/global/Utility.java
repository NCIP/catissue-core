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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimenRequirement;
import edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.EmbeddedEventParameters;
import edu.wustl.catissuecore.domain.FixedEventParameters;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimenRequirement;
import edu.wustl.catissuecore.domain.FrozenEventParameters;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimenRequirement;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.TissueSpecimenRequirement;
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
    	
		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS,null);
		Map subTypeMap = new HashMap();
    	specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
		
		while(itr.hasNext())
		{
			List innerList =  new ArrayList();
			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue)obj;
			String tmpStr = pv.getValue();
			specimenClassList.add(new NameValueBean(tmpStr,tmpStr));
			
			Set list1 = pv.getSubPermissibleValues();
			Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
			
			while(itr1.hasNext())
			{
				Object obj1 = itr1.next();
				PermissibleValue pv1 = (PermissibleValue)obj1;
				//Setting Specimen Type
				String tmpInnerStr = pv1.getValue(); 
				innerList.add(new NameValueBean( tmpInnerStr,tmpInnerStr));  
			}
			
			subTypeMap.put(pv.getValue(),innerList);
		}
		
		return subTypeMap;
	}
	
	public static List getSpecimenTypes(String specimenClass)
	{
		Map specimenTypeMap = getSpecimenTypeMap();
		List typeList = (List)specimenTypeMap.get(specimenClass);
		
		return typeList;
	}
	
	public static String getSpecimenClassName(SpecimenRequirement requirement)
	{
		if(requirement instanceof CellSpecimenRequirement)
		{
			return Constants.CELL;
		}
		else if(requirement instanceof MolecularSpecimenRequirement)
		{
			return Constants.MOLECULAR;
		}
		else if(requirement instanceof FluidSpecimenRequirement)
		{
			return Constants.FLUID;
		}
		else if(requirement instanceof TissueSpecimenRequirement)
		{
			return Constants.TISSUE;
		}
		
		return null;
	}
	
	public static String getSpecimenClassName(Specimen specimen)
	{
		if(specimen instanceof CellSpecimen)
		{
			return Constants.CELL;
		}
		else if(specimen instanceof MolecularSpecimen)
		{
			return Constants.MOLECULAR;
		}
		else if(specimen instanceof FluidSpecimen)
		{
			return Constants.FLUID;
		}
		else if(specimen instanceof TissueSpecimen)
		{
			return Constants.TISSUE;
		}
		
		return null;
	}
	
	public static int getEventParametersFormId(SpecimenEventParameters eventParameter)
	{
		if(eventParameter instanceof CheckInCheckOutEventParameter)
		{
			return Constants.CHECKIN_CHECKOUT_EVENT_PARAMETERS_FORM_ID;
		}
		else if(eventParameter instanceof CollectionEventParameters)
		{
			return Constants.COLLECTION_EVENT_PARAMETERS_FORM_ID;
		}
		else if(eventParameter instanceof EmbeddedEventParameters)
		{
			return Constants.EMBEDDED_EVENT_PARAMETERS_FORM_ID;
		}
		else if(eventParameter instanceof FixedEventParameters)
		{
			return Constants.FIXED_EVENT_PARAMETERS_FORM_ID;
		}
		else if(eventParameter instanceof FrozenEventParameters)
		{
			return Constants.FROZEN_EVENT_PARAMETERS_FORM_ID;
		}
		else if(eventParameter instanceof ReceivedEventParameters)
		{
			return Constants.RECEIVED_EVENT_PARAMETERS_FORM_ID;
		}
		else if(eventParameter instanceof TissueSpecimenReviewEventParameters)
		{
			return Constants.TISSUE_SPECIMEN_REVIEW_EVENT_PARAMETERS_FORM_ID;
		}
		else if(eventParameter instanceof TransferEventParameters)
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
	public static boolean isQuantityDouble(String className,String type)
	{
		if(Constants.CELL.equals(className))
		{
			return false;
		}
		else if(Constants.TISSUE.equals(className))
		{
			if(Constants.MICRODISSECTED.equals(type) || Constants.FROZEN_TISSUE_SLIDE.equals(type)
					|| Constants.FIXED_TISSUE_SLIDE.equals(type) || Constants.FROZEN_TISSUE_BLOCK.equals(type)
					|| Constants.FIXED_TISSUE_BLOCK.equals(type) || Constants.NOT_SPECIFIED.equals(type))
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
	public static String getUnit(String className,String type)
	{
		if(className == null || type == null)
		{
			return "";
		}
		
		if(className.equals("-1"))
		{
			return "";
		}
		else
		{
			if(Constants.CELL.equals(className))
			{
				return Constants.UNIT_CC;
			}
			else if(Constants.FLUID.equals(className))
			{
				return Constants.UNIT_ML;
			}
			else if(Constants.MOLECULAR.equals(className))
			{
				return Constants.UNIT_MG;
			}
			else if(Constants.TISSUE.equals(className))
			{
				if(Constants.FIXED_TISSUE_BLOCK.equals(type) || Constants.FROZEN_TISSUE_BLOCK.equals(type)
						|| Constants.FIXED_TISSUE_SLIDE.equals(type) || Constants.FROZEN_TISSUE_SLIDE.equals(type)
						|| Constants.NOT_SPECIFIED.equals(type))
				{
					return Constants.UNIT_CN;
				}
				else if(Constants.MICRODISSECTED.equals(type))
				{
					return Constants.UNIT_CL;
				}
				else
				{
					return Constants.UNIT_GM;
				}
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
		if(specimen instanceof CellSpecimen)
		{
			return new CellSpecimen();
		}
		else if(specimen instanceof FluidSpecimen)
		{
			return new FluidSpecimen();
		}
		else if(specimen instanceof MolecularSpecimen)
		{
			return new MolecularSpecimen();
		}
		else if(specimen instanceof TissueSpecimen)
		{
			return new TissueSpecimen();
		}
		
		return new Specimen();
	}
	
	private static String pattern="MM-dd-yyyy";
	/**
	 * @param date String representation of date.
	 * @param pattern Date pattern to be used.
	 * @return Month of the given date.
	 * @author mandar_deshmukh 
	 */
	public static int getMonth(String date,String pattern)
	{
		int month=0;
		month = getCalendar(date, pattern).get(Calendar.MONTH   );
		month=month+1;
		return month;
	}

	public static int getMonth(String date)
	{
		int month=0;
		
		month = getCalendar(date, pattern).get(Calendar.MONTH   );
		month=month+1;
		return month;
	}

	/**
	 * @param date String representation of date.
	 * @param pattern Date pattern to be used.
	 * @return Day of the given date.
	 * @author mandar_deshmukh 
	 */
	public static int getDay(String date,String pattern)
	{
		int day=0;
		day = getCalendar(date, pattern).get(Calendar.DAY_OF_MONTH  );
		return day;
	}

	public static int getDay(String date)
	{
		int day=0;
		day = getCalendar(date, pattern).get(Calendar.DAY_OF_MONTH  );
		return day;
	}
	
	/**
	 * @param date String representation of date.
	 * @param pattern Date pattern to be used.
	 * @return Year of the given date.
	 * @author mandar_deshmukh 
	 */
	public static int getYear(String date,String pattern)
	{
		int year=0;
		year =  getCalendar(date, pattern).get(Calendar.YEAR  );
		return year;
	}

	public static int getYear(String date)
	{
		int year=0;
		year =  getCalendar(date, pattern).get(Calendar.YEAR  );
		return year;
	}
	
	// Method to validate the date given by the user and return a calendar object for the date instance.
	private static Calendar getCalendar(String date,String pattern)
	{
		try
		{
			SimpleDateFormat dateformat = new SimpleDateFormat(pattern);
			Date givenDate = dateformat.parse(date);
			Calendar calendar =Calendar.getInstance();
			calendar.setTime(givenDate);
			return calendar; 
		}
		catch(Exception e)
		{
			Logger.out.error(e );
			return null;
		}
	}
	
	public static void main(String[] args)
	{
//		Utility u = new Utility();
		String dt="18-10-06";
		String pt="dd-MM-yy";

		System.out.println( Utility.getMonth(dt,pt )+"/"+ Utility.getDay(dt,pt)+"/"+ Utility.getYear(dt,pt ));

		dt="28-11-06";
		pt="dd-MM-yy";
		System.out.println( Utility.getMonth(dt,pt )+"/"+ Utility.getDay(dt,pt)+"/"+ Utility.getYear(dt,pt ));

		dt="18-21-06";
		pt="MM-dd-yy";
		System.out.println( Utility.getMonth(dt,pt )+"/"+ Utility.getDay(dt,pt)+"/"+ Utility.getYear(dt,pt ));

		dt="18-asa-06";
		pt="dd-MM-yy";
		System.out.println( Utility.getMonth(dt,pt )+"/"+ Utility.getDay(dt,pt)+"/"+ Utility.getYear(dt,pt ));
	}
}