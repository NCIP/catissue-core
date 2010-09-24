
package edu.wustl.catissuecore.action;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.tree.StorageContainerTreeNode;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

public class TissueSiteTreeAction extends BaseAction
{

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

	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Vector<StorageContainerTreeNode> finalDataListVector = null;
		final String success = Constants.SUCCESS;
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		final String cdeName = request.getParameter("cdeName");
		final String propName = request.getParameter("propertyName");
		if (Constants.PAGE_OF_TISSUE_SITE.equals(pageOf))
		{
			final String dummyNodeName = Constants.DUMMY_NODE_NAME;
			final Vector<StorageContainerTreeNode> dataList = new Vector<StorageContainerTreeNode>();

			final StorageContainerTreeNode tissueSiteNode = new StorageContainerTreeNode(Long
					.valueOf(1), cdeName, cdeName, "Active");

			final StorageContainerTreeNode dummyContainerNode = new StorageContainerTreeNode(Long
					.valueOf(1), dummyNodeName, dummyNodeName, "Active");
			dummyContainerNode.setParentNode(tissueSiteNode);
			tissueSiteNode.getChildNodes().add(dummyContainerNode);

			dataList.add(tissueSiteNode);

			if (dataList != null)
			{
				finalDataListVector = new Vector<StorageContainerTreeNode>();
			}
			finalDataListVector = AppUtility.createTreeNodeVector(dataList, finalDataListVector);
			request.setAttribute(Constants.TREE_DATA, finalDataListVector);
			request.setAttribute("propName", propName);
		}
		return mapping.findForward(success);
	}
}
