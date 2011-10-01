package edu.wustl.catissuecore.actionForm.ccts;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import edu.wustl.common.util.logger.Logger;

/**
 * @author Denis G. Krylov
 * 
 */
public class CctsEventNotificationForm extends ValidatorForm { 

	private static final long serialVersionUID = 1L;

	private int msgId;

	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger
			.getCommonLogger(CctsEventNotificationForm.class);

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		msgId = 0;
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * 
	 * @return error ActionErrors instance
	 * @param mapping
	 *            Actionmapping instance
	 * @param request
	 *            HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();

		return super.validate(mapping, request);
	}

	/**
	 * @return the msgId
	 */
	public final int getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId
	 *            the msgId to set
	 */
	public final void setMsgId(int msgId) {
		this.msgId = msgId;
	}

}