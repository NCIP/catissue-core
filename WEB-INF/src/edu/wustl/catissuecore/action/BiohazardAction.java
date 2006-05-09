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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.cde.CDEManager;

public class BiohazardAction  extends SecureAction
{
    /**
     * Overrides the execute method of Action class.
     * Initializes the various fields in Biohazard.jsp Page.
     * */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        //Sets the operation attribute to be used in the Add/Edit Institute Page. 
        List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_BIOHAZARD, null);
    	request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);
    	
        return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
}