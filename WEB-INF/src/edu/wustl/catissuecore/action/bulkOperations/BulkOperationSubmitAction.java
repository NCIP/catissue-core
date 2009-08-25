
package edu.wustl.catissuecore.action.bulkOperations;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.BulkEventOperationsForm;
import edu.wustl.catissuecore.bizlogic.bulkOperations.BulkOperationsBizlogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class BulkOperationSubmitAction extends BaseAction
{

	/**
	 * logger
	 */
	private transient final Logger logger = Logger.getCommonLogger(BulkOperationSubmitAction.class);

	/**
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		// Get Specimen Ids for which bulk events should be added
		final BulkEventOperationsForm bulkEventOperationsForm = (BulkEventOperationsForm) form;
		String target = bulkEventOperationsForm.getOperation();
		if (target == null)
		{
			target = Constants.SUCCESS;
		}

		final List<String> specimenIds = new ArrayList<String>(bulkEventOperationsForm
				.getSpecimenIds().keySet());
		request.setAttribute(Constants.SPECIMEN_ID, specimenIds);

		try
		{
			if (specimenIds != null && specimenIds.size() > 0)
			{
				// Insert bulk events
				final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
				final BulkOperationsBizlogic bizlogic = (BulkOperationsBizlogic) factory
						.getBizLogic(Constants.BULK_OPERATIONS_FORM_ID);
				bizlogic.insertEvents(bulkEventOperationsForm.getOperation(), this
						.getSessionData(request), specimenIds, bulkEventOperationsForm.getUserId(),
						bulkEventOperationsForm.getDateOfEvent(), bulkEventOperationsForm
								.getTimeInHours(), bulkEventOperationsForm.getTimeInMinutes(),
						bulkEventOperationsForm.getComments(), bulkEventOperationsForm
								.getEventSpecificData());

				ActionMessages messages = null;
				ActionErrors errors = null;
				if (specimenIds != null && specimenIds.size() > 0)
				{
					if (bulkEventOperationsForm.getOperation().equals(Constants.BULK_TRANSFERS))
					{
						messages = new ActionMessages();
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"bulk.operations.success", "transfer "));
					}
					else
					{
						messages = new ActionMessages();
						messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
								"bulk.operations.success", "disposal "));
					}
				}
				else
				{
					errors = new ActionErrors();
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError(
							"specimen.cart.size.zero"));
				}
				this.saveMessages(request, messages);
				this.saveErrors(request, errors);
			}
		}

		/*
		 * catch (UserNotAuthorizedException ex) { SessionDataBean
		 * sessionDataBean = getSessionData(request); String userName = "";
		 * if(sessionDataBean != null) { userName =
		 * sessionDataBean.getUserName(); } UserNotAuthorizedException excp =
		 * (UserNotAuthorizedException) ex; String className = new
		 * CommonAddEditAction
		 * ().getActualClassName(SpecimenEventParameters.class.getName());
		 * String decoratedPrivilegeName =
		 * Utility.getDisplayLabelForUnderscore(excp.getPrivilegeName()); String
		 * baseObject = ""; if (excp.getBaseObject() != null &&
		 * excp.getBaseObject().trim().length() != 0) { baseObject =
		 * excp.getBaseObject(); } else { baseObject = className; } ActionErrors
		 * errors = new ActionErrors(); ActionError error = new
		 * ActionError("access.addedit.object.denied", userName,
		 * className,decoratedPrivilegeName,baseObject);
		 * errors.add(ActionErrors.GLOBAL_ERROR, error); saveErrors(request,
		 * errors); }
		 */
		catch (final BizLogicException excp)
		{
			this.logger.debug(excp.getCustomizedMsg());
			final ActionErrors errors = new ActionErrors();
			final ActionError error = new ActionError("errors.item", excp.getCustomizedMsg());
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);
			this.logger.error(excp.getCustomizedMsg());
		}

		return mapping.findForward(target);
	}

}
