
package com.krishagni.catissueplus.events.specimencollectiongroups;

import java.util.Date;

public class SpecimenCollectionGroupInfo {

	private Long id;

	private String name;

	private String collectionStatus;

	private String collectionPointLabel;

	private Double eventPoint;

	private Date receivedDate;

	private Date registrationDate;

	private int parentOffset;

	private boolean hasChilds;

	public boolean isHasChilds() {
		return hasChilds;
	}

	public void setHasChilds(boolean hasChilds) {
		this.hasChilds = hasChilds;
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

	public String getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public String getCollectionPointLabel() {
		return collectionPointLabel;
	}

	public void setCollectionPointLabel(String collectionPointLabel) {
		this.collectionPointLabel = collectionPointLabel;
	}

	public Double getEventPoint() {
		return eventPoint;
	}

	public void setEventPoint(Double eventPoint) {
		this.eventPoint = eventPoint;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public int getParentOffset() {
		return parentOffset;
	}

	public void setParentOffset(int parentOffset) {
		this.parentOffset = parentOffset;
	}
}
