/**
 * <p>Title: AdvanceQueryInterfaceAction Class>
 * <p>Description:	This class initializes the fields of AdvanceQueryInterface.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Oct 11, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class AdvanceQueryInterfaceAction extends BaseAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in AdvanceQueryInterface.jsp Page.
     * */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {         
    	String operation = (String)request.getParameter(Constants.OPERATION);
    	Logger.out.debug("Advanced operation "+operation);
    	HttpSession session = request.getSession();
    	
    	//First time the Advance Search page loads set the Query view root as empty
    	if(operation==null)
    	{
    		Logger.out.debug("Inside initialization of root node");
    		DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    		session.setAttribute(Constants.ADVANCED_CONDITIONS_ROOT,root);
    	}
    	
    	String pageOf = (String)request.getParameter(Constants.PAGEOF);
    	request.setAttribute(Constants.MENU_SELECTED,new String("17") );
    	Logger.out.debug(Constants.MENU_SELECTED +" set in Advance Query Interface Action : 17  -- "  ); 
    	return mapping.findForward(pageOf);
    }
}