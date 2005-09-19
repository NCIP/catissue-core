/**
 * <p>Title: FrozenEventParametersAction Class>
 * <p>Description:	This class initializes the fields in the FrozenEventParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 28, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the FrozenEventParameters Add/Edit webpage.
 * @author mandar deshmukh
 */
public class SpecimenEventParametersAction  extends SecureAction
{
	protected void setRequestParameters(HttpServletRequest request)
	{
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit FrozenEventParameters Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        //Sets the minutesList attribute to be used in the Add/Edit FrozenEventParameters Page.
        request.setAttribute(Constants.MINUTESLIST, Constants.MINUTESARRAY);

        //Sets the hourList attribute to be used in the Add/Edit FrozenEventParameters Page.
        request.setAttribute(Constants.HOURLIST, Constants.HOURARRAY);
        
        //The id of specimen of this event.
        String specimenId = request.getParameter(Constants.SPECIMEN_ID); 
        request.setAttribute(Constants.SPECIMEN_ID, specimenId);
        
        String isRNA = request.getParameter(Constants.IS_RNA); 
        request.setAttribute(Constants.IS_RNA, isRNA);
        
        //The Add/Edit status message
        request.setAttribute(Constants.STATUS_MESSAGE_KEY, request.getAttribute(Constants.STATUS_MESSAGE_KEY));
       
                
       try
       {
           	UserBizLogic userBizLogic = (UserBizLogic)BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
        	Collection coll =  userBizLogic.getUsers(Constants.ACTIVITY_STATUS_ACTIVE);
        	
        	request.setAttribute(Constants.USERLIST, coll);
        }
        catch (Exception exc)
        {
            Logger.out.error(exc.getMessage());
            exc.printStackTrace();
        }
	}
    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in FrozenEventParameters Add/Edit webpage.
     * */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	setRequestParameters(request);
    	
    	EventParametersForm eventParametersForm = (EventParametersForm)form;
    	/*HttpSession session = request.getSession(true);
    	SessionDataBean sessionData = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
    	Logger.out.debug("sessionData.getUserId()*********"+sessionData.getUserId());
    	long userId = sessionData.getUserId().longValue();
    	//eventParametersForm.setUserId(userId);
    	Logger.out.debug("(String)request.getParameter(Constants.PAGEOF)******************"+(String)request.getParameter(Constants.PAGEOF));
    	*/
    	return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
}