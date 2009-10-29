
package edu.wustl.catissuecore.caties.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

/**
 * @author
 *
 */
public class StopServerThread
{

	/**
	 * logger Logger - Generic logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	/**
	 * logger.
	 */
	private static Logger logger = Logger.getCommonLogger(StopServerThread.class);

	/**
	 * Main method to stop caTIES servers.
	 * pass command line arguments to stop respective server
	 * @param args Comman line arguments
	 * @throws Exception Generic exception
	 */
	public static void main(String[] args) throws Exception
	{
		try
		{
			// Configuring logger properties
			PropertyConfigurator.configure(CommonServiceLocator.getInstance().getAppHome()
					+ File.separator + "logger.properties");
			System.setProperty("gov.nih.nci.security.configFile", "./catissuecore-properties"
					+ File.separator + "ApplicationSecurityConfig.xml");
			// get port number of DeidServer
			final int port = Integer.parseInt(args[0]);
			// Create client socket to connect to server
			final Socket s = new Socket("localhost", port);
			new BufferedReader(new InputStreamReader(s.getInputStream()));
			final PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()),
					true);
			// send stop command to stop the server
			out.write("stop");
			s.close();
			logger.info("Message sent to stop server");
		}
		catch (final UnknownHostException ex1)
		{
			StopServerThread.logger.error("Host not found" + ex1.getMessage(),ex1);
			ex1.printStackTrace();
		}
		catch (final SocketTimeoutException ex2)
		{
			StopServerThread.logger.error("Socket timed out" + ex2.getMessage(),ex2);
			ex2.printStackTrace();
		}
		catch (final IOException ex3)
		{
			StopServerThread.logger.error("IO exception" + ex3.getMessage(),ex3);
			ex3.printStackTrace();
		}
	}
}
