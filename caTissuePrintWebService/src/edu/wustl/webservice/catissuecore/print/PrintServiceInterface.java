/**
 * <p>Title: PrintService Class>
 * <p>Description:	PrintService is an remote Interface that contains the method which is 
 * invoked by the print web service client. 
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Amit Doshi
 * @version 1.00
 * Created on Nov 1, 2007
 */

package edu.wustl.webservice.catissuecore.print;
import java.rmi.RemoteException;

public interface PrintServiceInterface 
{
	
	/*	The first step in creating a web service is to design and
	 *	code its endpoint interface, in which you declare the methods 
	 *	that a web service remote client may invoke on the service. 
	 */
	
	public String print(String xmlFormat) throws RemoteException;
}
