/**
 * <p>Title: SiteAction Class>
 * <p>Description:	This class initializes the fields of the Site Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields of the Site Add/Edit webpage.
 * @author aniruddha_phadnis
 */
public class SiteAction  extends SecureAction
{

    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in Site Add/Edit webpage.
     * */
    protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	SiteForm siteForm = (SiteForm )form;

        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit User Page.
        if (operation != null)
        {
            request.setAttribute(Constants.OPERATION, operation);
        }

        //Sets the countryList attribute to be used in the Add/Edit User Page.
        List countryList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_COUNTRY_LIST,null);
        request.setAttribute(Constants.COUNTRYLIST, countryList);
        
        //Sets the stateList attribute to be used in the Add/Edit User Page.
        List stateList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_STATE_LIST,null);
        request.setAttribute(Constants.STATELIST, stateList);
        

        //Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.SITE_ACTIVITY_STATUS_VALUES);
        
        //Sets the siteTypeList attribute to be used in the Site Add/Edit Page.
        List siteList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SITE_TYPE,null);
        request.setAttribute(Constants.SITETYPELIST, siteList);
        
    	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
    	Collection coll =  userBizLogic.getUsers(operation);
    	request.setAttribute(Constants.USERLIST, coll);
    	
    	// ------------------------------------------------------------------
    	
    	/**** code for ajax *****/
    	
    	boolean isOnChange = getIsOnChange(request);  
 		Long coordinatorId = getCoordinatorId(request);
	
 		if(siteForm != null && isOnChange && coordinatorId!=null)
    	{
    	    String emailAddress ="";
    	    List userList = userBizLogic.retrieve(User.class.getName(),Constants.SYSTEM_IDENTIFIER , coordinatorId);
    	    
    	    if(userList.size()>0)
    	    {
    	    	User user = (User)userList.get(0); 	
    	    	if (user != null)
        		{
        		    emailAddress = user.getEmailAddress() ; 
        		    Logger.out.debug("Email Id of Coordinator of Site : " + emailAddress );
        		    sendEmailAddress(emailAddress,response);
        		    
        		    //set the email id in form bean
        		    siteForm.setEmailAddress(emailAddress);
        		}
    	    }
			//for ajax return null as Actionservlet returns ActionForward object
			return null;   
    	}
        String pageOf = (String)request.getParameter(Constants.PAGEOF);
        
        if (pageOf != null)
        {
            request.setAttribute(Constants.PAGEOF, pageOf);
        }
        return mapping.findForward(pageOf);
    }


    
    /**
     * method for ajax response
     * @param emailAddress : emailaddress of the coordinator
     * @param response :object of HttpServletResponse
     * @throws Exception
     */
    private void sendEmailAddress(String emailAddress, HttpServletResponse response) throws Exception 		  
	{
		PrintWriter out = response.getWriter();
		Logger.out.debug("mail"+emailAddress);
		response.setContentType("text/html");
		out.write(emailAddress );
	}

    /**
	 * method for getting coordinatorId from request
	 * @param request :object of HttpServletResponse
	 * @return coordinatorId
	 */
	private Long getCoordinatorId(HttpServletRequest request)
	{
		Long coordinatorId = null;
		String coordinatorIdStr = request.getParameter("coordinatorId");
		Validator validator = new Validator(); 
		try
		{
		if(!validator.isEmpty(coordinatorIdStr))
		{
			coordinatorId = new Long(coordinatorIdStr);
		}
		}
		catch(Exception e)
		{
			coordinatorId = null;
		}
		return coordinatorId;
	}
	
	/**
	 * method for getting isOnChange from request
	 * @param request:object of HttpServletResponse
	 * @return isOnChange :boolean 
	 */
	private boolean getIsOnChange(HttpServletRequest request)
	{
    	boolean isOnChange=false;
		String str = request.getParameter("isOnChange");
		if(str!=null && str.equals("true"))
		{
			isOnChange = true;
		}
		return isOnChange;
	}

	/**** code for ajax *****/

}