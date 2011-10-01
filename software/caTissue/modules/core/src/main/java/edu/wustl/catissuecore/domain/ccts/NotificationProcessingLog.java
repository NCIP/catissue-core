package edu.wustl.catissuecore.domain.ccts;

// Generated Jan 28, 2011 10:47:34 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * Represents a log entry about an attempt to process and respond to a CCTS {@link Notification}.
 */
public class NotificationProcessingLog extends AbstractDomainObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private ProcessingResult processingResult;
	private User user;
	private Notification notification;
	private Date dateTime;
	private String payload;
	private String errorCode;
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
	 * @return the dateTime
	 */
	public final Date getDateTime() {
		return dateTime;
	}
	/**
	 * @param dateTime the dateTime to set
	 */
	public final void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	/**
	 * @return the payload
	 */
	public final String getPayload() {
		return payload;
	}
	/**
	 * @param payload the payload to set
	 */
	public final void setPayload(String payload) {
		this.payload = payload;
	}
	/**
	 * @return the errorCode
	 */
	public final String getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public final void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the processingResult
	 */
	public final ProcessingResult getProcessingResult() {
		return processingResult;
	}
	/**
	 * @param processingResult the processingResult to set
	 */
	public final void setProcessingResult(ProcessingResult processingResult) {
		this.processingResult = processingResult;
	}
	/**
	 * @return the user
	 */
	public final User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public final void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the notification
	 */
	public final Notification getNotification() {
		return notification;
	}
	/**
	 * @param notification the notification to set
	 */
	public final void setNotification(Notification notification) {
		this.notification = notification;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NotificationProcessingLog [id=" + id + ", processingResult="
				+ processingResult + ", user=" + user + ", notification="
				+ notification + ", dateTime=" + dateTime + ", payload="
				+ payload + ", errorCode=" + errorCode + "]";
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
				+ ((notification == null) ? 0 : notification.hashCode());
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
		if (!(obj instanceof NotificationProcessingLog)) {
			return false;
		}
		NotificationProcessingLog other = (NotificationProcessingLog) obj;
		if (dateTime == null) {
			if (other.dateTime != null) {
				return false;
			}
		} else if (!dateTime.equals(other.dateTime)) {
			return false;
		}
		if (notification == null) {
			if (other.notification != null) {
				return false;
			}
		} else if (!notification.equals(other.notification)) {
			return false;
		}
		return true;
	}

	
}
