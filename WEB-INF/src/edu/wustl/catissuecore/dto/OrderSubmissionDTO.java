/**
 */

package edu.wustl.catissuecore.dto;

import java.util.Collection;
import java.util.Date;



public class OrderSubmissionDTO {
	private Long id;
	private Long site;
	private Collection<OrderItemSubmissionDTO> OrderItemSubmissionDTOs;
	private String comments;
	private String orderName;
	private Long disptributionProtocolId;
	private String disptributionProtocolName;
	private String requestorEmail;
	private Long requestorId;
	private String distributorsComment;
	private Date requestedDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSite() {
		return site;
	}
	public void setSite(Long site) {
		this.site = site;
	}
	public Collection<OrderItemSubmissionDTO> getOrderItemSubmissionDTOs() {
		return OrderItemSubmissionDTOs;
	}
	public void setOrderItemSubmissionDTOs(
			Collection<OrderItemSubmissionDTO> orderItemSubmissionDTO) {
		OrderItemSubmissionDTOs = orderItemSubmissionDTO;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public Long getDisptributionProtocolId() {
		return disptributionProtocolId;
	}
	public void setDisptributionProtocolId(Long disptributionProtocolId) {
		this.disptributionProtocolId = disptributionProtocolId;
	}
	public String getRequestorEmail() {
		return requestorEmail;
	}
	public void setRequestorEmail(String requestorEmail) {
		this.requestorEmail = requestorEmail;
	}
	public Long getRequestorId()
	{
		return requestorId;
	}
	
	public void setRequestorId(Long requestorId)
	{
		this.requestorId = requestorId;
	}
	public String getDisptributionProtocolName() {
		return disptributionProtocolName;
	}
	public void setDisptributionProtocolName(String disptributionProtocolName) {
		this.disptributionProtocolName = disptributionProtocolName;
	}
	public String getDistributorsComment() {
		return distributorsComment;
	}
	public void setDistributorsComment(String distributorsComment) {
		this.distributorsComment = distributorsComment;
	}
	
	public Date getRequestedDate()
	{
		return requestedDate;
	}
	
	public void setRequestedDate(Date requestedDate)
	{
		this.requestedDate = requestedDate;
	}
		
}
