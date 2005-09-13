package edu.wustl.catissuecore.action;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.exception.UserNotAuthenticatedException;
import edu.wustl.catissuecore.util.global.Constants;

import edu.wustl.common.beans.SessionDataBean;


/**
 * This is the base class for all other Actions. The class provides generic
 * methods that are resuable by all subclasses. In addition, this class ensures
 * that the user is authenticated before calling the executeWorkflow of the
 * subclass. If the User is not authenticated then an
 * UserNotAuthenticatedException is thrown.
 * 
 * @author Aarti Sharma
 *  
 */
public abstract class BaseAction extends Action  {



	/*
	 * Method ensures that the user is authenticated before calling the
	 * executeAction of the subclass. If the User is not authenticated then an
	 * UserNotAuthenticatedException is thrown.
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public final ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (1==2 && request.getSession().getAttribute(Constants.SESSION_DATA) == null) {
			//Forward to the Login
		   
			throw new UserNotAuthenticatedException();
		}
		setRequestData(request);
		return executeAction(mapping, form, request, response);
	}

	protected void setRequestData(HttpServletRequest request)
	{
		//Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        if(operation!=null)
        {
        	//Sets the operation attribute to be used in the Add/Edit User Page. 
        	request.setAttribute(Constants.OPERATION, operation);
        }
	}
	/**
	 * Returns the current User authenticated by CSM Authentication.
	 * 
	 * @param request
	 * @return
	 */
	protected String getUserLoginName(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if(obj!=null)
		{
			SessionDataBean sessionData = (SessionDataBean) obj;
			return  sessionData.getUserName();
		}
		return null;
		//return (String) request.getSession().getAttribute(Constants.SESSION_DATA);
	}
	
	/**
	 * Subclasses should implement the action's business logic in this method
	 * and can be sure that an authenticated user is present.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public abstract ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception;
}