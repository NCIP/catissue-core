
package edu.wustl.catissuecore.dto;

import java.sql.Date;

/**
 * @author deepti_phadnis
 * 
 */
public class DisplayOrderDTO 
{

	private static final long serialVersionUID = -6976105130845820240L;

	/**
	 * String contains the order name
	 */
	private String orderName = null;

	/**
	 * String contains the distribution protocol
	 */
	private String distributionProtocolName;

	/**
	 * String contains the comments
	 */
	private String comments;
	
	private String requestorName;
	
	private Date requestedDate;
	
	private String requestorEmail;
	
	private String site="--select--";
	
	/**
	 * @param distributionProtocol String contains the distribution protocol
	 */
	public void setDistributionProtocolName(String setDistributionProtocolName)
	{
		this.distributionProtocolName = setDistributionProtocolName;
	}

	/**
	 * @return distributionProtocol
	 */
	public String getDistributionProtocolName()
	{
		return (this.distributionProtocolName);
	}

	/**
	 * @param comments String contains the comments
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}

	/**
	 * @return comments
	 */
	public String getComments()
	{
		return (this.comments);
	}

   public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getRequestorName() {
		return requestorName;
	}

	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}

	public String getRequestorEmail() {
		return requestorEmail;
	}

	public void setRequestorEmail(String requestorEmail) {
		this.requestorEmail = requestorEmail;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
}
