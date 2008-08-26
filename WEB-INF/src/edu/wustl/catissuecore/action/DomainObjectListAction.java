/**
 * <p>Title: ApproveUserShowAction Class>
 * <p>Description:	ApproveUserShowAction is used to show the list of users
 * who have newly registered.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 25, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractDomainObjectFactory;
import edu.wustl.common.factory.MasterFactory;
import edu.wustl.common.util.XMLPropertyHandler;

/**
 * DomainObjectListAction is used to show the list of all 
 * values of the domain to be shown.
 * @author gautam_shetty
 */
public class DomainObjectListAction extends SecureAction
{

    /**
     * Overrides the execute method in Action.
     * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return value for ActionForward object
     */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        List list = null,showList = null;

        AbstractActionForm abstractForm = (AbstractActionForm)form;
        IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(abstractForm.getFormId());
        
        // Added by Ravindra to disallow Non Super Admin users to View Reported Problems
		SessionDataBean sessionDataBean = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		if(!sessionDataBean.isAdmin())
		{
			ActionErrors errors = new ActionErrors();
	        ActionError error = new ActionError("access.execute.action.denied");
	        errors.add(ActionErrors.GLOBAL_ERROR, error);
	        saveErrors(request, errors);

	        return mapping.findForward(Constants.ACCESS_DENIED);
		}
        
        //Returns the page number to be shown.
        int pageNum = Integer.parseInt(request.getParameter(Constants.PAGE_NUMBER));
        
        //Gets the session of this request.
        HttpSession session = request.getSession();
        
        //The start index in the list of users to be approved/rejected.
        int startIndex = Constants.ZERO;
        
        /**
		 * Name: Prafull
		 * Description: Query performance issue. Instead of saving complete query results in session, resultd will be fetched for each result page navigation.
		 * object of class QuerySessionData will be saved session, which will contain the required information for query execution while navigating through query result pages.
		 * 
		 * Changed Record per page constant. 
		 */
        //The end index in the list of users to be approved/rejected.
        int recordsPerPage = Integer.parseInt(XMLPropertyHandler.getValue(edu.wustl.common.util.global.Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
        
        if(request.getParameter(Constants.RESULTS_PER_PAGE) != null) 
    	{
        	recordsPerPage = Integer.parseInt(request.getParameter(Constants.RESULTS_PER_PAGE));       
    	}
        else if (session.getAttribute(Constants.RESULTS_PER_PAGE)!=null)
        {
        	recordsPerPage = Integer.parseInt(session.getAttribute(Constants.RESULTS_PER_PAGE).toString());
        } 
        int endIndex = recordsPerPage;
        
        AbstractDomainObjectFactory abstractDomainObjectFactory = (AbstractDomainObjectFactory)
        						MasterFactory.getFactory("edu.wustl.catissuecore.domain.DomainObjectFactory");
        if (pageNum == Constants.START_PAGE)
        {
            //If start page is to be shown retrieve the list from the database.
            if (abstractForm.getFormId() == Constants.APPROVE_USER_FORM_ID)
            {
                String [] whereColumnNames = {"activityStatus","activityStatus"};
                String [] whereColumnConditions = {"=","="};
                String [] whereColumnValues = {"New","Pending"};
                
                list = bizLogic.retrieve(abstractDomainObjectFactory.getDomainObjectName(abstractForm.getFormId()),
    					whereColumnNames,whereColumnConditions,whereColumnValues,Constants.OR_JOIN_CONDITION);
            }
            else
            {
                list = bizLogic.retrieve(abstractDomainObjectFactory.getDomainObjectName(abstractForm.getFormId()),
    					"activityStatus","Pending");
            }
            
            if (recordsPerPage > list.size())
            {
                endIndex = list.size();
            }
            
            //Save the list of users in the sesson.
            session.setAttribute(Constants.ORIGINAL_DOMAIN_OBJECT_LIST,list);
        }
        else
        {
            //Get the list of users from the session.
            list = (List)session.getAttribute(Constants.ORIGINAL_DOMAIN_OBJECT_LIST);
            if (recordsPerPage!= Integer.MAX_VALUE)
            {
	            //Set the start index of the users in the list.
	            startIndex = (pageNum-1) * recordsPerPage;
	            
	            //Set the end index of the users in the list.
	            endIndex = startIndex + recordsPerPage;
	            
	            if (endIndex > list.size())
	            {
	                endIndex = list.size();
	            }
            }
            else
            {
            	startIndex = 0;
            	endIndex = list.size();
            }
        }
        
        //Gets the list of users to be shown on the page.
        showList = list.subList(startIndex,endIndex);
        
        //Saves the list of users to be shown on the page in the request.
        request.setAttribute(Constants.SHOW_DOMAIN_OBJECT_LIST,showList);
        
        //Saves the page number in the request.
        request.setAttribute(Constants.PAGE_NUMBER,Integer.toString(pageNum));
        
        //Saves the total number of results in the request. 
        session.setAttribute(Constants.TOTAL_RESULTS,Integer.toString(list.size()));
        
        session.setAttribute(Constants.RESULTS_PER_PAGE,recordsPerPage+"");
    	String userDetailsLink = Constants.USER_DETAILS_SHOW_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"=";
        String problemDetailsLink = Constants.PROBLEM_DETAILS_ACTION+"?"+Constants.SYSTEM_IDENTIFIER+"=";
        int totalResults = Integer.parseInt((String)request.getSession().getAttribute(Constants.TOTAL_RESULTS));
  	  	int numResultsPerPage = Integer.parseInt((String)request.getSession().getAttribute(Constants.RESULTS_PER_PAGE));
  	  	int []RESULT_PERPAGE_OPTIONS=Constants.RESULT_PERPAGE_OPTIONS;
  
        request.setAttribute("userDetailsLink", userDetailsLink);
  	  	request.setAttribute("RESULT_PERPAGE_OPTIONS", RESULT_PERPAGE_OPTIONS);
  	  	request.setAttribute("pageNum", pageNum);
  	  	request.setAttribute("totalResults", totalResults);
  	  	request.setAttribute("numResultsPerPage", numResultsPerPage);
  	  	request.setAttribute("problemDetailsLink", problemDetailsLink);
  	  	
        //Saves the number of results per page in the request.
        //Prafull:Commented this can be retrived directly from constants on jsp, so no need to save it in request.
//        request.setAttribute(Constants.RESULTS_PER_PAGE,Integer.toString(Constants.NUMBER_RESULTS_PER_PAGE));
        
        return mapping.findForward(Constants.SUCCESS);
    }
    
}