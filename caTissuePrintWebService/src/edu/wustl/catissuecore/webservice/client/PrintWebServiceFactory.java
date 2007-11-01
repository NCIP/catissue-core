/**
 * <p>Title: PrintWebServiceFactory Class>
 * <p>Description:	PrintWebServiceFactory is a class that returns the object
 * of client for which print method is invoked. 
 * as per the domain objects.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Amit Doshi
 * @version 1.00
 * Created on Nov 1, 2007
 */

package edu.wustl.catissuecore.webservice.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import edu.wustl.webservice.catissuecore.print.PrintServiceClient;
public class PrintWebServiceFactory {

	public static PrintServiceClient getPrintServiceClientObject() 
	{

		PrintServiceClient printObj=null;			
		String classname="edu.wustl.catissuecore.webservice.client.WashUPrintServiceClient";
		System.out.println(classname);
		try 
			{
				printObj = (PrintServiceClient)Class.forName(classname).newInstance();
			} catch (InstantiationException e) {

				e.printStackTrace();
				
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}


		return printObj;

	}
}
