/**
 * <p>Title: PrintServiceImpl Class>
 * <p>Description:	PrintServiceImpl is a class which implements the PrintService 
 * interface and print  method. 
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Amit Doshi
 * @version 1.00
 * Created on Nov 1, 2007
 */
package edu.wustl.webservice.catissuecore.print;

import edu.wustl.catissuecore.webservice.client.PrintWebServiceFactory;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;

public class PrintServiceImpl implements PrintService{

	public String print(String xmlFormat) throws RemoteException {
		
		System.out.println(xmlFormat);
		PrintServiceClient printserviceClientObj=PrintWebServiceFactory.getPrintServiceClientObject();
		
		String msg=printserviceClientObj.print(xmlFormat);
		
		return msg;
	}


	
}
