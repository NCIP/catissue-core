
package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.TreeDataBizLogic;
import edu.wustl.catissuecore.tree.StorageContainerTreeNode;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.tree.TreeNodeImpl;

/**
 * This class class through AJAX call, when user clicks on [+] sign on UI, this
 * class will retrieve all the required containers under the clicked node.
 *
 * @author virender_mehta
 */
public class ShowChildNodes extends BaseAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form : object of ActionForm
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
		final PrintWriter out = response.getWriter();
		List<StorageContainerTreeNode> treeNodeDataVector = new LinkedList<StorageContainerTreeNode>();
		StringBuffer xmlData = new StringBuffer();
		final HttpSession session = request.getSession();
		final String pageOf = (String) session.getAttribute("PageForTree");
		final String nodeName = request.getParameter(Constants.NODE_NAME);
		final Long identifier = new Long(request.getParameter(Constants.CONTAINER_IDENTIFIER));
		final String parentId = request.getParameter(Constants.PARENT_IDENTIFIER);
		if (Constants.PAGE_OF_TISSUE_SITE.equals(pageOf))
		{
			treeNodeDataVector = AppUtility.getTissueSiteNodes(identifier, nodeName, parentId);
		}
		else
		{
			final TreeDataBizLogic treeBizLogic = new TreeDataBizLogic();
			treeNodeDataVector = treeBizLogic.getStorageContainers(identifier, nodeName, parentId);
		}

		response.setContentType("text/xml");
		xmlData = this.makeXMLData(treeNodeDataVector, xmlData);
		out.print(xmlData.toString());
		return null;
	}

	/**
	 * @param childVector
	 *            This vector contains all the nodes, when user clicks on [+]
	 *            sign then ajax call retrieve all the child node.
	 * @param xmlData
	 *            This is a string buffer and have all the information of the
	 *            node that is to be transfered to jsp.
	 * @return xmlData
	 */
	private StringBuffer makeXMLData(List<StorageContainerTreeNode> childVector,
			StringBuffer xmlData)
	{
		final Iterator<StorageContainerTreeNode> childItr = childVector.iterator();
		while (childItr.hasNext())
		{
			final StorageContainerTreeNode childNode = childItr.next();
			xmlData.append(childNode.getIdentifier());
			xmlData.append("~");
			xmlData.append(((TreeNodeImpl) childNode.getParentNode()).getIdentifier());
			xmlData.append("~");
			xmlData.append(((TreeNodeImpl) childNode.getParentNode()).getValue());
			xmlData.append("~");
			xmlData.append(childNode.getActivityStatus());
			xmlData.append("~");
			xmlData.append(childNode.getValue());
			xmlData.append("~");
			xmlData.append(childNode.getChildNodes());
			xmlData.append("#");
		}
		return xmlData;
	}

}
