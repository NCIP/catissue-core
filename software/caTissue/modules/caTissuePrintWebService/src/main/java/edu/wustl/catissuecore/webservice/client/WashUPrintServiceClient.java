/**
 * <p>Title: WashUPrintServiceClient Class>
 * <p>Description:	WashUPrintServiceClient is a class that is default client of
 * the print web service  
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Amit Doshi
 * @version 1.00
 * Created on Nov 1, 2007
 */
package edu.wustl.catissuecore.webservice.client;

import java.util.LinkedHashMap;
import java.util.Map;
import edu.wustl.webservice.catissuecore.print.PrintServiceClient;
import edu.wustl.webservice.catissuecore.print.PrintXMLParser;

public class WashUPrintServiceClient implements PrintServiceClient{

	/*To implement the PrintService interface 
	 * this class define the actual functionality   
	 * of web method
	 */
	PrintXMLParser pxp = new PrintXMLParser();
	public String print(String xmlFormat) 
	{
		try 
		{
			Map objMap = pxp.getPrintMap(xmlFormat);
			printDomainObject(objMap);
			return "--from WS--Printing Successfully ---";

		}
		catch (Exception e) 
		{
			return "Printing Failed";
		}
	}

	
	void printDomainObject(Map obj) {
		System.out.println(obj);
		// class =spe
		// id=12
		// label-abc
		// barcode =12

		// university will decide ..not in our scope.

	}
}