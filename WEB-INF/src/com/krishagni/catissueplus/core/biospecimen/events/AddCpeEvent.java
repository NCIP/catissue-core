package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class AddCpeEvent extends RequestEvent {
	private CollectionProtocolEventDetail cpe;

	public CollectionProtocolEventDetail getCpe() {
		return cpe;
	}

	public void setCpe(CollectionProtocolEventDetail cpe) {
		this.cpe = cpe;
	}
}
