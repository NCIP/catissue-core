package edu.wustl.catissuecore.action;



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
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
		if (getSessionData(request) == null) {
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
	
	protected SessionDataBean getSessionData(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if(obj!=null)
		{
			SessionDataBean sessionData = (SessionDataBean) obj;
			return  sessionData;
		}
		return null;
		//return (String) request.getSession().getAttribute(Constants.SESSION_DATA);
	}
	
	
	/**
	 * Returns boolean used for diabling/enabling checkbox in jsp Page and
	 * rearranging rows
	 */
	protected void DeleteRow(List list,Map map,
			HttpServletRequest request){
		
		String status = request.getParameter("status");
    	if(status == null){
    		status = Constants.FALSE;
    	}
    	
    	String text;
       	for(int k = 0; k < list.size(); k++){
    		text = (String)list.get(k);
    		String first = text.substring(0,text.indexOf(":"));
    		String last = text.substring(text.indexOf("_"));
    		if(status.equals(Constants.TRUE)){
    			Map values = map;
    			int count = 1;
    			for(int i = 1; i <= values.size() ; i++){
    				String id = first + ":" + i +last;
    				if(values.containsKey(id)){
    					values.put(first + ":" +count +last,map.get(id));
    					count++;
    				}
    			}
    		}
    	}
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