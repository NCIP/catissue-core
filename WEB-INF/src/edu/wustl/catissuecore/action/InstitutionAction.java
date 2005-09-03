/**
 * <p>Title: InstitutionAction Class>
 * <p>Description:	This class initializes the fields in the Institute.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 22, 2005
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

public class InstitutionAction extends SecureAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various drop down fields in Institute.jsp Page.
     * */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
}