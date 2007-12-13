package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.querysuite.queryobject.IParameterizedQuery;
/**
 * When the user searches or saves a query , the query is checked for the conditions like DAG should not be empty , is there 
 * at least one node in view on define view page and does the query contain the main object. If all the conditions are satisfied 
 * further process is done else corresponding error message is shown.
 * 
 * @author shrutika_chintal
 *
 */
public class ValidateQueryAction extends BaseAction {

	@Override
	protected ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String buttonClicked = (String)request.getParameter(Constants.BUTTON_CLICKED); 
		HttpSession session = request.getSession();
		IParameterizedQuery parameterizedQuery = (IParameterizedQuery) session.getAttribute(AppletConstants.QUERY_OBJECT);
		String validationMessage = ValidateQueryBizLogic.getValidationMessage(request, parameterizedQuery);
		if (validationMessage != null)
		{
			response.setContentType("text/html");
			response.getWriter().write(validationMessage);
			return null;
		}
		response.getWriter().write(buttonClicked);
		return null;
	}

}
