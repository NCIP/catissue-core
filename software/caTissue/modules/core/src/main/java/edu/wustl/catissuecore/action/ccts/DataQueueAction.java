/**
 * 
 */
package edu.wustl.catissuecore.action.ccts;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.ccts.DataQueueForm;
import edu.wustl.catissuecore.bizlogic.IParticipantBizLogic;
import edu.wustl.catissuecore.bizlogic.ccts.ICctsIntegrationBizLogic;
import edu.wustl.catissuecore.bizlogic.ccts.IErrorsReporter;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ccts.DataQueue;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.lookup.DefaultLookupResult;
import edu.wustl.common.participant.utility.ParticipantManagerUtility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;

/**
 * Implements processing of items in the CCTS data queue.
 * 
 * @author Denis G. Krylov
 * 
 */
public final class DataQueueAction extends SecureAction {

	private static final Logger logger = Logger
			.getCommonLogger(DataQueueAction.class);

	private static final String QUEUE_ITEM_KEY = "queueItem";
	private static final String TOTAL_KEY = "total";
	private static final String PARTICIPANT_COMPARISON_RESULTS_KEY = "partCompResults";
	private static final String REGISTRATION_COMPARISON_RESULTS_KEY = "regCompResults";
	private static final String CONVERSION_ERRORS_KEY = "convErrors";
	private static final String CONVERTED_REGISTRATION_KEY = "convertedReg";

	private ICctsIntegrationBizLogic bizLogic;

	private IParticipantBizLogic participantBizLogic;

	@Override
	public ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		DataQueueForm form = (DataQueueForm) actionForm;
		final SessionDataBean sessionDataBean = (SessionDataBean) request
				.getSession().getAttribute(Constants.SESSION_DATA);

		/**
		 * if (!sessionDataBean.isAdmin()) { return accessDenied(mapping,
		 * request); }
		 **/

		if (form.isRejected()) {
			return executeReject(mapping, form, request, response);
		}

		if (form.isAccepted()) {
			return executeAccept(mapping, form, request, response,
					sessionDataBean);
		}

