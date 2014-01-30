
package com.krishagni.catissueplus.events.collectionprotocols;

import com.krishagni.catissueplus.events.RequestEvent;

public class ReqCollProtocolDetailEvent extends RequestEvent {

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
