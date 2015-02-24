package com.krishagni.catissueplus.core.biospecimen.events;

public class CopyCpeOpDetail {
	private CollectionProtocolEventDetail cpe;
	
	private Long eventId;
	
	private String collectionProtocol;
	
	private String eventLabel;

	public CollectionProtocolEventDetail getCpe() {
		return cpe;
	}

	public void setCpe(CollectionProtocolEventDetail cpe) {
		this.cpe = cpe;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(String collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public String getEventLabel() {
		return eventLabel;
	}

	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
	}
}
