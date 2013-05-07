/**
 */

package edu.wustl.catissuecore.dto;



public class OrderItemDTO {
	
	String specLabel;
	String specimenType;
	String specimenClass;
    Double availableQuantity;
    Double requestedQuantity;
    Double distributedQuantity=null;
    String status;
    String description;
    Long OrderItemId;
    Long specimenId;
	public String getSpecLabel() {
		return specLabel;
	}
	public void setSpecLabel(String specLabel) {
		this.specLabel = specLabel;
	}
	public String getSpecimenType() {
		return specimenType;
	}
	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}
	public String getSpecimenClass() {
		return specimenClass;
	}
	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}
	public Double getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(Double availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	public Double getRequestedQuantity() {
		return requestedQuantity;
	}
	public void setRequestedQuantity(Double requestedQuantity) {
		this.requestedQuantity = requestedQuantity;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getOrderItemId() {
		return OrderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		OrderItemId = orderItemId;
	}
	public Long getSpecimenId() {
		return specimenId;
	}
	public void setSpecimenId(Long specimenId) {
		this.specimenId = specimenId;
	}
	public Double getDistributedQuantity() {
		return distributedQuantity;
	}
	public void setDistributedQuantity(Double distributedQuantity) {
		this.distributedQuantity = distributedQuantity;
	}
	
}
