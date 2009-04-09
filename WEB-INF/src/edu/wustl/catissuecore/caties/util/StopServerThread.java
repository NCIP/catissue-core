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
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;

public class StopServerThread 
{
	/**
	 * Main method to stop caTIES servers
	 * pass command line arguments to stop respective server
	 * @param args Comman line arguments
	 * @throws Exception Generic exception
	 */
	 public static void main(String args[])throws Exception
	 {	
	    try 
	    {    
	    	// initialization 
	    	//Variables.applicationHome = System.getProperty("user.dir");
			Logger.out = org.apache.log4j.Logger.getLogger("");
			LoggerConfig.configureLogger(CaTIESConstants.LOGGER_GENERAL);
			// Configuring logger properties
			PropertyConfigurator.configure(CommonServiceLocator.getInstance().getAppHome() + File.separator+"logger.properties");
			System.setProperty("gov.nih.nci.security.configFile",
					"./catissuecore-properties"+File.separator+"ApplicationSecurityConfig.xml");	
			// get port number of DeidServer
	    	int port=Integer.parseInt(args[0]);
	    	// Create client socket to connect to server
	        Socket s = new Socket("localhost",port);
	        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true);
	        // send stop command to stop the server
	        out.write("stop");
	        s.close();
	        Logger.out.info("Message sent to stop server");	        
	    } 
	    catch (UnknownHostException ex1) 
	    {
	    	Logger.out.error("Host not found"+ex1);
	    }
	    catch (SocketTimeoutException ex2) 
	    {
	    	Logger.out.error("Socket timed out"+ex2);
	    } 
	    catch (IOException ex3) 
	    {
	    	Logger.out.error("IO exception"+ex3);
	    }
	 }
}
