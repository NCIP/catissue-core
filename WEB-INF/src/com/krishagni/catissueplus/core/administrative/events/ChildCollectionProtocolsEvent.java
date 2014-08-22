
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ChildCollectionProtocolsEvent extends ResponseEvent {

	private List<CollectionProtocolSummary> childProtocols = new ArrayList<CollectionProtocolSummary>();

	public List<CollectionProtocolSummary> getChildProtocols() {
		return childProtocols;
	}

	public void setChildProtocols(List<CollectionProtocolSummary> childProtocols) {
		this.childProtocols = childProtocols;
	}

	public static ChildCollectionProtocolsEvent ok(List<CollectionProtocolSummary> list) {
		ChildCollectionProtocolsEvent event = new ChildCollectionProtocolsEvent();
		event.setChildProtocols(list);
		event.setStatus(EventStatus.OK);
		return event;
	}

}
