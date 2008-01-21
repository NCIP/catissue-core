package edu.wustl.catissuecore.printserviceclient;


import java.util.HashMap;

public class LabelPrinterFactory {
	
	private static HashMap printClassMap = new HashMap() ;
	public static LabelPrinter getInstance(String objectType) throws Exception
	{
		try
		{
			String className = PropertyHandler.getValue(objectType);
			if(className!=null)
			{
				printClassMap.put(objectType,Class.forName(className).newInstance());
			}
			else
			{	
				return null;
			}
			return (LabelPrinter)printClassMap.get(objectType);
			
		}
		catch (Exception e) {
			throw e;
			
		}
	}
}
