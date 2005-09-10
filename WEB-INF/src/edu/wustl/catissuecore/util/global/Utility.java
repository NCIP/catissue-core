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
import java.util.Date;

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
	
//	public static void main(String[] args)
//    {
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
//    }
}
