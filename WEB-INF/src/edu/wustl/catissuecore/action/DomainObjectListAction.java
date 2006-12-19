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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
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
     */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        List list = null,showList = null;

        AbstractActionForm abstractForm = (AbstractActionForm)form;
        IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(abstractForm.getFormId());
        
        //Returns the page number to be shown.
        int pageNum = Integer.parseInt(request.getParameter(Constants.PAGE_NUMBER));
        
        //Gets the session of this request.
        HttpSession session = request.getSession();
        
        //The start index in the list of users to be approved/rejected.
        int startIndex = Constants.ZERO;
        
        //The end index in the list of users to be approved/rejected.
        int recordsPerPage = Integer.parseInt(XMLPropertyHandler.getValue(Constants.NO_OF_RECORDS_PER_PAGE));
        
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
        //Saves the number of results per page in the request.
        //Prafull:Commented this can be retrived directly from constants on jsp, so no need to save it in request.
//        request.setAttribute(Constants.RESULTS_PER_PAGE,Integer.toString(Constants.NUMBER_RESULTS_PER_PAGE));
        
        return mapping.findForward(Constants.SUCCESS);
    }
    
}