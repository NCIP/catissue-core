package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.GSID.GSIDBatchUpdate;
import edu.wustl.catissuecore.actionForm.GSIDBatchUpdateForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.logger.Logger;

public class GSIDBatchUpdateStatusAction extends SecureAction 
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(GSIDBatchUpdateStatusAction.class);
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {		
		final SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		double percentage=0;
		boolean isError=false;
		if(sessionData.isAdmin())
		{
			percentage=GSIDBatchUpdate.getPercentage();
			isError=GSIDBatchUpdate.isError();
		}
		else
		{
			percentage=0;
		}
		request.setAttribute("statusPercentage", percentage);
		request.setAttribute("statusError",isError);
		return mapping.findForward(Constants.SUCCESS);
	}

}
