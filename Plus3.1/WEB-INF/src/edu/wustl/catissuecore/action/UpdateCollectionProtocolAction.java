
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.dto.CollectionProtocolDTO;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.UserNotAuthorizedException;

// TODO: Auto-generated Javadoc
/**
 * The Class UpdateCollectionProtocolAction.
 *
 * @author renuka_bajpai
 */
public class UpdateCollectionProtocolAction extends BaseAction
{

	/** logger. */
	private static final Logger LOGGER = Logger
			.getCommonLogger(UpdateCollectionProtocolAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return ActionForward : ActionForward
	 *
	 * @throws Exception generic exception
	 */

	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		String target = Constants.SUCCESS;

		try
		{
			final CollectionProtocol collectionProtocol = CollectionProtocolUtil
					.populateCollectionProtocolObjects(request);

			final CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) (request
					.getSession()).getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
			final HttpSession session = request.getSession();
			final SessionDataBean sessionDataBean = (SessionDataBean) session
					.getAttribute(Constants.SESSION_DATA);
			final CollectionProtocolDTO collectionProtocolDTO = AppUtility
					.getCoolectionProtocolDTO(collectionProtocol, session);
			bizLogic.update(collectionProtocolDTO, null, 0, sessionDataBean);
			CollectionProtocolUtil.updateSession(request, collectionProtocol.getId());
			target = getTarget(collectionProtocolBean);
			final ActionMessages actionMessages = new ActionMessages();
			actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"object.edit.successOnly", "Collection Protocol"));
			this.saveMessages(request, actionMessages);

		}
		catch (final Exception exception)
		{
			LOGGER.error(exception.getMessage(), exception);
			final ActionErrors actionErrors = new ActionErrors();

			if (exception instanceof UserNotAuthorizedException)
			{
				final UserNotAuthorizedException excep = (UserNotAuthorizedException) exception;
				final SessionDataBean sessionDataBean = (SessionDataBean) request.getSession()
						.getAttribute(Constants.SESSION_DATA);
				String userName = "";

				if (sessionDataBean != null)
				{
					userName = sessionDataBean.getUserName();
				}
				final String className = Constants.COLLECTION_PROTOCOL;
				final String decoratedPrivilegeName = AppUtility.getDisplayLabelForUnderscore(excep
						.getPrivilegeName());
				String baseObject = "";
				baseObject = getBaseObj(excep, className);

				final ActionError error = new ActionError("access.addedit.object.denied", userName,
						className, decoratedPrivilegeName, baseObject);
				actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
				this.saveErrors(request, actionErrors);
				target = Constants.FAILURE;
				return (mapping.findForward(target));
			}

			actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item",
					exception.getMessage()));
			this.saveErrors(request, actionErrors);
			target = Constants.FAILURE;
		}
		return mapping.findForward(target);
	}

	/**
	 * Gets the target.
	 *
	 * @param collectionProtocolBean the collection protocol bean
	 *
	 * @return the target
	 */
	private String getTarget(final CollectionProtocolBean collectionProtocolBean)
	{
		String target;
		if (Constants.DISABLED.equals(collectionProtocolBean.getActivityStatus()))
		{
			target = "disabled";
		}
		else
		{
			target = Constants.SUCCESS;
		}
		return target;
	}

	/**
	 * Gets the base obj.
	 *
	 * @param exception the exception
	 * @param className the class name
	 *
	 * @return the base obj
	 */
	private String getBaseObj(final UserNotAuthorizedException exception, final String className)
	{
		String baseObject;
		if (exception.getBaseObject() != null && exception.getBaseObject().trim().length() != 0)
		{
			baseObject = exception.getBaseObject();
		}
		else
		{
			baseObject = className;
		}
		return baseObject;
	}
}
