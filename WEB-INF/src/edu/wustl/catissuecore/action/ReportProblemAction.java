/**
 * <p>Title: ParticipantAction Class>
 * <p>Description:  This class initializes the fields in the ReportProblem webpage.</p>
 * Copyright:  Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ReportedProblemForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.CommonServiceLocator;


/**
 * This class initializes the fields in the ReportProblem webpage.
 * @author gautam_shetty
 */
public class ReportProblemAction extends Action
{
    /**
     * Overrides the execute method of Action class.
     * Sets the various in ReportProblem webpage.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if(obj!=null)
		{
			SessionDataBean sessionData = (SessionDataBean) obj;
			ReportedProblemForm reportedProblemForm = (ReportedProblemForm)form;
			if(reportedProblemForm!=null)
			{
				reportedProblemForm.setNameOfReporter(sessionData.getLastName()+", "+sessionData.getFirstName());
				reportedProblemForm.setFrom(sessionData.getUserName());
			}
		}
    	
		//Mandar 17-Apr-06 : 1667:- Application URL
		CommonServiceLocator.getInstance().setAppURL(request.getRequestURL().toString());
		
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit Problem Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        if (operation.equals(Constants.EDIT))
        {
            Long prevIdentifier = (Long)request.getAttribute(Constants.PREVIOUS_PAGE);
            request.setAttribute(Constants.PREVIOUS_PAGE,prevIdentifier);
            Long nextIdentifier = (Long)request.getAttribute(Constants.NEXT_PAGE);
            request.setAttribute(Constants.NEXT_PAGE,nextIdentifier);
        }
        request.setAttribute(Constants.ACTIVITYSTATUSLIST,Constants.REPORTED_PROBLEM_ACTIVITY_STATUS_VALUES);
        return mapping.findForward(Constants.SUCCESS);
    }
    
}
