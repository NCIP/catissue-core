package edu.wustl.catissuecore.domain.ccts;

// Generated Jan 28, 2011 10:47:34 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import edu.wustl.common.domain.AbstractDomainObject;

/**
 * Represents a CCTS notification message received by caTissue from iHub.
 */
public class Notification extends AbstractDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Application application;
	private EventType eventType;
	private ObjectIdType objectIdType;
	private ProcessingStatus processingStatus;
	private Date dateSent;
	private Date dateReceived;
	private String objectIdValue;
	private Set<NotificationProcessingLog> processingLog = new HashSet<NotificationProcessingLog>();
	/**
	 * @return the id
	 */
	public final Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public final void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the application
	 */
	public final Application getApplication() {
		return application;
	}
	/**
	 * @param application the application to set
	 */
	public final void setApplication(Application application) {
		this.application = application;
	}
	/**
	 * @return the eventType
	 */
	public final EventType getEventType() {
		return eventType;
	}
	/**
	 * @param eventType the eventType to set
	 */
	public final void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	/**
	 * @return the objectType
	 */
	public final ObjectIdType getObjectIdType() {
		return objectIdType;
	}
	/**
	 * @param objectType the objectType to set
	 */
	public final void setObjectIdType(ObjectIdType objectType) {
		this.objectIdType = objectType;
	}
	/**
	 * @return the processingStatus
	 */
	public final ProcessingStatus getProcessingStatus() {
		return processingStatus;
	}
	/**
	 * @param processingStatus the processingStatus to set
	 */
	public final void setProcessingStatus(ProcessingStatus processingStatus) {
		this.processingStatus = processingStatus;
	}
	/**
	 * @return the objectIdValue
	 */
	public final String getObjectIdValue() {
		return objectIdValue;
	}
	/**
	 * @param objectIdValue the objectIdValue to set
	 */
	public final void setObjectIdValue(String objectIdValue) {
		this.objectIdValue = objectIdValue;
	}
	/**
	 * @return the processingLog
	 */
	public final Set<NotificationProcessingLog> getProcessingLog() {
		return processingLog;
	}
	/**
	 * @param processingLog the processingLog to set
	 */
	public final void setProcessingLog(Set<NotificationProcessingLog> processingLog) {
		this.processingLog = processingLog;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Notification [id=" + id + ", application=" + application
				+ ", eventType=" + eventType + ", objectIdType=" + objectIdType
				+ ", processingStatus=" + processingStatus + ", dateSent="
				+ dateSent + ", dateReceived=" + dateReceived
				+ ", objectIdValue=" + objectIdValue + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof Notification)) {
			return false;
		}
		Notification other = (Notification) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	/**
	 * @return the dateSent
	 */
	public final Date getDateSent() {
		return dateSent;
	}
	/**
	 * @param dateSent the dateSent to set
	 */
	public final void setDateSent(Date dateSent) {
		this.dateSent = dateSent;
	}
	/**
	 * @return the dateReceived
	 */
	public final Date getDateReceived() {
		return dateReceived;
	}
	/**
	 * @param dateReceived the dateReceived to set
	 */
	public final void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}
	
	/**
	 * @return
	 */
	public Date getDateOfLastProcessingAttempt() {
		Date date = null;
		if (CollectionUtils.isNotEmpty(getProcessingLog())) {
			date = getProcessingLog().iterator().next().getDateTime();
		}
		return date;
	}
	
	/**
	 * Creates a successful processing log entry.
	 * @param payload
	 */
	public void addSuccess(String payload) {
		NotificationProcessingLog log = new NotificationProcessingLog();
		log.setDateTime(new Date());
		log.setNotification(this);
		log.setPayload(payload);
		log.setProcessingResult(ProcessingResult.SUCCESS);
		getProcessingLog().add(log);
	}
	/**
	 * Creates an unsuccessful processing log entry.
	 * @param errorMessage
	 */
	public void addFailure(String errorMessage) {
		NotificationProcessingLog log = new NotificationProcessingLog();
		log.setDateTime(new Date());
		log.setNotification(this);
		log.setErrorCode(StringUtils.left(errorMessage,255));
		log.setProcessingResult(ProcessingResult.FAILURE);
		getProcessingLog().add(log);		
	}
	

}
