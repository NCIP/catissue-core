/**
 * <p>Title: BiohazardAction Class>
 * <p>Description:	This class initializes the fields of Biohazard.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
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

public class BiohazardAction extends BaseAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in Biohazard.jsp Page.
     * */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, Constants.BIOHAZARD_TYPE_ARRAY);
        
        return mapping.findForward(Constants.SUCCESS);
    }
}