
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StorageContainerCreatedEvent extends ResponseEvent {

	private StorageContainerDetails storageContainerDetails;

	public StorageContainerDetails getStorageContainerDetails() {
		return storageContainerDetails;
	}

	public void setStorageContainerDetails(StorageContainerDetails storageContainerDetails) {
		this.storageContainerDetails = storageContainerDetails;
	}

	public static StorageContainerCreatedEvent ok(StorageContainerDetails details) {
		StorageContainerCreatedEvent event = new StorageContainerCreatedEvent();
		event.setStorageContainerDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static StorageContainerCreatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		StorageContainerCreatedEvent resp = new StorageContainerCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static StorageContainerCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		StorageContainerCreatedEvent resp = new StorageContainerCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
