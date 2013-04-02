/**
 */

package edu.wustl.catissuecore.dto;

import java.util.List;


public class OrderStatusDTO {
	private String status;
	private List<String> specimensWithError;
	private Long orderId;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getSpecimensWithError() {
		return specimensWithError;
	}
	public void setSpecimensWithError(List<String> specimensWithError) {
		this.specimensWithError = specimensWithError;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
}
