
package edu.wustl.catissuecore.privatepublicmigrator;

import java.io.IOException;

import edu.wustl.catissuecore.privatepublicmigrator.MaskUsingDEMetatdata;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * @author prashant_bandal
 *
 */
public class MaskData
{

	/**
	 * Runtime instance.
	 */
	private Runtime runtime;

	/**
	 * main method.
	 * @param args arguments.
	 */
	public static void main(String[] args)
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
		MaskData mig = new MaskData();
		mig.init();
		try
		{
			mig.maskData();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * init method.
	 */
	public void init()
	{
		ApplicationProperties.initBundle("commands");
		runtime = Runtime.getRuntime();
	}


	/**
	 * This method mask data.
	 * @throws Throwable Throwable.
	 */
	private void maskData() throws Throwable
	{
		MaskUsingDEMetatdata mask = new MaskUsingDEMetatdata();

		mask.maskIdentifiedData();

	}

}
