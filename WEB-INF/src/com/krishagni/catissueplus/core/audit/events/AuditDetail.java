/**
 * 
 */

package com.krishagni.catissueplus.core.audit.events;

import java.util.Date;

import com.krishagni.catissueplus.core.audit.domain.Audit;

public class AuditDetail {

	private String objectType;

	private Long objectId;

	private String operation;

	private Long userId;

	private String ipAddress;

	private Date updatedDate;

	private String reasonForChange;

	/**
	 * @return the objectId
	 */
	public Long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId
	 *            the objectId to set
	 */
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return the objectType
	 */
	public String getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate
	 *            the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the reasonForChange
	 */
	public String getReasonForChange() {
		return reasonForChange;
	}

	/**
	 * @param reasonForChange
	 *            the reasonForChange to set
	 */
	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}

	public static AuditDetail fromDomain(Audit audit) {
		AuditDetail auditDetail = new AuditDetail();
		auditDetail.setObjectId(audit.getObjectId());
		auditDetail.setObjectType(audit.getObjectType());
		auditDetail.setOperation(audit.getOperation());
		auditDetail.setReasonForChange(audit.getReasonForChange());
		auditDetail.setUpdatedDate(audit.getUpdatedDate());
		auditDetail.setUserId(audit.getUserId());
		auditDetail.setIpAddress(audit.getIpAddress());
		return auditDetail;

	}

}
