/**
 * <p>Title: AddNewAction Class>
 * <p>Description:	This Class is used to maintain FormBean for AddNew operation</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Krunal Thakkar
 * @version 1.00
 * Created on Apr 12, 2006
 */

package edu.wustl.catissuecore.action;

import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.AddNewSessionDataBean;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to maintain FormBean for AddNew operation.
 * @author Krunal Thakkar
 */
public class AddNewAction extends Action 
{
    /**
     * Overrides the execute method of Action class.
     * Maintains FormBean for AddNew operation.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            Logger.out.debug("-----------------------------AddNewAction-----------------------------");
            //Populating ADDNewSessionDataBean to store FormBean into Session and Path to redirect 
	        AddNewSessionDataBean addNewSessionDataBean=new AddNewSessionDataBean();
	        
	        //Storing FormBean into AddNewSessionDataBean
	        addNewSessionDataBean.setAbstractActionForm((AbstractActionForm)form);
	        Logger.out.debug("ActionForm in AddNewAction----"+form);
	        
	        //Getting ForwardTo attribute from Request
	        String forwardTo=(String)request.getParameter(Constants.FORWARD_TO);
	        
	        Logger.out.debug("forwardTo stored in AddNewSessionDataBean------>"+forwardTo);
	        
	        //Storing forwardTo of InitializeAction into AddNewSessionDataBean
	        addNewSessionDataBean.setForwardTo(forwardTo);
	        
	        Logger.out.debug("AddNewAction:::Forward To ------------"+addNewSessionDataBean.getForwardTo());
	        
	        //Getting AddNewFor attribute from Request
	        String addNewFor =(String)request.getParameter(Constants.ADD_NEW_FOR);
	        
	        //Storing addNewFor of InitializeAction into AddNewSessionDataBean
	        addNewSessionDataBean.setAddNewFor(addNewFor);
	        
	        //Retrieving current Session
	        HttpSession session = request.getSession();
	        
	        //Storing Stack of FormBean into Session
	        Stack formBeanStack=(Stack)session.getAttribute(Constants.FORM_BEAN_STACK);
	        
	        if(formBeanStack ==null)
	        {
	            Logger.out.debug("Creating FormBeanStack in AddNewAction.......................");
	            formBeanStack=new Stack();
	        }
	        
	        formBeanStack.push(addNewSessionDataBean);
	        
	        session.setAttribute(Constants.FORM_BEAN_STACK, formBeanStack);
	     
	        //Storing required Attributes into Request
	        request.setAttribute(Constants.SUBMITTED_FOR , "AddNew");
	        
	        Logger.out.debug("ForwardTo attribute----------------"+request.getParameter(Constants.ADD_NEW_FORWARD_TO));
	    	
	        //Forwarding to ADD_NEW_FORWARD_TO Action
	    	return mapping.findForward((String)request.getParameter(Constants.ADD_NEW_FORWARD_TO));
        }
        catch(Exception e)
        {
            Logger.out.info("Exception: " + e.getMessage(), e);
            
            return mapping.findForward(Constants.SUCCESS);
        }
    }
}
