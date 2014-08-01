package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class GetAllStorageContainersEvent extends ResponseEvent {

	private List<StorageContainerSummary> summary;

	public List<StorageContainerSummary> getSummary() {
		return summary;
	}

	public void setSummary(List<StorageContainerSummary> summary) {
		this.summary = summary;
	}

	public static GetAllStorageContainersEvent ok(
			List<StorageContainerSummary> summary) {
		GetAllStorageContainersEvent event = new GetAllStorageContainersEvent();
		event.setStatus(EventStatus.OK);
		event.setSummary(summary);
		return event;
	}
}
