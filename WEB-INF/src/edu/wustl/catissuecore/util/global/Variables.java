/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */ 
package edu.wustl.catissuecore.util.global;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * @author aarti_sharma
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Variables extends edu.wustl.common.util.global.Variables
{
    public static Vector databaseDefinitions=new Vector();
    public static String databaseDriver=new String();
    public static String[] databasenames;
    public static String applicationCvsTag = new String();
    public static boolean isLoadFromCaties=false;

    public static String prepareColTypes(List dataColl)
    {
    	return prepareColTypes(dataColl, false);
    }
    
    public static String prepareColTypes(List dataColl, boolean createCheckBoxColumn)
    {
    	String colType = "";
    	if(dataColl!=null && !dataColl.isEmpty())
    	{
    		List rowDataColl = (List)dataColl.get(0);

    		Iterator it = rowDataColl.iterator();
    		if(createCheckBoxColumn == true)
    			colType="ch,";
        	while(it.hasNext())
        	{
        		Object obj = it.next();
        		if(obj!=null && obj instanceof Number)
        		{
        			colType = colType + "int,";
        		}
        		else if(obj!=null && obj instanceof Date)
        		{
        			colType = colType + "date,";
        		}
        		else
        		{
        			colType = colType + "str,";
        		}
        	}    		
    	}
    	if(colType.length()>0)
    	{
    		colType = colType.substring(0,colType.length()-1);
    	}
    	return colType;
    }
    
    public static void main(String[] args)
	{
    	List list = new ArrayList();
    	
    	List a = new ArrayList();
    	a.add("As");
    	a.add("1");
    	a.add("1.5");
    	a.add("true");
    	a.add("BB");    	
    	
    	list.add(a);
    	
    	String str = prepareColTypes(list);
    	System.out.println(str);
	}
}