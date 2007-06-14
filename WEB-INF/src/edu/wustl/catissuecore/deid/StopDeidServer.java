package edu.wustl.catissuecore.deid;

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

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;


/**
 * This class is to provide method to stop deid server 
 * @author vijay_pande
 *
 */
public class StopDeidServer
{
	/**
	 * Main method to stop deid server
	 * @param args Comman line arguments
	 * @throws Exception Generic exception
	 */
	 public static void main(String args[])throws Exception
	 {
	
	    try 
	    {    
	    	// initialization of stopDeidServer
	    	Variables.applicationHome = System.getProperty("user.dir");
			Logger.out = org.apache.log4j.Logger.getLogger("");
			Logger.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
			PropertyConfigurator.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
			System.setProperty("gov.nih.nci.security.configFile",
					"./catissuecore-properties"+File.separator+"ApplicationSecurityConfig.xml");
	    	XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
	    	// get port number of DeidServer
	    	int PORT=Integer.parseInt(XMLPropertyHandler.getValue("deid.port"));
	    	// Create client socket to connect to server
	        Socket s = new Socket("localhost",PORT);
	        Logger.out.info(XMLPropertyHandler.getValue("deid.port"));
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
