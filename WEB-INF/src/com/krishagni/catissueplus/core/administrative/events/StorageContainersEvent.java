package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StorageContainersEvent extends ResponseEvent {
	private List<StorageContainerSummary> containers;

	public List<StorageContainerSummary> getContainers() {
		return containers;
	}

	public void setContainers(List<StorageContainerSummary> containers) {
		this.containers = containers;
	}
	
	public static StorageContainersEvent ok(List<StorageContainerSummary> containers) {
		StorageContainersEvent resp = new StorageContainersEvent();
		resp.setStatus(EventStatus.OK);
		resp.setContainers(containers);
		return resp;
	}
	
	public static StorageContainersEvent serverError(Exception e) {
		StorageContainersEvent resp = new StorageContainersEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}