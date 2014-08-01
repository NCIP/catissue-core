package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StorageContainerGotEvent extends ResponseEvent {

	private StorageContainerDetails storageContainerDetails;

	private Long id;

	private String name;

	public StorageContainerDetails getStorageContainerDetails() {
		return storageContainerDetails;
	}

	public void setStorageContainerDetails(
			StorageContainerDetails storageContainerDetails) {
		this.storageContainerDetails = storageContainerDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static StorageContainerGotEvent ok(StorageContainerDetails details) {
		StorageContainerGotEvent event = new StorageContainerGotEvent();
		event.setStorageContainerDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static StorageContainerGotEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		StorageContainerGotEvent resp = new StorageContainerGotEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static StorageContainerGotEvent notFound(Long id) {
		StorageContainerGotEvent resp = new StorageContainerGotEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setId(id);
		return resp;
	}

	public static StorageContainerGotEvent notFound(String name) {
		StorageContainerGotEvent resp = new StorageContainerGotEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setName(name);
		return resp;
	}
}
