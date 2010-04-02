
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.ClinPortalIntegrationConstants;
import edu.wustl.common.action.XSSSupportedAction;
import edu.wustl.common.util.global.PasswordManager;

/**
 * This class allow us to make DE forms data entry using hardCoded URL
 * @author suhas_khot
 *
 */
public class UrlFeatureAction extends XSSSupportedAction
{

	/* (non-Javadoc)
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward executeXSS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//get all attributes as defined in clinportal and set in request
		//go to loginAction and then forward it to SCG page.
		LoginForm loginForm = (LoginForm) form;
		ActionForward actionForward = null;

		String path1 = request.getParameter("path");
		path1 = PasswordManager.decrypt(path1);
		String[] strArray = path1.split("&");
		String csmUserId = "";
		for (String s : strArray)
		{
			if (s.startsWith(ClinPortalIntegrationConstants.CSM_USER_ID))
			{
				String[] sa = s.split("=");
				csmUserId = sa[1];
			}
			else if (s.startsWith(ClinPortalIntegrationConstants.EVENTENTRYID))
			{
				String[] sa = s.split("=");
				request.getSession().setAttribute(ClinPortalIntegrationConstants.EVENTENTRYID,
						sa[1]);
			}
		}

//        String scgId=request.getParameter(ClinPortalIntegrationConstants.SCGID);
//        if(scgId!=null)
//        {
//            //Got to cCPbased view
//
//
//        }
      //  ParticipantBizLogic participantBizLogic = new ParticipantBizLogic();
        //String pId=participantBizLogic.getParticipantIDOnEmpiId(empiID);
       // String callBackURL=request.getParameter(ClinPortalIntegrationConstants.CALLBACK_URL);
        //request.getSession().setAttribute(ClinPortalIntegrationConstants.CALLBACK_URL, callBackURL);
       // String visitId=request.getParameter(ClinPortalIntegrationConstants.EVENTENTRYID);
       // request.getSession().setAttribute(ClinPortalIntegrationConstants.EVENTENTRYID,"24487");
        //String csmUserId=request.getParameter(ClinPortalIntegrationConstants.CSM_USER_ID);
		String loginName = AppUtility.getCsmUserName(csmUserId);
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
		newActionForward.setPath(path + "&" + path1);

		actionForward = newActionForward;
		return actionForward;
	}
}
