package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolRegistrationBizLogic;
import edu.wustl.catissuecore.util.global.CDMSIntegrationConstants;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;

/**
 * @author rinku rohra
 * 
 */
public class UrlCprViewAction extends BaseAction {
	protected ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		final StringBuffer path = new StringBuffer();
		final SessionDataBean sessionData = (SessionDataBean) request
				.getSession().getAttribute(Constants.SESSION_DATA);
		String cprId = request
				.getParameter(edu.wustl.common.util.global.Constants.SYSTEM_IDENTIFIER);
		CollectionProtocolRegistrationBizLogic cprBizLogic = new CollectionProtocolRegistrationBizLogic();
		List<Object> list = cprBizLogic.getCPIdandPartId(sessionData, cprId);
		if (!list.isEmpty()) {
			final Object[] id = (Object[]) list.get(0);
			path.append("&URLCollectionProtocolId=").append(id[0].toString())
					.append("&URLParticipantId=").append(id[1].toString());
		}
		ActionForward actionForward = mapping
				.findForward(CDMSIntegrationConstants.CP_BASED_VIEW);
		final ActionForward newActionForward = new ActionForward();
		newActionForward.setName(actionForward.getName());
		newActionForward.setRedirect(false);
		newActionForward.setContextRelative(false);
		newActionForward.setPath(actionForward.getPath() + path.toString());
		actionForward = newActionForward;
		return actionForward;
	}

}
