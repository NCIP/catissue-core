package edu.wustl.catissuecore.printserviceclient;

import java.util.Properties;

/**
 * This class has functions to read PrintServiceImplementor Properties file.
 * @author falguni sachde
 *
 */
public class PropertyHandler {

	/**
	 * 
	 */
	private static Properties printimplClassProperties = null;


	/**
	 * @param path
	 * @throws Exception
	 */
	public static void init(String path) throws Exception
	{
		try{
			printimplClassProperties = new Properties();
			printimplClassProperties.load(PropertyHandler.class.getClassLoader().getResourceAsStream(path));					
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
			
	}
	
	/**
	 * Description:This method takes the property name as String argument and
	 * returns the properties value as String. 
	 * @param propertyName  name of property Key  
	 * @return String	property value
	 * @throws Exception
	 */
	public static String getValue(String propertyName) throws Exception
	{
		
		if (printimplClassProperties == null)
		{
			init("PrintServiceImplementor.properties");
		}
		return (String)printimplClassProperties.get(propertyName);

	}
}
