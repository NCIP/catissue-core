/**
 * <p>Title: ReportedProblemAddAction Class>
 * <p>Description:	This Class is used to add a reported problem in the database.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 11, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ReportedProblemForm;
import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.exception.BizLogicException;
import edu.wustl.catissuecore.util.global.ApplicationProperties;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.SendEmail;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.logger.Logger;


/**
 * This Class is used to add a reported problem in the database.
 * @author gautam_shetty
 */
public class ReportedProblemAddAction extends Action
{
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        String target = null;
        
        ReportedProblemForm reportedProblemForm = (ReportedProblemForm)form;
        AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(reportedProblemForm.getFormId());
        ReportedProblem reportedProblem =null;
        
        try
        {
            reportedProblem = new ReportedProblem(reportedProblemForm);
            bizLogic.insert(reportedProblem,null,Constants.HIBERNATE_DAO);
            
            String adminEmailAddress = ApplicationProperties.getValue("email.administrative.emailAddress");
            String technicalSupportEmailAddress = ApplicationProperties.getValue("email.technicalSupport.emailAddress");
            String mailServer = ApplicationProperties.getValue("email.mailServer");
            
            SendEmail email = new SendEmail();
            boolean mailStatus = email.sendmail(adminEmailAddress,reportedProblem.getFrom(),
                    							mailServer,reportedProblem.getSubject(),
                    							reportedProblem.getMessageBody());
            
            if(mailStatus == true)
            {
                String statusMessageKey = String.valueOf(reportedProblemForm.getFormId()+
    					"."+String.valueOf(reportedProblemForm.isAddOperation()));

                request.setAttribute(Constants.STATUS_MESSAGE_KEY,statusMessageKey);
                target = new String(Constants.SUCCESS);
            }
            else
            {
                target = new String(Constants.FAILURE);
            }
        }
        catch(BizLogicException daoExp)
        {
            target = new String(Constants.FAILURE);
            Logger.out.error(daoExp.getMessage(),daoExp);
        }
        catch (UserNotAuthorizedException excp)
        {
            ActionErrors errors = new ActionErrors();
            SessionDataBean sessionDataBean =getSessionData(request);
            String userName;
        	if(sessionDataBean == null)
        	{
        	    userName = "";
        	}
        	else
        	{
        	    userName = sessionDataBean.getUserName();
        	}
        	ActionError error = new ActionError("access.addedit.object.denied",userName, reportedProblem .getClass().getName());
        	errors.add(ActionErrors.GLOBAL_ERROR,error);
        	saveErrors(request,errors);
        	target = new String(Constants.FAILURE);
            Logger.out.debug("excp "+excp.getMessage());
            Logger.out.error(excp.getMessage(), excp);
        }
        return (mapping.findForward(target));
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
}
