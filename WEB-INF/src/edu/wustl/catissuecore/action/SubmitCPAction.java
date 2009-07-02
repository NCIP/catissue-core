
package edu.wustl.catissuecore.action;

import java.util.Map;

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
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.security.exception.UserNotAuthorizedException;

/**
 * This is the method for submitting Collection Protocol.
 *
 * @author virender_mehta
 */
public class SubmitCPAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient Logger logger = Logger.getCommonLogger(SubmitCPAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
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
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// HashMap<String, String> resultMap = new HashMap<String, String>();
		String target = Constants.SUCCESS;
		try
		{
			HttpSession session = request.getSession();
			CollectionProtocolBean collectionProtocolBean = (CollectionProtocolBean) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
			String cptitle = collectionProtocolBean.getTitle();
			String treeNode = "cpName_" + cptitle;
			session.setAttribute(Constants.TREE_NODE_ID, treeNode);
			session.setAttribute("tempKey", treeNode);
			String operation = collectionProtocolBean.getOperation();
			if ("update".equals(operation))// "update"
			{
				target = "updateCP";
				return mapping.findForward(target);
			}
			else
			{

				CollectionProtocol collectionProtocol = CollectionProtocolUtil
						.populateCollectionProtocolObjects(request);
				CollectionProtocolDTO collectionProtocolDTO = getCoolectionProtocolDTO(
						collectionProtocol, session);
				insertCollectionProtocol(collectionProtocolDTO, request.getSession());
				collectionProtocolBean.setIdentifier(collectionProtocol.getId());
				CollectionProtocolUtil.updateSession(request, collectionProtocol.getId());
			}
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"object.add.successOnly", "Collection Protocol"));
			saveMessages(request, actionMessages);
		}
		catch (ApplicationException ex)
		{
			logger.debug(ex.getMessage(), ex);
			target = Constants.FAILURE;
			// String errorMsg = ex.getFormattedMessage();
			// resultMap.put(Constants.ERROR_DETAIL, errorMsg);
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError("errors.item", ex
					.getMessage()));
			SessionDataBean sessionDataBean = getSessionData(request);
			String userName = "";
			if (sessionDataBean != null)
			{
				userName = sessionDataBean.getUserName();
			}
			// To delegate UserNotAuthorizedException forward
			if (ex instanceof UserNotAuthorizedException)
			{
				UserNotAuthorizedException excp = (UserNotAuthorizedException) ex;
				String className = Utility.getActualClassName(CollectionProtocol.class.getName());
				String decoratedPrivilegeName = AppUtility.getDisplayLabelForUnderscore(excp
						.getPrivilegeName());
				String baseObject = "";
				if (excp.getBaseObject() != null && excp.getBaseObject().trim().length() != 0)
				{
					baseObject = excp.getBaseObject();
				}
				else
				{
					baseObject = className;
				}

				ActionError error = new ActionError("access.addedit.object.denied", userName,
						className, decoratedPrivilegeName, baseObject);

				actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
			}
			saveErrors(request, actionErrors);
		}
		return mapping.findForward(target);
	}

	/**
	 * @param collectionProtocol
	 *            Collection Protocol Object
	 * @param session
	 *            Session Object
	 * @return collectionProtocolDTO : collectionProtocolDTO
	 */
	private CollectionProtocolDTO getCoolectionProtocolDTO(CollectionProtocol collectionProtocol,
			HttpSession session)
	{
		CollectionProtocolDTO collectionProtocolDTO = new CollectionProtocolDTO();
		Map < String , SiteUserRolePrivilegeBean > rowIdBeanMap =
			(Map < String , SiteUserRolePrivilegeBean >) session
				.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
		collectionProtocolDTO.setCollectionProtocol(collectionProtocol);
		collectionProtocolDTO.setRowIdBeanMap(rowIdBeanMap);
		return collectionProtocolDTO;
	}

	/**
	 *
	 * @param collectionProtocolDTO : collectionProtocolDTO
	 * @param session : session
	 * @throws BizLogicException : BizLogicException
	 */
	private void insertCollectionProtocol(CollectionProtocolDTO collectionProtocolDTO,
			HttpSession session) throws BizLogicException
	{
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
		SessionDataBean sessionDataBean = (SessionDataBean) session
				.getAttribute(Constants.SESSION_DATA);
		bizLogic.insert(collectionProtocolDTO, sessionDataBean, 0);
	}

}
