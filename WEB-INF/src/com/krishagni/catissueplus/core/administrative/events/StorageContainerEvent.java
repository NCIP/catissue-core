package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StorageContainerEvent extends ResponseEvent {
	private StorageContainerDetail container;

	public StorageContainerDetail getContainer() {
		return container;
	}

	public void setContainer(StorageContainerDetail container) {
		this.container = container;
	}
	
	public static StorageContainerEvent ok(StorageContainerDetail input) {
		StorageContainerEvent resp = new StorageContainerEvent();
		resp.setStatus(EventStatus.OK);
		resp.setContainer(input);
		return resp;
	}
	
	public static StorageContainerEvent notFound(Long id) {
		StorageContainerDetail detail = new StorageContainerDetail();
		detail.setId(id);
		
		StorageContainerEvent resp = new StorageContainerEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setContainer(detail);
		return resp;
	}
	
	public static StorageContainerEvent serverError(Exception e) {
		StorageContainerEvent resp = new StorageContainerEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}
