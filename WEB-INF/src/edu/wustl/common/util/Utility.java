/*
 * Created on Aug 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.util;

import java.lang.reflect.Method;

import edu.wustl.common.util.logger.Logger;


/**
 * @author kapil_kaveeshwar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Utility
{
	public static String createAccessorMethodName(String attr,boolean isSetter)
	{
		String firstChar = attr.substring(0,1);
		String str = "get"; 
		if(isSetter)
			str = "set"; 
		return str + firstChar.toUpperCase() + attr.substring(1);
	}
	
	public static Object getValueFor(Object obj, String attrName) throws Exception
	{
		//Create the getter method of attribute
		String methodName =  Utility.createAccessorMethodName(attrName,false);
		Class objClass = obj.getClass();
		Method method = objClass.getMethod(methodName, new Class[0]);

		return method.invoke(obj,new Object[0]);
	}
	
	public static Object getValueFor(Object obj, Method method) throws Exception
	{
		return method.invoke(obj,new Object[0]);
	}

    /**
     * @param objectIds
     * @return
     */
    public static String getArrayString(Object[] objectIds)
    {
        StringBuffer arrayStr = new StringBuffer();
        for(int i=0; i<objectIds.length; i++)
        {
            arrayStr.append(objectIds[i].toString()+",");
        }
        return arrayStr.toString();
    }
    
    public static Class getClassObject(String fullyQualifiedClassName)
    {
        Class className = null;
        try
        {
            className = Class.forName(fullyQualifiedClassName);
        }
        catch (ClassNotFoundException classNotExcp)
		{
			return null;
		}
        
        return className;
    }
	
}
