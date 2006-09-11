/**
 * <p>Title: AdvanceQueryInterfaceDefaultPageAction Class>
 * <p>Description:	This class initializes the fields of AdvanceQueryInterfaceDefaultPage.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Prafull Kadam
 * @version 1.00
 * Created on Sept 8, 2006
 */
package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.logger.Logger;


public class AdvanceQueryInterfaceDefaultPageAction extends BaseAction
{

	/**
	 * Overrides the execute method of Action class.
	 * Initializes the various fields in AdvanceQueryInterface.jsp Page.
	 * */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {         
    	String pageOf = (String)request.getParameter(Constants.PAGEOF);
    	request.setAttribute(Constants.MENU_SELECTED,new String("17") );
    	Logger.out.debug(Constants.MENU_SELECTED +" set in Advance Query Default Page Action : 17  -- "  );
    	return mapping.findForward(Constants.SUCCESS);
	}

}
