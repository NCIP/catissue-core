/**
 * <p>Title: QuickEventsAction Class</p>
 * <p>Description:  This class initializes the atributes required for the QuickEvents webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 3, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.QuickEventsForm;
import edu.wustl.catissuecore.bizlogic.SOPBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * Created : 03-July-2006.
 * @author mandar_deshmukh.
 *
 * This class initializes the atributes required for the QuickEvents webpage.
 */
public class QuickEventsAction extends BaseAction
{

	/**
	 * Overrides the execute method of Action class.
	 * Sets the various fields in QuickEvents webpage.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return value for ActionForward object
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Map dynamicEventMap= new HashMap();

		request.setAttribute(Constants.EVENT_PARAMETERS_LIST, new SOPBizLogic().getAllSOPEventFormNames(dynamicEventMap));
		request.setAttribute(Constants.SPECIMEN_ID, request.getAttribute(Constants.SPECIMEN_ID));
		//add messages from session to request
		final HttpSession session = request.getSession(true);
		if (session != null)
		{
			final ActionMessages messages = (ActionMessages) session.getAttribute("messages");
			if (messages != null)
			{
				this.saveMessages(request, messages);
				session.removeAttribute("messages");
			}
			saveActionErrors(form, request);
		}
		final String pageOf = Constants.SUCCESS;

		return mapping.findForward(pageOf);
	}

	/**
	 * @param form
	 * @param request
	 */
	private void saveActionErrors(ActionForm form, HttpServletRequest request)
	{
		HttpSession session = request.getSession();
		final ActionErrors errors = (ActionErrors) session.getAttribute(Constants.ERRORS);
		if (errors != null)
		{
			this.saveErrors(request, errors);

			QuickEventsForm qEForm = (QuickEventsForm) form;
			qEForm.setSpecimenLabel((String)session.getAttribute(Constants.SPECIMEN_LABLE));
			qEForm.setSpecimenEventParameter((String)session.getAttribute(Constants.SPECIMEN_EVENT_PARAMETER));

			request.setAttribute(Constants.SPECIMEN_EVENT_PARAMETER, (String)session.getAttribute(Constants.SPECIMEN_EVENT_PARAMETER));
			request.setAttribute(Constants.SPECIMEN_ID, (String)session.getAttribute(Constants.SPECIMEN_ID));
			request.setAttribute("eventSelected", (String)session.getAttribute(Constants.SPECIMEN_EVENT_PARAMETER));

			session.removeAttribute(Constants.SPECIMEN_EVENT_PARAMETER);
			session.removeAttribute(Constants.SPECIMEN_LABLE);
			session.removeAttribute(Constants.ERRORS);
		}
	}
}
