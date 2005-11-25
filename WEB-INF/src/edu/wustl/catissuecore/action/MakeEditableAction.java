/*
 * Created on Jun 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MakeEditableAction extends Action
{
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String target = new String(Constants.EDIT);
        request.setAttribute(mapping.getAttribute(),form);
        
        String editable = request.getParameter(Constants.EDITABLE);
        if (editable.equals("true"))
        {
            target = new String(Constants.VIEW);
        }
        
        return mapping.findForward(target);
    }

}
