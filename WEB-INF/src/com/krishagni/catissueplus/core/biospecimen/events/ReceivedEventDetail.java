package com.krishagni.catissueplus.core.biospecimen.events;

public class ReceivedEventDetail extends SpecimenEventDetail {
	private String receivedQuality;

	public String getReceivedQuality() {
		return receivedQuality;
	}

	public void setReceivedQuality(String receivedQuality) {
		this.receivedQuality = receivedQuality;
	}
}