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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;

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
        String pageOf = (String)request.getParameter(Constants.PAGEOF);
    	return mapping.findForward(pageOf);
    }
}