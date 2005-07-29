/*
 * Created on Apr 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.util.global;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import edu.wustl.common.util.logger.Logger;

/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Utility
{
	public static Date parseDate(String date,String pattern) throws ParseException
	{
		try
		{
		    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		    Date d = dateFormat.parse(date);
		    return d;
		}
		catch(Exception e)
		{
			throw new ParseException("Date '"+date+"' is not in format of YYYY-MM-DD",0);
		}
	}
	
	public static String parseDateToString(Date date, String pattern)
	{
	    String d = null;
	    
	    try
	    {
	        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		    d = dateFormat.format(date);
		    
	    }
	    catch(Exception excp)
	    {
	        Logger.out.error(excp.getMessage(),excp);
	    }
	    return d;
	}
	
	public static void main(String[] args)
    {
	    try{
	      String date = "10-22-2005";  
//	      String pattern = "yyyy-MM-dd";
	      String pattern = "MM-dd-yyyy";
	      Date d = parseDate(date,pattern);
	      String dd = parseDateToString(d,pattern);
	      System.out.println("Date........."+d);
	      System.out.println("String......."+dd);
	    }
	    catch(ParseException pexcp)
	    {
	        System.out.println("Exception"+pexcp.getMessage());
	    }
    }
}
