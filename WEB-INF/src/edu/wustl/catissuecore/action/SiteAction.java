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
import edu.wustl.common.cde.CDEManager;
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

        //Sets the stateList attribute to be used in the Add/Edit User Page.
        request.setAttribute(Constants.STATELIST, Constants.STATEARRAY);

        //Sets the countryList attribute to be used in the Add/Edit User Page.
        request.setAttribute(Constants.COUNTRYLIST, Constants.COUNTRYARRAY);

        //Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.SITE_ACTIVITY_STATUS_VALUES);
        
        //Sets the siteTypeList attribute to be used in the Site Add/Edit Page.
        List siteList = CDEManager.getCDEManager().getList(Constants.CDE_NAME_SITE_TYPE,null);
        request.setAttribute(Constants.SITETYPELIST, siteList);
        
    	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
    	Collection coll =  userBizLogic.getUsers();
    	request.setAttribute(Constants.USERLIST, coll);
    	
    	// ------------------------------------------------------------------
    	boolean isOnChange = false; 
		String str = request.getParameter("isOnChange");
		
		if(str!=null && str.equals("true"))
		{
			isOnChange = true;
		}
		
    	if(siteForm != null && isOnChange)
    	{
    	    String emailAddress ="";
    	    Logger.out.debug("Id of Coordinator of Site : " + siteForm.getCoordinatorId());
    	    
    	    List userList = userBizLogic.retrieve(User.class.getName(),Constants.SYSTEM_IDENTIFIER , new Long(siteForm.getCoordinatorId()));
    	    if(userList.size()>0)
    	    {
    	    	User user = (User)userList.get(0); 	
    	    	if (user != null)
        		{
        		    emailAddress = user.getEmailAddress() ; 
        		    Logger.out.debug("Email Id of Coordinator of Site : " + emailAddress );
        		}
    	    }
    		siteForm.setEmailAddress(emailAddress); 
    	}        
    	// ------------------------------------------------------------------
   
    	// ------------- add new
		String reqPath = request.getParameter(Constants.REQ_PATH);
		
		if (reqPath != null)
		{
			request.setAttribute(Constants.REQ_PATH, reqPath);
		}
		
		Logger.out.debug("SiteAction redirect :---------- "+ reqPath  );
        
        // ----------------add new end-----
        
        String pageOf = (String)request.getParameter(Constants.PAGEOF);
        
        if (pageOf != null)
        {
            request.setAttribute(Constants.PAGEOF, pageOf);
        }
        
        return mapping.findForward(pageOf);
    }
}