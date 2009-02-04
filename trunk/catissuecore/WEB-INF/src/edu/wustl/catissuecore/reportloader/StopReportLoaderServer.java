package edu.wustl.catissuecore.reportloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.log4j.PropertyConfigurator;

import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;


/**
 * @author vijay_pande
 * Program to stop the report loader server
 */
public class StopReportLoaderServer
{
	 /**
	  * Default entry point for the program
	 * @param args command line arguments
	 * @throws Exception Generic exception
	 */
	public static void main(String[] args)throws Exception
	 {
	
	    try 
	    {    
	    	// initialization of stopReportLoaderServer
	    	Variables.applicationHome = System.getProperty("user.dir");
			Logger.out = org.apache.log4j.Logger.getLogger("");
			Logger.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
			PropertyConfigurator.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
			System.setProperty("gov.nih.nci.security.configFile",
					"./catissuecore-properties"+File.separator+"ApplicationSecurityConfig.xml");
	    	XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
	    	// get port number of ReportLoaderServer
	    	int port=Integer.parseInt(XMLPropertyHandler.getValue("filepollerport"));
	    	// Create client socket to connect to server
	        Socket s = new Socket("localhost",port);
	        Logger.out.info(XMLPropertyHandler.getValue("deid.port"));
	        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true);
	        // send stop command to stop the server
	        out.write("stop");
	        s.close();
	        Logger.out.info("Message sent to stop server");
	        
	    } 
	    catch (Exception ex) 
	    {
	    	Logger.out.error("Error while stopping Report Loader server ",ex);
	    }
	 }

}
