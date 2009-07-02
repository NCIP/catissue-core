
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.bean.CollectionProtocolBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * Forward to collection protocol main page.
 * @author pathik_sheth
 *
 */
public class OpenCollectionProtocolAction extends BaseAction
{


	/**
	 * Overrides the executeSecureAction method of SecureAction class.
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
		CollectionProtocolForm formName = (CollectionProtocolForm) form;
		String operation = (String) request.getParameter(Constants.OPERATION);
		String pageOf = (String) request.getParameter(Constants.PAGE_OF);
		HttpSession session = request.getSession();
		if ("pageOfmainCP".equalsIgnoreCase(pageOf))
		{
			session.removeAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
			session.removeAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
		}
		CollectionProtocolBean cpBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		if (cpBean != null)
		{
			request.setAttribute("isParticipantReg", cpBean.isParticiapantReg());
			request.setAttribute(Constants.OPERATION, operation);
			String treeNode = "cpName_" + cpBean.getTitle();
			session.setAttribute(Constants.TREE_NODE_ID, treeNode);
		}
		request.setAttribute("formName", formName);
		return mapping.findForward(Constants.SUCCESS);
	}

}
