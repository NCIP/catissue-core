package edu.wustl.catissuecore.bizlogic.test;

import java.util.HashMap;
import java.util.Map;


public class TestCaseUtility
{
	
	private static Map objectMap = new HashMap();
	public static Object getObjectMap(Class className)
	{
		return objectMap.get(className.getName());
	}

	public static void setObjectMap(Object object, Class className)
	{
		objectMap.put(className.getName(), object);
	} 
}
