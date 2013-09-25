/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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
