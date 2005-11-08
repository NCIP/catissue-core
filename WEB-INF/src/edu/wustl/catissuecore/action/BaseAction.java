package edu.wustl.catissuecore.action;



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.exception.UserNotAuthenticatedException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;


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
		setSelectedMenu(request);
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
	
	protected void DeleteRow(List list,Map map,
			HttpServletRequest request){
		
		DeleteRow(list,map,request,null);
	}
	/**
	 * Returns boolean used for diabling/enabling checkbox in jsp Page and
	 * rearranging rows
	 */
	protected void DeleteRow(List list,Map map,
			HttpServletRequest request,String outer){
		
		//whether delete button is clicked or not
		String status = request.getParameter("status");
    	if(status == null){
    		status = Constants.FALSE;
    	}
    	
    	String text;
       	for(int k = 0; k < list.size(); k++){
       		text = (String)list.get(k);
    		String first = text.substring(0,text.indexOf(":"));
    		String second = text.substring(text.indexOf("_"));
    		
    		//condition for creating ids for innerTable
    		boolean condition = false;
    		String third = "",fourth = "";
    		
    		//checking whether key is inneTable'key or not
    		if(second.indexOf(":") != -1){
    			condition = true;
    			third = second.substring(0,second.indexOf(":"));
    			fourth = second.substring(second.lastIndexOf("_"));
    			
    		}
    		
    		if(status.equals(Constants.TRUE)){
    			Map values = map;
    			
    			//for outerTable
    			int outerCount = 1;
    			
    			//for innerTable
    			int innerCount = 1;
    			for(int i = 1; i <= values.size() ; i++){
    				String id = "";
    				String mapId = "";
    				
    				//for innerTable key's rearrangement
    				if(condition){
    					if(outer != null){
    						id = first + ":"+ outer + third + ":"+ i + fourth;
    						mapId = first + ":"+ outer + third + ":"+ innerCount + fourth;
    					}
    					else {
    						//for outer key's rearrangement
    						for(int j = 1; j <= values.size() ; j++){
    							id = first + ":"+ i + third + ":"+ j + fourth;
    							mapId = first + ":"+ outerCount + third + ":"+ j + fourth;
    							
    							//checking whether map from form contains keys or not
    							if(values.containsKey(id)){
    								values.put(mapId,map.get(id));
    		    					outerCount++;
    		    				}
    						}
    					}
    					
    				}
    	    			
    	    		else {
    	    			id = first + ":" + i + second;
    	    			mapId = first + ":" +innerCount + second;
    	    		}
    				
    				//rearranging key's
    				if(values.containsKey(id)){
    					values.put(mapId,map.get(id));
    					innerCount++;
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
	
	public void setSelectedMenu(HttpServletRequest request)
	{
		Logger.out.debug("Inside setSelectedMenu.....");
		Logger.out.debug("#######################################################################");
		String strMenu = request.getParameter(Constants.MENU_SELECTED );
		if(strMenu != null )
		{
			request.setAttribute(Constants.MENU_SELECTED ,strMenu);
			Logger.out.debug(Constants.MENU_SELECTED + " " +strMenu +" set successfully");
		}
	}
}