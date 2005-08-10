/*
 * Created on Aug 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
 * @author mandar_deshmukh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TmplAction extends SpecimenProtocolAction
{
	   /**
     * Overrides the execute method of Action class.
     * Sets the various fields in User Add/Edit webpage.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	super.execute(mapping, form, request, response);
    	
    	String [] clinicalStatusArry = {Constants.SELECT_OPTION, "Pre-Opt","Post-Opt"};
    	request.setAttribute(Constants.CLINICAL_STATUS_LIST, clinicalStatusArry);
	    	
        return mapping.findForward(Constants.SUCCESS);
    }
}
