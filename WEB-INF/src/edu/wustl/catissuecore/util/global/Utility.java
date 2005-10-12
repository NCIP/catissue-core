/**
 * <p>Title: Utility Class>
 * <p>Description:  Utility Class contain general methods used through out the application. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.util.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.common.util.logger.Logger;

/**
 * Utility Class contain general methods used through out the application.
 * @author kapil_kaveeshwar
 */

public class Utility
{
    /**
     * Parses the string format of date in the given format and returns the Data object.
     * @param date the string containing date.
     * @param pattern the pattern in which the date is present.
     * @return the string format of date in the given format and returns the Data object.
     * @throws ParseException
     */
	public static Date parseDate(String date,String pattern) throws ParseException
	{
		if(date!=null && !date.trim().equals(""))
		{
			try
			{
			    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			    Date dateObj = dateFormat.parse(date);
			    return dateObj;
			}
			catch(Exception e)
			{
				throw new ParseException("Date '"+date+"' is not in format of "+pattern,0);
			}
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Parses the Date in given format and returns the string representation.
	 * @param date the Date to be parsed.
	 * @param pattern the pattern of the date.
	 * @return
	 */
	public static String parseDateToString(Date date, String pattern)
	{
	    String d = "";
	    //TODO Check for null
	    if(date!=null)
	    {
		    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			d = dateFormat.format(date);
	    }
	    return d;
	}
	
	public static String toString(Object obj)
	{
		if(obj == null)
			return "";
		
		return obj.toString();
	}
	
	public static String[] getTime(Date date)
	{
		String []time =new String[2];
		Calendar cal = Calendar.getInstance();
 		cal.setTime(date);
 		time[0]= Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
 		time[1]= Integer.toString(cal.get(Calendar.MINUTE));
 		return time;
	}
	
	public static Long[] toLongArray(Collection collection)
	{
		Logger.out.debug(collection.toArray().getClass().getName());
		
		Long obj[] = new Long[collection.size()];
		
		int index = 0;
		Iterator it = collection.iterator();
		while(it.hasNext())
		{
			obj[index] = (Long)it.next();
			Logger.out.debug("obj[index] "+obj[index].getClass().getName());
			index++;
		}
		return obj;
	}
	
	public static int toInt(Object obj)
	{
		int value=0;
		if(obj == null)
			return value;
		else
		{	Integer intObj = (Integer)obj;
			value=intObj.intValue() ;
			return value;
		}
	}

	public static double toDouble(Object obj)
	{
		double value=0;
		if(obj == null)
			return value;
		else
		{	Double dblObj = (Double)obj;
			value=dblObj.doubleValue() ;
			return value;
		}
	}
	
	public static Object[] addElement(Object[] array, Object obj)
	{
		Object newObjectArr[] = new Object[array.length+1];
		
		if(array instanceof String[])
			newObjectArr = new String[array.length+1];
		
		for (int i = 0; i < array.length; i++)
		{
			newObjectArr[i] = array[i];
		}
		newObjectArr[newObjectArr.length-1] = obj;
		return newObjectArr;
	}
	
	/**
	 * checking whether key's value is persisted or not
	 *
	 */
	public static boolean isPersistedValue(Map map,String key){
		Object obj = map.get(key);
		String val=null;
		if (obj!=null) 
		{
			val = obj.toString();
		}
		if((val!= null && !(val.equals("0"))) && !(val.equals("")))
			return true;
		else 
			return false; 
			
	}
	
	/**
     * Parses the fully qualified classname and returns only the classname.
     * @param fullyQualifiedName The fully qualified classname. 
     * @return The classname.
     */
    public static String parseClassName(String fullyQualifiedName)
    {
        try
        {
            return fullyQualifiedName.substring(fullyQualifiedName
                    .lastIndexOf(".") + 1);
        }
        catch (Exception e)
        {
            return fullyQualifiedName;
        }
    }
	
	//	public static void main(String[] args)
//  {
//	    try{
//	      String date = "2005-10-22";  
//	      String pattern = "yyyy-MM-dd HH:mm aa";
//	      Date d = parseDate(date,pattern);
//	      String dd = parseDateToString(d,pattern);
//	      System.out.println("Date........."+d);
//	      System.out.println("String......."+dd);
//	    }
//	    catch(ParseException pexcp)
//	    {
//	        System.out.println("Exception"+pexcp.getMessage());
//	    }
//  }
}