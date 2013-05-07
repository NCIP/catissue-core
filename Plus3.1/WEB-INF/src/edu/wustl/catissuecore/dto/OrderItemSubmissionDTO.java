/**
 */

package edu.wustl.catissuecore.dto;



public class OrderItemSubmissionDTO {
	
	String specimenLabel;
	Long specId;
	Double requestedQty;
	Double distQty;
	String status;
	String comments;
	Long orderitemId;
	
	public Long getSpecimenId() {
		return specId;
	}
	public void setSpecimenId(Long specLabel) {
		this.specId = specLabel;
	}
	public Double getDistQty() {
		return distQty;
	}
	public void setDistQty(Double distQty) {
		this.distQty = distQty;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getOrderitemId() {
		return orderitemId;
	}
	public void setOrderitemId(Long orderitemId) {
		this.orderitemId = orderitemId;
	}
	public String getSpecimenLabel() {
		return specimenLabel;
	}
	public void setSpecimenLabel(String specimenLabel) {
		this.specimenLabel = specimenLabel;
	}
	public Double getRequestedQty() {
		return requestedQty;
	}
	public void setRequestedQty(Double requestedQty) {
		this.requestedQty = requestedQty;
	}
	
}
