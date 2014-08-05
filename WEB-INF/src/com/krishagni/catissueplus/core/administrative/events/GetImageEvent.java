
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class GetImageEvent extends RequestEvent {

	private Long id;

	private String eqpImageId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEqpImageId() {
		return eqpImageId;
	}

	public void setEqpImageId(String eqpImageId) {
		this.eqpImageId = eqpImageId;
	}

}
