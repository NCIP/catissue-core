/**
 * <p>Title: DepartmentAction Class</p>
 * <p>Description:	This class initializes the fields in the Department Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on May 23rd, 2005
 */

package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;

/**
 * This class initializes the fields in the Department Add/Edit webpage.
 * @author Mandar Deshmukh
 */

public class DepartmentAction extends SecureAction
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
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		DepartmentForm departmentForm = (DepartmentForm) form;
		String submittedFor = (String) request.getAttribute(Constants.SUBMITTED_FOR);
		departmentForm.setOperation(operation);
		departmentForm.setSubmittedFor(submittedFor);

		String formName;
		boolean readOnlyValue;
		if (operation.equals(Constants.EDIT))
		{
			formName = Constants.DEPARTMENT_EDIT_ACTION;
			readOnlyValue = false;
		}
		else
		{
			formName = Constants.DEPARTMENT_ADD_ACTION;
			readOnlyValue = false;
		}
		request.setAttribute("operationAdd", Constants.ADD);
		request.setAttribute("operationEdit", Constants.EDIT);

		request.setAttribute("formName", formName);
		request.setAttribute("readOnlyValue", readOnlyValue);

		return mapping.findForward((String) request.getParameter(Constants.PAGE_OF));
	}
}