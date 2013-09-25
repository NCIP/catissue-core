/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.util;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessages;

public class CommonLoginInfoUtility {
	
	private String forwardTo;
	private ActionMessages actionMessages;
	private ActionErrors actionErrors;
	public String getForwardTo() {
		return forwardTo;
	}
	public void setForwardTo(String forwardTo) {
		this.forwardTo = forwardTo;
	}
	public ActionMessages getActionMessages() {
		return actionMessages;
	}
	public void setActionMessages(ActionMessages actionMessages) {
		this.actionMessages = actionMessages;
	}
	public ActionErrors getActionErrors() {
		return actionErrors;
	}
	public void setActionErrors(ActionErrors actionErrors) {
		this.actionErrors = actionErrors;
	}

}
