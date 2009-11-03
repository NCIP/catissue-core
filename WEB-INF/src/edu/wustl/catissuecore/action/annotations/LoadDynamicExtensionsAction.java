/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.wustl.catissuecore.action.annotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.wustl.catissuecore.actionForm.AnnotationForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;

/**
 * @author preeti_munot
 *
 */
public class LoadDynamicExtensionsAction extends BaseAction
{

	/**
	 * @param mapping - mapping
	 * @param form - ActionForm
	 * @param request - HttpServletRequest object
	 * @param response - HttpServletResponse
	 * @return ActionForward
	 * @throws Exception - Exception
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		final AnnotationForm annotationForm = (AnnotationForm) form;
		//Get static entity id and store in cache
		String staticEntityId = annotationForm.getSelectedStaticEntityId();
		String[] conditions = new String[]{"All"};

		if (annotationForm.getConditionVal() != null)
		{
			conditions = annotationForm.getConditionVal();
		}
		if (staticEntityId == null)
		{
			staticEntityId = request.getParameter( "staticEntityId" );
		}
		final HttpSession session = request.getSession();
		session.setAttribute( AnnotationConstants.SELECTED_STATIC_ENTITYID, staticEntityId );
		session.setAttribute( AnnotationConstants.SELECTED_STATIC_RECORDID, conditions );

		//Get Dynamic extensions URL
		final String dynamicExtensionsURL = this.getDynamicExtensionsURL( request );
		//Set as request attribute
		request.setAttribute( AnnotationConstants.DYNAMIC_EXTN_URL_ATTRIB, dynamicExtensionsURL );
		return mapping.findForward( Constants.SUCCESS );
	}

	/**
	 * @param request : request)
	 * @return String : String
	 */
	private String getDynamicExtensionsURL(HttpServletRequest request)
	{
		//Get Dynamic extensions URL
		final String dynamicExtensionsURL = request.getContextPath()
				+ WebUIManager.getCreateContainerURL();
		final StringBuffer dynExturl = new StringBuffer();
		dynExturl.append( dynamicExtensionsURL );
		final SessionDataBean sessionbean = (SessionDataBean) request.getSession().getAttribute(
				edu.wustl.catissuecore.util.global.Constants.SESSION_DATA );

		final String userId = sessionbean.getUserId().toString();
		//request.getSession().getAttribute("SESSION_DATA").toString();
		String isAuthenticatedUser = "false";
		if (userId != null)
		{
			isAuthenticatedUser = "true";
		}
		//append container id if any
		if (request.getParameter( "containerId" ) != null)
		{
			dynExturl.append( '?' );
			dynExturl.append( WebUIManagerConstants.CONATINER_IDENTIFIER_PARAMETER_NAME );
			dynExturl.append( '=' );
			dynExturl.append( request.getParameter( "containerId" ) );
			/*dynamicExtensionsURL = dynamicExtensionsURL + "?"
					+ WebUIManagerConstants.CONATINER_IDENTIFIER_PARAMETER_NAME + "="
					+ request.getParameter("containerId");*/
			dynExturl.append( '&' );
			dynExturl.append( WebUIManager.getCallbackURLParamName() );
			dynExturl.append( '=' );
			dynExturl.append( request.getContextPath() );
			dynExturl.append( "&isAuthenticatedUser=" );
			dynExturl.append( isAuthenticatedUser );
			/*dynamicExtensionsURL = dynamicExtensionsURL + "&"
					+ WebUIManager.getCallbackURLParamName() + "=" + request.getContextPath()
					+ AnnotationConstants.CALLBACK_URL_PATH_ANNOTATION_DEFN
					+ "&isAuthenticatedUser=" + isAuthenticatedUser;*/
		}
		else
		{
			//append callback parameter
			dynExturl.append( '?' );
			dynExturl.append( WebUIManager.getCallbackURLParamName() );
			dynExturl.append( '=' );
			dynExturl.append( request.getContextPath() );
			dynExturl.append( AnnotationConstants.CALLBACK_URL_PATH_ANNOTATION_DEFN );
			dynExturl.append( "&isAuthenticatedUser=" );
			dynExturl.append( isAuthenticatedUser );
			/*dynamicExtensionsURL = dynamicExtensionsURL + "?"
					+ WebUIManager.getCallbackURLParamName() + "=" + request.getContextPath()
					+ AnnotationConstants.CALLBACK_URL_PATH_ANNOTATION_DEFN
					+ "&isAuthenticatedUser=" + isAuthenticatedUser;*/
		}
		return dynamicExtensionsURL;
	}
}
