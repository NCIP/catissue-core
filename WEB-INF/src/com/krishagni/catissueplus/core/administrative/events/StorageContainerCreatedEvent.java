package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StorageContainerCreatedEvent extends ResponseEvent {
	private StorageContainerDetail container;

	public StorageContainerDetail getContainer() {
		return container;
	}

	public void setContainer(StorageContainerDetail container) {
		this.container = container;
	}
	
	public static StorageContainerCreatedEvent ok(StorageContainerDetail detail) {
		StorageContainerCreatedEvent result = new StorageContainerCreatedEvent();
		result.setContainer(detail);
		result.setStatus(EventStatus.OK);
		return result;
	}
	
	public static StorageContainerCreatedEvent badRequest(Exception e) {
		StorageContainerCreatedEvent result = new StorageContainerCreatedEvent();
		result.setStatus(EventStatus.BAD_REQUEST);
		result.setException(e);
				
		if (e instanceof ObjectCreationException) {
			ObjectCreationException oce = (ObjectCreationException)e;
			result.setErroneousFields(oce.getErroneousFields());
		}
		
		return result;
	}
	
	public static StorageContainerCreatedEvent serverError(Exception e) {
		StorageContainerCreatedEvent result = new StorageContainerCreatedEvent();
		result.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		result.setException(e);
		return result;
	}
}
