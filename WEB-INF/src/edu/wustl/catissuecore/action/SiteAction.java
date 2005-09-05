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

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;

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
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION, operation);

        //Sets the stateList attribute to be used in the Add/Edit User Page.
        request.setAttribute(Constants.STATELIST, Constants.STATEARRAY);

        //Sets the countryList attribute to be used in the Add/Edit User Page.
        request.setAttribute(Constants.COUNTRYLIST, Constants.COUNTRYARRAY);

        //Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
        
        //Sets the siteTypeList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.SITETYPELIST, Constants.SITE_TYPE_ARRAY);
        
        try
		{
        	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
        	Collection coll =  userBizLogic.getUsers(Constants.ACTIVITY_STATUS_ACTIVE);
        	request.setAttribute(Constants.USERLIST, coll);
		}
        catch(Exception e)
		{
        	e.printStackTrace();
		}


        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
}