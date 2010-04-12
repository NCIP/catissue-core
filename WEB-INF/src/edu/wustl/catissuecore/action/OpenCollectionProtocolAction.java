
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
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final CollectionProtocolForm formName = (CollectionProtocolForm) form;
		final String operation = request.getParameter(Constants.OPERATION);
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		final HttpSession session = request.getSession();
		if ("pageOfmainCP".equalsIgnoreCase(pageOf))
		{
			session.removeAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
			session.removeAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
		}
		final CollectionProtocolBean cpBean = (CollectionProtocolBean) session
				.getAttribute(Constants.COLLECTION_PROTOCOL_SESSION_BEAN);
		if (cpBean != null)
		{
			request.setAttribute("isParticipantReg", cpBean.isParticiapantReg());
			request.setAttribute(Constants.OPERATION, operation);
			final String treeNode = "cpName_" + cpBean.getTitle();
			session.setAttribute(Constants.TREE_NODE_ID, treeNode);
			request.setAttribute("labelGeneration", cpBean.isGenerateLabel());
		}
		String labelGen= Boolean.toString(formName.isGenerateLabel());
		request.setAttribute("formName", formName);
		request.setAttribute("labelGen", labelGen);
//		System.out.println("formName   &&****#$#$#$#$$##$#$   :  "+ cpBean.isGenerateLabel());
		return mapping.findForward(Constants.SUCCESS);
	}

}