		// default branch: init.
		return executeInit(mapping, request);
	}

	/**
	 * @param mapping
	 * @param request
	 * @return
	 * @throws DAOException
	 * @throws BizLogicException 
	 */
	private ActionForward executeInit(ActionMapping mapping,
			HttpServletRequest request) throws DAOException, BizLogicException {
		final SessionDataBean sessionDataBean = (SessionDataBean) request
				.getSession().getAttribute(Constants.SESSION_DATA);
		try {
			bizLogic.autoProcessDataQueue();
		} catch (Exception e) {
			logger.error(e.toString(), e);
			addActionError(
					request,
					"Auto-processing of the clinical data queue has failed due to an internal problem. Please perform the processing manually.");
		}

		int total = bizLogic.getPendingDataQueueItemsCount();
		DataQueue queueItem = bizLogic.pickDataQueueItemForProcessing();
		final Set<String> errors = new LinkedHashSet<String>();
		if (queueItem != null) {
			// do security checks on this item first.
			if (!sessionDataBean.isAdmin() && !bizLogic.isAuthorized(sessionDataBean, queueItem)) {
				return accessDenied(mapping, request);
			}
			// access granted, so we can proceed.
			request.getSession().setAttribute(QUEUE_ITEM_KEY, queueItem);
			request.setAttribute(TOTAL_KEY, total);
			request.setAttribute(QUEUE_ITEM_KEY, queueItem);
			request.setAttribute(CONVERSION_ERRORS_KEY, errors);
			final IErrorsReporter errorsReporter = new IErrorsReporter() {
				@Override
				public void error(String msg) {
					errors.add(msg);
				}
			};
			request.setAttribute(PARTICIPANT_COMPARISON_RESULTS_KEY, bizLogic
					.getParticipantComparison(queueItem, errorsReporter));
			request.setAttribute(REGISTRATION_COMPARISON_RESULTS_KEY, bizLogic
					.getRegistrationComparison(queueItem, errorsReporter));
			prepareParticipantMatchingData(request, queueItem, errorsReporter);
			if (queueItem.isRegistrationRelated()) {
				request.setAttribute(CONVERTED_REGISTRATION_KEY,
						bizLogic.convertRegistration(queueItem, null));
			}
		} else {
			request.getSession().removeAttribute(QUEUE_ITEM_KEY);
		}

		return mapping.findForward(Constants.SUCCESS);
	}

	private void prepareParticipantMatchingData(HttpServletRequest request,
			DataQueue queueItem, IErrorsReporter errorsReporter)
			throws DAOException {
		final HttpSession session = request.getSession();
		List<DefaultLookupResult> matchPartpantLst = bizLogic
				.getParticipantMatchingResults(queueItem, errorsReporter);
		if (!matchPartpantLst.isEmpty()) {
			List<String> columnList = ParticipantManagerUtility
					.getColumnHeadingList();
			request.setAttribute(
					edu.wustl.common.util.global.Constants.SPREADSHEET_COLUMN_LIST,
					columnList);
			final List<List<String>> pcpantDisplayLst = ParticipantManagerUtility
					.getParticipantDisplayList(matchPartpantLst);
			request.setAttribute(
					edu.wustl.common.participant.utility.Constants.SPREADSHEET_DATA_LIST,
					pcpantDisplayLst);
			session.setAttribute("MatchedParticpant", matchPartpantLst);
			request.setAttribute("participantId", "");
		} else {
			session.removeAttribute("MatchedParticpant");
		}

	}

	private ActionForward executeReject(ActionMapping mapping,
			DataQueueForm form, HttpServletRequest request,
			HttpServletResponse response) {
		DataQueue queueItem = (DataQueue) request.getSession().getAttribute(
				QUEUE_ITEM_KEY);
		if (queueItem != null) {
			bizLogic.rejectQueueItem(queueItem);
			addMessage(request, "DataQueue.msg.rejected", null);
		}
		return mapping.findForward(Constants.INIT);
	}

	private ActionForward executeAccept(ActionMapping mapping,
			DataQueueForm form, HttpServletRequest request,
			HttpServletResponse response, SessionDataBean sessionDataBean)
			throws DAOException, BizLogicException {
		DataQueue queueItem = (DataQueue) request.getSession().getAttribute(
				QUEUE_ITEM_KEY);
		if (queueItem != null) {
			try {
				Participant participant = null;
				if (NumberUtils.isDigits(form.getMatchedParticipantId())) {
					participant = participantBizLogic
							.getParticipantById(NumberUtils.toLong(form
									.getMatchedParticipantId()));
				}
				bizLogic.acceptQueueItem(queueItem,
						sessionDataBean.getUserName(), participant);
				addMessage(request, "DataQueue.msg.accepted", null);
				if (queueItem.getParticipant() != null) {
					addMessage(request, "DataQueue.msg.participantLink",
							new Object[] { queueItem.getParticipant().getId() });
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				addActionError(request, e);
				return executeInit(mapping, request);
			}

		}
		return mapping.findForward(Constants.INIT);
	}

	/**
	 * @param request
	 * @param e
	 */
	private void addActionError(HttpServletRequest request, Exception e) {
		final String message = e.getMessage();
		addActionError(request, message);
	}

	/**
	 * @param request
	 * @param message
	 */
	private void addActionError(HttpServletRequest request, final String message) {
		ActionErrors actionErrors = new ActionErrors();
		ActionError actionError = new ActionError("errors.item", message);
		actionErrors.add(ActionErrors.GLOBAL_ERROR, actionError);
		saveErrors(request, actionErrors);
	}

	private void addMessage(HttpServletRequest request, String key,
			Object[] values) {
		final HttpSession session = request.getSession();
		ActionMessages messages;
		if (session.getAttribute(Globals.MESSAGE_KEY) != null) {
			messages = (ActionMessages) session
					.getAttribute(Globals.MESSAGE_KEY);
		} else {
			messages = new ActionMessages();
		}

		ActionMessage msg = new ActionMessage(key, values);
		messages.add("message", msg);
		session.setAttribute(Globals.MESSAGE_KEY, messages);

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
				"access.queue.denied");
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

	/**
	 * @return the participantBizLogic
	 */
	public final IParticipantBizLogic getParticipantBizLogic() {
		return participantBizLogic;
	}

	/**
	 * @param participantBizLogic
	 *            the participantBizLogic to set
	 */
	public final void setParticipantBizLogic(
			IParticipantBizLogic participantBizLogic) {
		this.participantBizLogic = participantBizLogic;
	}

}
