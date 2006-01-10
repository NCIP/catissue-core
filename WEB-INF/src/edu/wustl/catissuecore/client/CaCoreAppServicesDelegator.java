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

/**
 * This class contains the basic methods that are required for HTTP APIs. 
 * It just passes on the request at proper place.
 * @author aniruddha_phadnis
 */
public class CaCoreAppServicesDelegator
{
	public boolean login(String userName,String password) throws Exception
	{
		return false;
	}
	
	public boolean logout() throws Exception
	{
		return false;
	}
	
	public Object add(Object domainbject) throws Exception
	{
		return null;
	}
	
	public Object edit(Object domainObject) throws Exception
	{
		return null;
	}
	
	public Object delete(Object domainObject) throws Exception
	{
		return null;
	}
	
	public List search(String className, Object domainObject) throws Exception
	{
		return null;
	}	
}