/**
 * <p>Title: CaTissueHTTPClient Class>
 * <p>Description:	This is the wrapper class over HTTP API that provides a 
 * functionality to APIs to connect& access the caTISSUE Core Application.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Dec 15, 2005
 */

package edu.wustl.catissuecore.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class CaTissueHTTPClient
{
    private static CaTissueHTTPClient caTissueHTTPClient = new CaTissueHTTPClient();

    private static final String servletURL="http://localhost:8080/catissuecore/";
    
    private String httpSessionId;
    
    private CaTissueHTTPClient()
    {
        
    }
    public static CaTissueHTTPClient getInstance()
    {
        return caTissueHTTPClient;
    }
    
	public boolean connect(String userName, String password) throws Exception
	{
	    User user = new User();
		user.setLoginName(userName);
		user.setPassword(password);
		
		HTTPWrapperObject wrapperObject = new HTTPWrapperObject(user,Constants.LOGIN);
	    
		HTTPMessage httpMessage=(HTTPMessage)sendData(wrapperObject, servletURL+"LoginHTTP.do");
		
		if(Constants.SUCCESS.equals(httpMessage.getResponseStatus()))
		{
			httpSessionId = httpMessage.getSessionId();
			return true;
		}
		
		return false;
	}
	
	public boolean disConnect() throws Exception
	{
	    HTTPWrapperObject wrapperObject = new HTTPWrapperObject(null, Constants.LOGOUT);
	    
	    HTTPMessage httpMessage =(HTTPMessage)sendData(wrapperObject, servletURL+"LogoutHTTP.do;jsessionid="+httpSessionId);
	    
	    if(Constants.SUCCESS.equals(httpMessage.getResponseStatus()))
		{
			return true;
		}
		
		return false;
	}
	
	private Object doOperation(Object domainObject,String operation) throws Exception
	{
	    HTTPWrapperObject wrapperObject = new HTTPWrapperObject(domainObject,operation);
	    
	    HTTPMessage httpMessage =(HTTPMessage)sendData(wrapperObject, servletURL+"OperationHTTP.do;jsessionid="+httpSessionId);
	    
	    if ( (operation.equals(Constants.ADD)) && (httpMessage.getResponseStatus().equals(Constants.SUCCESS))  && 
	         (httpMessage.getDomainObjectId() != null) )
	    {
		    Class[] argClass=new Class[] {Long.class};
		    
		    Method setIdMethod=domainObject.getClass().getMethod("setId",argClass);
		    
		    Long[] arguments=new Long[]{httpMessage.getDomainObjectId()};
		    
		    setIdMethod.invoke(domainObject, arguments);
	    }
	    else if(httpMessage.getResponseStatus().equals(Constants.FAILURE))
	    {
	        String exceptions=new String();
	        
	        List messageList = httpMessage.getMessageList();
			
			for(int i=0;i<messageList.size();i++)
			{
			    exceptions +=(String)messageList.get(i);
			}
	        
	        throw new Exception(exceptions);
	    }
	    
	    return domainObject;
	}
	
	private Object sendData(HTTPWrapperObject wrapperObject, String urlString) throws Exception
	{
	    // Opens a connection to the specific URL
		URL url = new URL(urlString);
		URLConnection con = url.openConnection();
		
		con.setDoOutput(true);
		con.setRequestProperty(Constants.CONTENT_TYPE,Constants.HTTP_API);
		ObjectOutputStream objectOutStream = new ObjectOutputStream(con.getOutputStream());
		
				
		objectOutStream.writeObject(wrapperObject);
		objectOutStream.flush();
		objectOutStream.close();
		
		ObjectInputStream objectInStream = new ObjectInputStream(con.getInputStream());
		HTTPMessage httpMessage = (HTTPMessage)objectInStream.readObject();
		
		objectInStream.close();
		
		List messageList = httpMessage.getMessageList();
		
		for(int i=0;i<messageList.size();i++)
		{
		    Logger.out.debug("HTTPClient sendData-->" + messageList.get(i));
		}
		
		return httpMessage;
	}
	
	public Object add(Object domainObject) throws Exception
	{
		return doOperation(domainObject,Constants.ADD);
	}
	
	public Object edit(Object domainObject) throws Exception
	{
		return doOperation(domainObject,Constants.EDIT);
	}
	
}