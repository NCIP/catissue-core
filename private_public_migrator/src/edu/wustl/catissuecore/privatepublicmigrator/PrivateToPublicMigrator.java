
package edu.wustl.catissuecore.privatepublicmigrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.wustl.common.util.global.ApplicationProperties;

/**
 *
 * @author prashant_bandal
 *
 */
public class PrivateToPublicMigrator
{

	/**
	 * configure Logger.
	 */
	//LoggerConfig.configureLogger(System.getProperty("user.dir"));

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
		final PrivateToPublicMigrator mig = new PrivateToPublicMigrator();
		mig.init();
		try
		{
			mig.migrateData();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (final Throwable e)
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
		this.runtime = Runtime.getRuntime();
	}

	/**
	 * This method migrates Data.
	 * @throws Throwable Throwable
	 */
	public void migrateData() throws Throwable
	{
		this.createStagingPrivateInstance();
		this.maskData();
		this.importDataInPublicInstance();
	}

	/**
	 * This method mask data.
	 * @throws Throwable Throwable.
	 */
	private void maskData() throws Throwable
	{
		// run simple JDBC queries to mask data
		// use java date and random abilities where needed  }  private createStagingPrivateInstance()  {
		//export from production
		// delete private staging schema
		//import to private staging

		final MaskUsingDEMetatdata mask = new MaskUsingDEMetatdata();

		mask.maskIdentifiedData();

	}

	/**
	 * This method create Staging Private Instance.
	 * @throws Exception Exception
	 */
	private void createStagingPrivateInstance() throws Exception
	{
		//TODO stop public jboss and grid service -- do not bother now --

		final String dropPublicDBCmd = ApplicationProperties.getValue("createPrivateDump");
		this.executeCommand(dropPublicDBCmd);

		final String createPubliDumpCmd = ApplicationProperties.getValue("dropStagingDB");
		this.executeCommand(createPubliDumpCmd);

		final String createPublicDB = ApplicationProperties.getValue("createStagingDB");
		this.executeCommand(createPublicDB);
		//TODO start public jboss and grid service -- do not bother now --
	}

	/**
	 * This method import Data In Public Instance.
	 * @throws Exception Exception
	 */
	private void importDataInPublicInstance() throws Exception
	{
		final String dropPrivateDBCmd = ApplicationProperties.getValue("createStagingDump");
		this.executeCommand(dropPrivateDBCmd);

		final String createPrivateDumpCmd = ApplicationProperties.getValue("dropPublicDB");
		this.executeCommand(createPrivateDumpCmd);

		final String createPrivateDB = ApplicationProperties.getValue("createPublicDB");
		this.executeCommand(createPrivateDB);
	}

	/**
	 * This method configure Masking.
	 * @throws Exception Exception
	 */
	private void configureMasking() throws Exception
	{

		final String dropPrivateDBCmd = ApplicationProperties.getValue("createStagingDump");
		this.executeCommand(dropPrivateDBCmd);
	}

	/**
	 * This method executes Command.
	 * @param command command
	 * @throws Exception Exception
	 */
	private void executeCommand(String command) throws Exception
	{
		Process process = null;

		if (!command.equalsIgnoreCase(""))
		{
			process = this.runtime.exec(command);

			// any error message?
			final StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream());

			// any output?
			final StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream());

			// kick them off
			errorGobbler.start();
			outputGobbler.start();

			final int i = process.waitFor();
			process.destroy();
			System.out.println("exit val of process " + process.exitValue());
			System.out.println(command + ":" + i);
		}
	}
}

/**
 *
 * @author prashant_bandal
 *
 */
class StreamGobbler extends Thread
{

	/**
	 * Specify InputStream object.
	 */
	InputStream is;

	/**
	 * Constructor.
	 * @param is InputStream object
	 */
	StreamGobbler(InputStream is)
	{
		this.is = is;
	}


	/**
	 * run method.
	 *
	 */
	@Override
	public void run()
	{
		try
		{
			final InputStreamReader isr = new InputStreamReader(this.is);
			final BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{
				System.out.println(line);
			}
		}
		catch (final IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

}
