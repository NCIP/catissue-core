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
