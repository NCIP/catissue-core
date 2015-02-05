package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StorageContainerUpdatedEvent extends ResponseEvent {
	private StorageContainerDetail container;

	public StorageContainerDetail getContainer() {
		return container;
	}

	public void setContainer(StorageContainerDetail container) {
		this.container = container;
	}
	
	public static StorageContainerUpdatedEvent ok(StorageContainerDetail container) {
		StorageContainerUpdatedEvent resp = new StorageContainerUpdatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setContainer(container);
		return resp;
	}
	
	public static StorageContainerUpdatedEvent badRequest(Exception e) {
		StorageContainerUpdatedEvent resp = new StorageContainerUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		if (e instanceof ObjectCreationException) {
			ObjectCreationException oce = (ObjectCreationException)e;
			resp.setErroneousFields(oce.getErroneousFields());
		}
		resp.setException(e);
		return resp;		
	}
	
	public static StorageContainerUpdatedEvent notFound(Long containerId) {
		StorageContainerUpdatedEvent resp = new StorageContainerUpdatedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		
		StorageContainerDetail detail = new StorageContainerDetail();
		detail.setId(containerId);
		resp.setContainer(detail);
		return resp;
	}
	
	public static StorageContainerUpdatedEvent serverError(Exception e) {
		StorageContainerUpdatedEvent resp = new StorageContainerUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;		
	}
}
