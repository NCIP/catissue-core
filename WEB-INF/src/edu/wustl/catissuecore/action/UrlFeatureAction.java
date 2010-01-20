
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.ClinPortalIntegrationConstants;

/**
 * This class allow us to make DE forms data entry using hardCoded URL
 * @author suhas_khot
 *
 */
public class UrlFeatureAction extends Action
{

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//get all attributes as defined in clinportal and set in request
		//go to loginAction and then forward it to SCG page.
	    LoginForm loginForm = (LoginForm) form;
	    ActionForward actionForward = null;
        String scgId=request.getParameter(ClinPortalIntegrationConstants.SCGID);
        if(scgId!=null)
        {
            //Got to cCPbased view


        }
      //  ParticipantBizLogic participantBizLogic = new ParticipantBizLogic();
        //String pId=participantBizLogic.getParticipantIDOnEmpiId(empiID);
        String callBackURL=request.getParameter(ClinPortalIntegrationConstants.CALLBACK_URL);
        //request.getSession().setAttribute(ClinPortalIntegrationConstants.CALLBACK_URL, callBackURL);
        String visitId=request.getParameter(ClinPortalIntegrationConstants.EVENTENTRYID);
        request.getSession().setAttribute(ClinPortalIntegrationConstants.EVENTENTRYID,visitId);
        String csmUserId=request.getParameter(ClinPortalIntegrationConstants.CSM_USER_ID);
        String loginName=AppUtility.getCsmUserName(csmUserId);
        String password = AppUtility.getPassord(loginName);
        loginForm.setPassword(password);
        loginForm.setLoginName(loginName);
		{
			//Forward to the Login
			actionForward = mapping.findForward(ClinPortalIntegrationConstants.LOGIN);
		}
			String path = actionForward.getPath();//+"&participantId="+pId;
			ActionForward newActionForward = new ActionForward();
			newActionForward.setName(actionForward.getName());
			newActionForward.setRedirect(false);
			newActionForward.setContextRelative(false);
			newActionForward.setPath(path);

			actionForward = newActionForward;
		return actionForward;
	}
}
