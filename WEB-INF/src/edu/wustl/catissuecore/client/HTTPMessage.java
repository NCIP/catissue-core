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
	
	private Long domainObjectId;
	
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
	
	/**
	 * Returns the Response Status of the operation
	 * @return the Response Status of the operation
	 */
	public String getResponseStatus()
	{
		return responseStatus;
	}
	
	/**
	 * Sets Response Status of the operation 
	 * @param responseStatus Response Status of the operation
	 */
	public void setResponseStatus(String responseStatus)
	{
		this.responseStatus = responseStatus;
	}

	/**
	 * Returns the Session ID 
	 * @return the Session ID
	 */
	public String getSessionId()
	{
		return sessionId;
	}
	
	/**
	 * Sets the Session ID
	 * @param sessionId the Session ID
	 */
	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}
	
	/**
	 * Sets the ID of Domain Object
	 * @param domainObjectId the ID of Domain Object
	 */
	public void setDomainObjectId(Long domainObjectId)
	{
	    this.domainObjectId=domainObjectId;
	}
	
	/**
	 * Returns the ID of Domain Object
	 * @return the ID of Domain Object
	 */
	public Long getDomainObjectId()
	{
	    return this.domainObjectId;
	}
}