package edu.wustl.catissuecore.actionForm.ccts;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import edu.wustl.common.util.logger.Logger;

/**
 * @author Denis G. Krylov
 * 
 */
public class DataQueueForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger
			.getCommonLogger(DataQueueForm.class);
	
	private String accept;
	private String reject;
	@SuppressWarnings("unused")
	private String matchedParticipantId;
	
	

	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		accept = null;
		reject = null;
		matchedParticipantId = null;
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
	 * @return the accept
	 */
	public final String getAccept() {
		return accept;
	}

	/**
	 * @param accept the accept to set
	 */
	public final void setAccept(String accept) {
		this.accept = accept;
	}

	/**
	 * @return the reject
	 */
	public final String getReject() {
		return reject;
	}

	/**
	 * @param reject the reject to set
	 */
	public final void setReject(String reject) {
		this.reject = reject;
	}
	
	public boolean isRejected() {
		return StringUtils.isNotBlank(getReject());
	}
	
	public boolean isAccepted() {
		return StringUtils.isNotBlank(getAccept());
	}

	/**
	 * @return the matchedParticipantId
	 */
	public final String getMatchedParticipantId() {
		return matchedParticipantId;
	}

	/**
	 * @param matchedParticipantId the matchedParticipantId to set
	 */
	public final void setMatchedParticipantId(String matchedParticipantId) {
		this.matchedParticipantId = matchedParticipantId;
	}


}