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

import edu.wustl.catissuecore.action.DomainObjectListAction;
import edu.wustl.catissuecore.actionForm.ccts.CctsEventNotificationsForm;
import edu.wustl.catissuecore.bizlogic.ccts.ICctsIntegrationBizLogic;
import edu.wustl.catissuecore.domain.ccts.Notification;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;

/**
 * I could not use {@link DomainObjectListAction}, because it loads all objects
 * in memory at once. Refactoring it to not do so would break a lot of other
 * code. So I had to go an alternative route and write this class instead.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class CctsEventNotificationsAction extends SecureAction {

	private ICctsIntegrationBizLogic bizLogic;

	@Override
	public ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CctsEventNotificationsForm form = (CctsEventNotificationsForm) actionForm;
		final SessionDataBean sessionDataBean = (SessionDataBean) request
				.getSession().getAttribute(Constants.SESSION_DATA);

		if (!sessionDataBean.isAdmin()) {
			return accessDenied(mapping, request);
		}

		int recordCount = bizLogic.getNotificationCount();
		int recordsPerPage = form.getNumResultsPerPage();
		int pageNumber = form.getPageNum();
		int firstResult = (pageNumber - 1) * recordsPerPage;
		Collection<Notification> data = bizLogic.getNotifications(firstResult,
				recordsPerPage);

		request.setAttribute("data", data);
		request.setAttribute("pageNum", pageNumber);		
		request.setAttribute("totalResults", recordCount);
		request.setAttribute("numResultsPerPage", recordsPerPage);
		request.setAttribute("RESULT_PERPAGE_OPTIONS",
				Constants.RESULT_PERPAGE_OPTIONS);
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
