/**
 * <p>Title: ConflictCommonAction Class>
 * <p>Description:	Conflict Common Action class instantiate the bean for ConflictCommonView.jsp
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @version 1.00
 *@author kalpana Thakur
 * Created on sep 18,2007
 *
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ConflictCommonForm;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;

/**
 * @author renuka_bajpai
 *
 */
public class ConflictCommonAction extends BaseAction
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

		final ConflictCommonForm conflictCommonForm = (ConflictCommonForm) form;
		final String reportQueueId = request.getParameter(Constants.REPORT_ID);

		conflictCommonForm.setSurgicalPathologyNumber(request
				.getParameter(Constants.SURGICAL_PATHOLOGY_NUMBER));
		conflictCommonForm.setReportDate(request.getParameter(Constants.REPORT_DATE));
		conflictCommonForm.setSiteName(request.getParameter(Constants.SITE_NAME));
		conflictCommonForm.setReportCollectionDate(request
				.getParameter(Constants.REPORT_COLLECTION_DATE));

		final Participant participant = edu.wustl.catissuecore.caties.util.Utility
				.getParticipantFromReportLoaderQueue(reportQueueId);
		final String participantName = participant.getLastName() + "," + participant.getFirstName();
		final String birthDate = CommonUtilities.parseDateToString(participant.getBirthDate(),
				CommonServiceLocator.getInstance().getDatePattern());
		conflictCommonForm.setParticipantName(participantName);
		conflictCommonForm.setBirthDate(birthDate);
		conflictCommonForm.setSocialSecurityNumber(participant.getSocialSecurityNumber());

		return mapping.findForward(Constants.SUCCESS);
	}
}
