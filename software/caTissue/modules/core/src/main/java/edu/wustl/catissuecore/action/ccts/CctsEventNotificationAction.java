/**
 * 
 */
package edu.wustl.catissuecore.action.ccts;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ccts.CctsEventNotificationForm;
import edu.wustl.catissuecore.bizlogic.ccts.ICctsIntegrationBizLogic;
import edu.wustl.catissuecore.domain.ccts.Notification;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;

/**
 * Displays details about a selected CCTS Notification.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class CctsEventNotificationAction extends SecureAction {

	public static final String NOTIFICATION_KEY = "notification";
	private ICctsIntegrationBizLogic bizLogic;

	@Override
	public ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CctsEventNotificationForm form = (CctsEventNotificationForm) actionForm;
		final SessionDataBean sessionDataBean = (SessionDataBean) request
				.getSession().getAttribute(Constants.SESSION_DATA);

		if (!sessionDataBean.isAdmin()) {
			return accessDenied(mapping, request);
		}

		int msgId = form.getMsgId();
		Notification notification = bizLogic.getNotificationById(msgId);
		if (notification != null) {
			request.setAttribute(NOTIFICATION_KEY, notification);			
		} else {
			final ActionErrors errors = new ActionErrors();
			final ActionError error = new ActionError(
					"CctsEventNotificationForm.error.notifNotFound");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);
		}
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * @param mapping
	 * @param request
	 * @return
	 */
	protected ActionForward accessDenied(ActionMapping mapping,
			HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		final ActionError error = new ActionError(
				"access.execute.action.denied");
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		this.saveErrors(request, errors);
		return mapping.findForward(Constants.ACCESS_DENIED);
	}

	/**
	 * @return the bizLogic
	 */
	public ICctsIntegrationBizLogic getBizLogic() {
		return bizLogic;
	}

	/**
	 * @param bizLogic
	 *            the bizLogic to set
	 */
	public void setBizLogic(ICctsIntegrationBizLogic bizLogic) {
		this.bizLogic = bizLogic;
	}

}
