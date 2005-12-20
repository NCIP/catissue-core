/**
 * <p>Title: HTTPMessage Class>
 * <p>Description:	This is the wrapper class over error messages from the server.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Dec 15, 2005
 */

package edu.wustl.catissuecore.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the wrapper class over error messages from the server.
 * @author aniruddha_phadnis
 */
public class HTTPMessage implements Serializable
{
	private List messageList;
	
	private String responseStatus;
	
	private String sessionId;
	
	public HTTPMessage()
	{
		messageList = new ArrayList();
	}
	
	public HTTPMessage(List messageList)
	{
		this.messageList = messageList;
	}
	
	/**
     * Returns the list of error messages.
     * @return the list of error messages.
     * @see #setMessageList(List)
     */
	public List getMessageList()
	{
		return messageList;
	}
	
	 /**
     * Sets the list of error messages.
     * @param message the list of error messages.
     * @see #getMessageList()
     */
	public void setMessageList(List message)
	{
		this.messageList = message;
	}
	
	/**
     * Adds an error object to the list.
     * @param object an error object.
     * @see #getMessageList()
     * @see #setMessageList(List)
     */
	public void addMessage(Object object)
	{
		messageList.add(object);
	}
	
	
	public String getResponseStatus()
	{
		return responseStatus;
	}
	
	public void setResponseStatus(String responseStatus)
	{
		this.responseStatus = responseStatus;
	}

	public String getSessionId()
	{
		return sessionId;
	}
	
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
}