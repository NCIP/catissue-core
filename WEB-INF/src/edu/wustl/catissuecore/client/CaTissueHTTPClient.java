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
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;

public class CaTissueHTTPClient
{
	private String httpSessionId;

	public boolean connect(String userName, String password) throws Exception
	{
		//FIXME : Write a method say, getURL() which will return the corresponding URL as per domainObject
		String servletURL = "http://localhost:8080/catissuecore/LoginHTTP.do";
		
		//Opens a connection to the specific URL
		URL url = new URL(servletURL);
		URLConnection con = url.openConnection();
		
		User user = new User();
		user.setLoginName(userName);
		user.setPassword(password);
		
		HTTPWrapperObject wrapperObject = new HTTPWrapperObject(user,Constants.LOGIN);
		
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
			System.out.println("\n-->" + messageList.get(i));
		}
		
		if(Constants.SUCCESS.equals(httpMessage.getResponseStatus()))
		{
			httpSessionId = httpMessage.getSessionId();
			return true;
		}
		
		return false;
	}
	
	private Object doOperation(AbstractDomainObject domainObject,String operation) throws Exception
	{
		String servletURL = "http://localhost:8080/catissuecore/InstituteHTTP.do;jsessionid=" + httpSessionId; //FIXME : Use of getURL function
		
		//Opens a connection to the specific URL
		URL url = new URL(servletURL);
		URLConnection con = url.openConnection();
		
		con.setDoOutput(true);
		con.setRequestProperty(Constants.CONTENT_TYPE,Constants.HTTP_API);
		ObjectOutputStream objectOutStream = new ObjectOutputStream(con.getOutputStream());
		
		HTTPWrapperObject wrapperObject = new HTTPWrapperObject(domainObject,operation);
		
		objectOutStream.writeObject(wrapperObject);
		objectOutStream.flush();
		objectOutStream.close();
		
		ObjectInputStream objectInStream = new ObjectInputStream(con.getInputStream());
		HTTPMessage httpMessage = (HTTPMessage)objectInStream.readObject();
		
		objectInStream.close();
		
		List messageList = httpMessage.getMessageList();
		
		for(int i=0;i<messageList.size();i++)
		{
			System.out.println("\n-->" + messageList.get(i));
		}
		
		return httpMessage;
	}
	
	public Object add(AbstractDomainObject domainObject) throws Exception
	{
		return doOperation(domainObject,Constants.ADD);
	}
	
	public Object edit(AbstractDomainObject domainObject) throws Exception
	{
		return doOperation(domainObject,Constants.EDIT);
	}
	
	public static void main(String [] args)
	{
		CaTissueHTTPClient client = new CaTissueHTTPClient();
		Institution inst = new Institution();
		
		inst.setName("Persistent Systems");
		inst.setSystemIdentifier(new Long("0"));
		
		try
		{
			System.out.println("****************** LOGIN STATUS : " + client.connect("admin@admin.com","login123"));
			client.add(inst);
		}
		catch(Exception e) { System.out.println(e); }
	}
}