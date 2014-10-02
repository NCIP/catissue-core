
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Calendar;
import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;

public class SpecimenCollectionGroupInfo implements Comparable<SpecimenCollectionGroupInfo> {

	private Long id;

	private String name;

	private String collectionStatus;

	private String collectionPointLabel;

	private Double eventPoint;

	private Date receivedDate;

	private Date collectionDate;

	private Date registrationDate;

	private int parentOffset;

	private boolean hasChilds;

	private Long eventId;

	private String instanceType;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

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

	public Date getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
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

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof SpecimenCollectionGroupInfo) {
			SpecimenCollectionGroupInfo scgInfo = (SpecimenCollectionGroupInfo) obj;
			if (scgInfo.getId() == null && scgInfo.getEventId() == null) {
				return false;
			}
			if (scgInfo.getId() == null && this.id == null) {
				return this.eventId.equals(scgInfo.getEventId());
			}
			else if (this.id != null && scgInfo.getId() == null) {
				return this.eventId.equals(scgInfo.getEventId());
			}
			else if (scgInfo.getId() != null && this.id == null) {
				return this.eventId.equals(scgInfo.getEventId());
			}
			else {
				return this.id.equals(scgInfo.getId());
			}
			//			else if (scgInfo.getId() == null ) {
			//				return this.eventId.equals(scgInfo.getEventId());
			//			}
			//			else {
			//				return ((this.id+"_"+this.eventId).equals(scgInfo.getId()+"_"+scgInfo.getEventId()));
			//			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
		return result;
	}

	public static SpecimenCollectionGroupInfo fromScg(SpecimenCollectionGroup specimenCollectionGroup,
			Date registrationDate) {
		CollectionProtocolEvent event = specimenCollectionGroup.getCollectionProtocolEvent();
		SpecimenCollectionGroupInfo scgInfo = new SpecimenCollectionGroupInfo();
		scgInfo.setCollectionStatus(specimenCollectionGroup.getCollectionStatus());
		scgInfo.setHasChilds(false);
		scgInfo.setId(specimenCollectionGroup.getId());
		scgInfo.setName(specimenCollectionGroup.getName());
		scgInfo.setReceivedDate(specimenCollectionGroup.getReceivedTimestamp());
		scgInfo.setCollectionDate(specimenCollectionGroup.getCollectionTimestamp());
		scgInfo.setRegistrationDate(registrationDate);
		scgInfo.setCollectionPointLabel(event.getCollectionPointLabel());
		scgInfo.setEventPoint(event.getStudyCalendarEventPoint());
		scgInfo.setEventId(event.getId());
		scgInfo.setInstanceType("scg");
		return scgInfo;
	}

	public static SpecimenCollectionGroupInfo fromCpe(CollectionProtocolEvent collectionProtocolEvent,
			Date registrationDate) {
		SpecimenCollectionGroupInfo scgInfo = new SpecimenCollectionGroupInfo();
		System.out.println();
		//		scgInfo.setId(collectionProtocolEvent.getId());
		scgInfo.setCollectionStatus(Status.SCG_COLLECTION_STATUS_PENDING.getStatus());
		scgInfo.setHasChilds(false);
		Calendar cal = Calendar.getInstance();
		cal.setTime(registrationDate);
		cal.add(Calendar.DATE, collectionProtocolEvent.getStudyCalendarEventPoint().intValue());
		scgInfo.setRegistrationDate(cal.getTime());
		scgInfo.setCollectionPointLabel(collectionProtocolEvent.getCollectionPointLabel());
		scgInfo.setEventPoint(collectionProtocolEvent.getStudyCalendarEventPoint());
		scgInfo.setEventId(collectionProtocolEvent.getId());
		scgInfo.setInstanceType("cpe");
		return scgInfo;
	}

	@Override
	public int compareTo(SpecimenCollectionGroupInfo scgInfo) {
		int result = Double.compare(eventPoint, scgInfo.getEventPoint());
		if (result != 0) {
			return result;
		}
		if (receivedDate != null && scgInfo.getReceivedDate() != null) {
			return receivedDate.compareTo(scgInfo.getReceivedDate());
		}
		else {
			return eventId.compareTo(scgInfo.getEventId());
		}
	}
}
