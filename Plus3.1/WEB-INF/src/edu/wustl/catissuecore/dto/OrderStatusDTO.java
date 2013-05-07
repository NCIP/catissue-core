/**
 */

package edu.wustl.catissuecore.dto;

import java.util.ArrayList;
import java.util.List;



public class OrderStatusDTO {
	private String status;
	private Long orderId;
	private List<OrderErrorDTO> orderErrorDTOs;
	
	public OrderStatusDTO() {
		this.orderErrorDTOs = new ArrayList<OrderErrorDTO>();
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public List<OrderErrorDTO> getOrderErrorDTOs() {
		return orderErrorDTOs;
	}
	public void setOrderErrorDTOs(List<OrderErrorDTO> orderErrorDTOs) {
		this.orderErrorDTOs = orderErrorDTOs;
	}
	
}
