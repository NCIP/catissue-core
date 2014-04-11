package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class SaveQueryEvent extends RequestEvent {

	private SavedQueryDetail savedQueryDetail;

	public SavedQueryDetail getSavedQueryDetail() {
		return savedQueryDetail;
	}

	public void setSavedQueryDetail(SavedQueryDetail savedQueryDetail) {
		this.savedQueryDetail = savedQueryDetail;
	}
}
