/**
 * <p>Title: ConflictParticipantDataDetailsAction Class>
 * <p>Description: To retrieve the participant details
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 * @Date 9/18/2007
 * @author kalpana Thakur
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConflictParticipantDataDetailsForm;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;

/**
 * @author renuka_bajpai
 *
 */
public class ConflictParticipantDataDetailsAction extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final ConflictParticipantDataDetailsForm conflictParticipantDataDetailsForm
		= (ConflictParticipantDataDetailsForm) form;
		final String participantId = request.getParameter(Constants.ID);

		final HttpSession session = request.getSession();
		session.setAttribute(Constants.PARTICIPANT_ID_TO_ASSOCIATE, participantId);
		session.removeAttribute(Constants.SCG_ID_TO_ASSOCIATE);

		final DefaultBizLogic defaultBizLogic = new DefaultBizLogic();

		//retrieved the participant object and populated the bean
		final Object object = defaultBizLogic.retrieve(Participant.class.getName(), new Long(
				participantId));
		Participant participant = null;
		if (object != null)
		{
			participant = (Participant) object;
			defaultBizLogic.populateUIBean(Participant.class.getName(), participant.getId(),
					conflictParticipantDataDetailsForm);
		}

		return mapping.findForward(Constants.SUCCESS);
	}
}
