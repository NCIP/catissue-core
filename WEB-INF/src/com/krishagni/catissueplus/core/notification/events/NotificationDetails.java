
package com.krishagni.catissueplus.core.notification.events;

import com.krishagni.catissueplus.core.common.util.ObjectType;
import com.krishagni.catissueplus.core.common.util.Operation;

public class NotificationDetails {

	private Long auditId;

	private ObjectType objectType;

	private Long objectId;

	private Operation operation;

	/**
	 * @return the auditId
	 */
	public Long getAuditId() {
		return auditId;
	}

	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}

	/**
	 * @return the objectType
	 */
	public ObjectType getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return the objectId
	 */
	public Long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/*	*//**
				* @param operation the operation to set
				*/

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

}
