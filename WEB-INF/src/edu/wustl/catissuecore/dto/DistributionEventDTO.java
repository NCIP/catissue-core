package edu.wustl.catissuecore.dto;

import java.util.Date;

public class DistributionEventDTO {
	
	private String user;
	private Date eventDate;
	private Double distributedQuantity;
	private String comment;
	private Long id;
	private String siteName;
	
	public String getSiteName()
	{
		return siteName;
	}
	
	public void setSiteName(String siteName)
	{
		this.siteName = siteName;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public Double getDistributedQuantity() {
		return distributedQuantity;
	}
	public void setDistributedQuantity(Double distributedQuantity) {
		this.distributedQuantity = distributedQuantity;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	

}
