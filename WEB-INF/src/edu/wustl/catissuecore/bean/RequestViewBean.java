/**
 * <p>Title: RequestViewBean Class>
 * <p>Description:	This class contains attributes to display on RequestListAdministratorView.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Oct 05,2006
 */

package edu.wustl.catissuecore.bean;


import java.io.Serializable;

public class RequestViewBean implements Serializable
{	
	
	private static final long serialVersionUID = 1L;
	/**
	 * The serial number to display.
	 */
	private int serialNo;
	/**
	 * The Id associated with the request order.
	 */
	private String requestId;
	/**
	 * The name of the request Order.
	 */
	private String orderName;
	/**
	 * The name of the distribution protocol associated with the request order.
	 */
	private String distributionProtocol;
	/**
	 * The user name who has placed the request order.
	 */
	private String requestedBy;
	/**
	 * The email of the user who has placed the request order.
	 */
	private String email;
	/**
	 * The date on which the request order was placed.
	 */
	private String requestedDate;
	/**
	 * The current status of the request order.
	 */
	private String status;
	/**
	 * The comments associated with the request order.
	 */
	private String comments;
	/**
	 * The distribution Protocol id.
	 */
	private String distributionProtocolId;
	

	/**
	 * @return comments The comments associated with the request order.
	 */
	public String getComments()
	{
		return comments;
	}

	/**
	 * @param comments The comments associated with the request order.
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}

	/**
	 * @return distributionProtocol The name of the distribution protocol associated with the request order.
	 */
	public String getDistributionProtocol()
	{
		return distributionProtocol;
	}

	/**
	 * @param distributionProtocol The name of the distribution protocol associated with the request order.
	 */
	public void setDistributionProtocol(String distributionProtocol)
	{
		this.distributionProtocol = distributionProtocol;
	}

	/**
	 * @return email The email of the user who has placed the request order.
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email The email of the user who has placed the request order.
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * @return orderName The name of the request Order.
	 */
	public String getOrderName()
	{
		return orderName;
	}

	/**
	 * @param orderName The name of the request Order.
	 */
	public void setOrderName(String orderName)
	{
		this.orderName = orderName;
	}

	/**
	 * @return requestedDate The date on which the request order was placed.
	 */
	public String getRequestedDate()
	{
		return requestedDate;
	}

	/** 
	 * @param requestedDate The date on which the request order was placed.
	 */
	public void setRequestedDate(String requestedDate)
	{
		this.requestedDate = requestedDate;
	}

	/**
	 * @return requestedBy The user name who has placed the request order.
	 */
	public String getRequestedBy()
	{
		return requestedBy;
	}

	/**
	 * @param requestedBy The user name who has placed the request order.
	 */
	public void setRequestedBy(String requestedBy)
	{
		this.requestedBy = requestedBy;
	}

	/**
	 * @return status The current status of the request order.
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status The current status of the request order.
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	
	/**
	 * @return the requestId The Id associated with the request order.
	 */
	public String getRequestId()
	{
		return requestId;
	}

	
	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId)
	{
		this.requestId = requestId;
	}

	
	/**
	 * @return the serialNo
	 */
	public int getSerialNo()
	{
		return serialNo;
	}

	
	/**
	 * @param serialNo the serialNo to set
	 */
	public void setSerialNo(int serialNo)
	{
		this.serialNo = serialNo;
	}

	
	/**
	 * @return the distributionProtocolId
	 */
	public String getDistributionProtocolId()
	{
		return distributionProtocolId;
	}

	
	/**
	 * @param distributionProtocolId the distributionProtocolId to set
	 */
	public void setDistributionProtocolId(String distributionProtocolId)
	{
		this.distributionProtocolId = distributionProtocolId;
	}

}
