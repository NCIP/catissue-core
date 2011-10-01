package edu.wustl.catissuecore.uiobject;

import edu.wustl.common.domain.UIObject;

public class OrderUIObject implements UIObject {
	
	protected Boolean operationAdd;
	
	/**
	 * mailNotification.
	 */
	protected Boolean mailNotification;

	public Boolean getOperationAdd() {
		return operationAdd;
	}

	public void setOperationAdd(Boolean operationAdd) {
		this.operationAdd = operationAdd;
	}

	public Boolean getMailNotification() {
		return mailNotification;
	}

	public void setMailNotification(Boolean mailNotification) {
		this.mailNotification = mailNotification;
	}

}
