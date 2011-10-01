package edu.wustl.catissuecore.domain.ccts;

// Generated Jan 28, 2011 10:47:34 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * Represents a queue that holds data received from C3PR and PSC for further
 * processing in caTissue.
 */
public class DataQueue extends AbstractDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	/**
	 * User that initiated the receipt of this data item.
	 */
	private User user;
	/**
	 * Notification that prompted the receipt of this data item.
	 */
	private Notification notification;
	/**
	 * Current status.
	 */
	private ProcessingStatus processingStatus;
	/**
	 * Actual content.
	 */
	private String payload;
	/**
	 * Timestamp.
	 */
	private Date dateTime;
	
	private Participant participant;
	
	private CollectionProtocolRegistration registration;
	
	private Boolean incoming;
	
	public DataQueue() {		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.common.domain.AbstractDomainObject#getId()
	 */
	public Long getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.common.domain.AbstractDomainObject#setId(java.lang.Long)
	 */
	public void setId(Long identifier) {
		this.id = identifier;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User catissueUser) {
		this.user = catissueUser;
	}

	public Notification getNotification() {
		return this.notification;
	}

	public void setNotification(Notification catissueCctsNotif) {
		this.notification = catissueCctsNotif;
	}

	public String getPayload() {
		return this.payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public Date getDateTime() {
		return this.dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return the processingStatus
	 */
	public final ProcessingStatus getProcessingStatus() {
		return processingStatus;
	}

	/**
	 * @param processingStatus
	 *            the processingStatus to set
	 */
	public final void setProcessingStatus(ProcessingStatus processingStatus) {
		this.processingStatus = processingStatus;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result
				+ ((incoming == null) ? 0 : incoming.hashCode());
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DataQueue)) {
			return false;
		}
		DataQueue other = (DataQueue) obj;
		if (dateTime == null) {
			if (other.dateTime != null) {
				return false;
			}
		} else if (!dateTime.equals(other.dateTime)) {
			return false;
		}
		if (incoming == null) {
			if (other.incoming != null) {
				return false;
			}
		} else if (!incoming.equals(other.incoming)) {
			return false;
		}
		if (payload == null) {
			if (other.payload != null) {
				return false;
			}
		} else if (!payload.equals(other.payload)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataQueue [id=" + id + ", user=" + user + ", processingStatus="
				+ processingStatus + ", payload=" + payload + ", dateTime="
				+ dateTime + "]";
	}

	/**
	 * @return the participant
	 */
	public Participant getParticipant() {
		return participant;
	}

	/**
	 * @param participant the participant to set
	 */
	public void setParticipant(Participant participant) {
		this.participant = participant;
	}
	
	/**
	 * Returns true, if this item is a creation or an update of {@link Participant}. 
	 * @return
	 */
	public boolean isParticipantRelated() {
		return getNotification().getEventType()==EventType.SUBJECT_CREATION;
	}
	
	/**
	 * Returns true, if this item is a creation or an update of a {@link CollectionProtocolRegistration}. 
	 * @return
	 */
	public boolean isRegistrationRelated() {
		return getNotification().getEventType()==EventType.SUBJECT_REGISTRATION;
	}
	

	/**
	 * @return the registration
	 */
	public final CollectionProtocolRegistration getRegistration() {
		return registration;
	}

	/**
	 * @param registration the registration to set
	 */
	public final void setRegistration(CollectionProtocolRegistration registration) {
		this.registration = registration;
	}

	/**
	 * @return the incoming
	 */
	public Boolean getIncoming() {
		return incoming;
	}

	/**
	 * @param incoming the incoming to set
	 */
	public void setIncoming(Boolean incoming) {
		this.incoming = incoming;
	}

}
