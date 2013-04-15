package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.DAO;

public class DistributionEventParametersAction  extends BaseAction {

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Long eventId = Long.parseLong(request.getParameter("id"));
		SessionDataBean sessionDataBean = (SessionDataBean) request
				.getSession().getAttribute(Constants.SESSION_DATA);
		DAO dao=null;
		try {
			dao = AppUtility.openDAOSession(sessionDataBean);
			DistributionBizLogic distributionBizLogic = new DistributionBizLogic();
			request.setAttribute("distEventDTO",distributionBizLogic.getDistributionEventDTO(eventId, dao));
		}finally{
			AppUtility.closeDAOSession(dao);
		}
		
		return  mapping.findForward(request.getParameter(Constants.PAGE_OF));
	}
}
