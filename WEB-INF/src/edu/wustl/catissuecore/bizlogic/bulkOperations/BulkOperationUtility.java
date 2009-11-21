package edu.wustl.catissuecore.bizlogic.bulkOperations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import edu.wustl.catissuecore.util.global.Constants;

/**
 * Bulk Operations from UI Utility.
 * @author sagar_baldwa
 *
 */
public class BulkOperationUtility
{
	/**
	 * Get CatissueInstallProperties.
	 * @return Properties.
	 */
	public static Properties getCatissueInstallProperties()
	{
		Properties props = new Properties();
		try
		{
			FileInputStream propFile = new FileInputStream(
					Constants.CATISSUE_INTSALL_PROPERTIES_FILE);
			props.load(propFile);
		}
		catch (FileNotFoundException fnfException) 
		{
			fnfException.printStackTrace();
		}
		catch (IOException ioException) 
		{
			ioException.printStackTrace();
		}
		return props;
	}
}