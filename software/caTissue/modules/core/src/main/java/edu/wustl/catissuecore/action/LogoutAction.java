/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.action;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.global.Validator;
import edu.wustl.dao.JDBCDAO;

/**
 *
 *<p>
 * Title:
 * </p>
 *<p>
 * Description:
 * </p>
 *<p>
 * Copyright: (c) Washington University, School of Medicine 2005
 * </p>
 *<p>
 * Company: Washington University, School of Medicine, St. Louis.
 * </p>
 *
 * @author Aarti Sharma
 *@version 1.0
 */
public class LogoutAction extends BaseAction
{

    /**
     * @param mapping
     *            object of ActionMapping
     * @param form
     *            object of ActionForm
     * @param request
     *            object of HttpServletRequest
     * @param response
     *            object of HttpServletResponse
     * @throws Exception
     *             generic exception
     * @return ActionForward : ActionForward
     */
    @Override
    public ActionForward executeAction(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception
    {
    	String invalidRequest=(String)request.getParameter("invalidRequest");
    	if(invalidRequest!=null && "true".equals(invalidRequest))
    	{
    		final ActionErrors errors = new ActionErrors();
    		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.invalid","Request"));
    		// Report any errors we have discovered
    		if (!errors.isEmpty())
    		{
    			saveErrors(request, errors);
    		}
    	}
        final HttpSession session = request.getSession();

        // Delete Advance Query table if exists
        final SessionDataBean sessionData = getSessionData(request);
        if(sessionData!=null)
        {
        	// Advance Query table name with userID attached
        	final String tempTableName = Constants.QUERY_RESULTS_TABLE + "_" + sessionData.getUserId();

        	final JDBCDAO jdbcDao = AppUtility.openJDBCSession();
        	jdbcDao.deleteTable(tempTableName);
        	AppUtility.closeJDBCSession(jdbcDao);
        }
        session.invalidate();
        // Redirect to SSO logout page
        String ssoRedirectURL = null;
        if (!Validator.isEmpty(XMLPropertyHandler.getValue("sso.url")))
        {
        	String requestURL= request.getRequestURL().toString();
        	final int index = requestURL.indexOf("CasLogout");
        	String as = "?service="+ URLEncoder.encode(requestURL.substring(0, index)) + "CasLogin.do";
        	ssoRedirectURL = XMLPropertyHandler.getValue("sso.logout.url")+as;
        }
        ActionForward forwardTo = null;
        if (ssoRedirectURL == null)
        {
            forwardTo = (mapping.findForward(Constants.SUCCESS));
        }
        else
        {
            response.sendRedirect(ssoRedirectURL);
        }
        return forwardTo;
    }
}