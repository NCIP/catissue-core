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


public class StopDeidServer
{
	 public static void main( String args[] )throws Exception
	 {
	
	    try 
	    {    
	    
	    	Variables.applicationHome = System.getProperty("user.dir");
			Logger.out = org.apache.log4j.Logger.getLogger("");
			Logger.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
			PropertyConfigurator.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
			System.setProperty("gov.nih.nci.security.configFile",
					"./catissuecore-properties"+File.separator+"ApplicationSecurityConfig.xml");
	    	XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
	    	int PORT=Integer.parseInt(XMLPropertyHandler.getValue("deid.port"));
	        Socket s = new Socket("localhost",PORT);
	        Logger.out.info(XMLPropertyHandler.getValue("deid.port"));
	        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        PrintWriter out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()),true);
	        out.write("stop");
	        s.close();
	        Logger.out.info("Message sent to stop server");
	        
	    } catch (UnknownHostException e2) {
	    } catch (SocketTimeoutException ex1) {
	    } catch (IOException exc) {
	    }
	 }

}
