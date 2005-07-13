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
	
	public static Date parseDate(String date) throws ParseException
	{
		try
		{
			StringTokenizer tokenizer = new StringTokenizer(date,"-");
			int yyyy =  Integer.parseInt(tokenizer.nextToken());
			int mm =  Integer.parseInt(tokenizer.nextToken());
			int dd =  Integer.parseInt(tokenizer.nextToken());
			
			Calendar cal = Calendar.getInstance();
			cal.set(yyyy,mm-1,dd);
			return cal.getTime();
		}
		catch(Exception e)
		{
			throw new ParseException("Date '"+date+"' is not in format of YYYY-MM-DD",0);
		}
	}
	
	public static String parseDateToString(Date date) 
	{
	    String str = new String();
	    try
	    {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        
	        str = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE);
	        
	    }
	    catch(Exception excp)
	    {
	        Logger.out.error(excp.getMessage());
	    }
	    return str;
	}
	
	public static Date parseTime(String date,int hours,int minutes,String AMPM) throws ParseException
	{
	    try
	    {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(parseDate(date));
			cal.set(Calendar.HOUR,hours);
			cal.set(Calendar.MINUTE,minutes);
			cal.set(Calendar.AM_PM,(AMPM.equals("AM")?0:1));
			
	        return cal.getTime();
	    }
	    catch(Exception excp)
	    {
	        throw new ParseException("Date '"+date+"' is not in format of YYYY-MM-DD",0);	
	    }
	}

	public static void main(String[] args)
    {
	    try{
	      String date = "2005-10-22 09:42 AM";  
	      String pattern = "yyyy-MM-dd HH:mm aa";
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
