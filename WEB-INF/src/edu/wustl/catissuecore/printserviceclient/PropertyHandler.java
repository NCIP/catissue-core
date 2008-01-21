package edu.wustl.catissuecore.printserviceclient;

import java.util.Properties;

/**
 * @author abhijit_naik
 *
 */
public class PropertyHandler {

	private static Properties printimplClassProperties = null;


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
	 * <p>
	 * Description:This method takes the property name as String argument and
	 * returns the properties value as String. 
	 * </p>
	 */

	public static String getValue(String propertyName) throws Exception
	{
		System.out.println("Inside PropertyHandler.init factory..");
		if (printimplClassProperties == null)
		{
			init("PrintServiceImplementor.properties");
		}
		return (String)printimplClassProperties.get(propertyName);

	}
}
