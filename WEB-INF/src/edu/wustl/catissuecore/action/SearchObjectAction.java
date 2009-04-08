/*
 * Created on Sep 3, 2005
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
import edu.wustl.common.util.logger.Logger;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SearchObjectAction extends Action
{
    
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Long identifier = Long.valueOf(request.getParameter(Constants.SYSTEM_IDENTIFIER));
        request.setAttribute(Constants.SYSTEM_IDENTIFIER, identifier);
        
        String pageOf = request.getParameter(Constants.PAGE_OF);
        request.setAttribute(Constants.PAGE_OF, pageOf);
        
        Logger.out.debug("identifier:"+identifier+" PAGEOF:"+pageOf);
        return mapping.findForward(pageOf);
    }

}
