/**
 * <p>Title: UserDetailsAction Class>
 * <p>Description:	UserDetailsAction is used to display details of user 
 * whose membership is to be approved/Rejected.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 29, 2005
 */
package edu.wustl.catissuecore.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * UserDetailsAction is used to display details of user whose membership is to be approved/Rejected.
 * @author gautam_shetty
 */
public class DomainObjectDetailsAction extends SecureAction
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
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {

        HttpSession session = request.getSession();
        List list = (List)session.getAttribute(Constants.ORIGINAL_DOMAIN_OBJECT_LIST);
        
        // To show empty summary in case of Approve User scenario
        request.getSession(true).setAttribute(Constants.USER_ROW_ID_BEAN_MAP, null);
        
        long identifier = Long.parseLong(request.getParameter(Constants.SYSTEM_IDENTIFIER));
        Iterator iterator = list.iterator();
        
        AbstractDomainObject currentDomainObject = null;
        Long prevIdentifier = null,nextIdentifier = null;
        
        while (iterator.hasNext())
        {
            currentDomainObject = (AbstractDomainObject)iterator.next();
            if (identifier == currentDomainObject.getId().longValue())
            {
                break;
            }
            prevIdentifier = currentDomainObject.getId();
        }
        
        if (iterator.hasNext())
        {
            AbstractDomainObject nextDomainObject = (AbstractDomainObject)iterator.next();
            nextIdentifier = nextDomainObject.getId();
        }
        
        AbstractActionForm abstractActionForm = (AbstractActionForm)form;
        /**
    	 * Name: Vijay Pande
    	 * Reviewer Name: Aarti Sharma
    	 * Instead of setAllValues() method retrieveForEditMode() method is called to bypass lazy loading error in domain object
    	 */	
        //abstractActionForm.setAllValues(currentDomainObject);
        DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
        defaultBizLogic.populateUIBean(currentDomainObject.getClass().getName(),currentDomainObject.getId(), abstractActionForm);
        
        request.setAttribute(Constants.PREVIOUS_PAGE,prevIdentifier);
        request.setAttribute(Constants.NEXT_PAGE,nextIdentifier);
        
        return mapping.findForward(Constants.SUCCESS);
    }
}
