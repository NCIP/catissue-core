package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;

public class ForwardAdminAction extends CatissueBaseAction {

	@Override
	protected ActionForward executeCatissueAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SessionDataBean bean = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		if(bean.isAdmin())
		{
			return mapping.findForward("success");
		}
		else
		{
			return mapping.findForward("access_denied");
		}
		
	}

}
