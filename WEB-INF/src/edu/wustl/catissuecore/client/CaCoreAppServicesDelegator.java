/**
 * <p>Title: CaCoreAppServicesDelegator Class>
 * <p>Description:	This class contains the basic methods that are required
 * for HTTP APIs. It just passes on the request at proper place.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jan 10, 2006
 */

package edu.wustl.catissuecore.client;

import java.util.List;

import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the basic methods that are required for HTTP APIs. 
 * It just passes on the request at proper place.
 * @author aniruddha_phadnis
 */
public class CaCoreAppServicesDelegator
{
	public boolean delegateLogin(String userName,String password) throws Exception
	{
		CaTissueHTTPClient httpClient = CaTissueHTTPClient.getInstance();
		
		boolean status = httpClient.connect(userName,password);
		
		Logger.out.debug("****************** HTTP LOGIN STATUS : " + status);
		
		return status;
	}
	
	public boolean delegateLogout()// throws Exception
	{
		try
		{
			CaTissueHTTPClient httpClient = CaTissueHTTPClient.getInstance();
		
			boolean status = httpClient.disConnect();
		
			Logger.out.debug("****************** HTTP LOGOUT STATUS : " + status);
		}
		catch(Exception e)
		{
			
		}
		
		return false;
	}
	
	public Object delegateAdd(Object obj) throws Exception
	{
	    try
	    {
	        CaTissueHTTPClient httpClient = CaTissueHTTPClient.getInstance();
	        return httpClient.add(obj);
	    }
	    catch(Exception e)
	    {
	        Logger.out.error("Delegate Add-->" + e.getMessage());
	        throw e;
	    }
	}
	
	public Object delegateEdit(Object obj) throws Exception
	{
		try
		{
		    CaTissueHTTPClient httpClient = CaTissueHTTPClient.getInstance();
		    return httpClient.edit(obj);
		}
		catch(Exception e)
		{
		    Logger.out.error("Delegate Edit"+ e.getMessage());
	        throw e;
		}
	}
	
	public Object delegateDelete(Object obj) throws Exception
	{
		throw new Exception("Does not support delete");
	}
	
	public List delegateSearchFilter(List list) throws Exception
	{
		return list;
	}	
}