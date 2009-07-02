/**
 * <p>Title: BiohazardAction Class>
 * <p>Description:	This class initializes the fields of Biohazard.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.BiohazardForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.cde.CDEManager;

/**
 * @author renuka_bajpai
 *
 */
public class BiohazardAction extends SecureAction
{

	/**
	 * Overrides the execute method of Action class.
	 * Initializes the various fields in Biohazard.jsp Page.
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return ActionForward : ActionForward
	 * */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		BiohazardForm biohazardForm = (BiohazardForm) form;
		String submittedFor = (String) request.getAttribute(Constants.SUBMITTED_FOR);
		biohazardForm.setOperation(operation);
		biohazardForm.setSubmittedFor(submittedFor);

		String biohazard_Type_List = Constants.BIOHAZARD_TYPE_LIST;
		String formName;

		request.setAttribute("operationAdd", Constants.ADD);
		request.setAttribute("operationEdit", Constants.EDIT);
		request.setAttribute("biohazard_Type_List", biohazard_Type_List);
		request.setAttribute("search", Constants.SEARCH);

		if (operation.equals(Constants.EDIT))
		{
			formName = Constants.BIOHAZARD_EDIT_ACTION;
		}
		else
		{
			formName = Constants.BIOHAZARD_ADD_ACTION;
		}

		request.setAttribute("formName", formName);

		//Sets the operation attribute to be used in the Add/Edit Institute Page.
		List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_BIOHAZARD, null);
		request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);

		return mapping.findForward((String) request.getParameter(Constants.PAGE_OF));
	}
}