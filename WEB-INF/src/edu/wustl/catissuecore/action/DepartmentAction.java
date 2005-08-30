/**
 * <p>Title: DepartmentAction Class</p>
 * <p>Description:	This class initializes the fields in the Department Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on May 23rd, 2005
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

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * @author gautam_shetty
 */

public class DepartmentAction extends SecureAction
{
    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in Department Add/Edit webpage.
     * */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	/*System.out.println("heree rerere rere...............");
    	Logger.out.info("heree rerere rere...............");
    	CDE cde = CDEManager.cdeManager.getCDE("Sex");
    	System.out.println("CDE>> "+cde.getPublicId()+" : "+cde.getLongName());
    	Logger.out.info("After heree rerere rere...............");*/
    	
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION,operation);
        
        return mapping.findForward(Constants.SUCCESS);
    }
}