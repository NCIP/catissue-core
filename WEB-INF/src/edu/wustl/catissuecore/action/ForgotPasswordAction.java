/**
 * <p>Title: ForgotPasswordAction Class>
 * <p>Description:  This class initializes the fields in the ForgotPassword webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 19, 2005
 */
package edu.wustl.catissuecore.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;


/**
 * This class initializes the fields in the ForgotPassword webpage.
 * @author gautam_shetty
 */
public class ForgotPasswordAction extends Action
{
    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in ForgotPassword webpage.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
		//Mandar 17-Apr-06 : 1667:- Application URL
    	Utility.setApplicationURL(request.getRequestURL().toString());
        return mapping.findForward(Constants.SUCCESS);
    }
    
}
