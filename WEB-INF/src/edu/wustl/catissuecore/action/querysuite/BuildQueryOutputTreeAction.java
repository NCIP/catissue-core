package edu.wustl.catissuecore.action.querysuite;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;

/**
 * This class is invoked when user clicks on a node from the tree. It loads the data required for tree formation.
 * @author deepti_shelar
 *
 */
public class BuildQueryOutputTreeAction extends BaseAction
{
	/**
	 * This method loads the data required for Query Output tree. 
	 * With the help of QueryOutputTreeBizLogic it generates a string which will be then passed to client side and tree is formed accordingly. 
	 * @param mapping mapping
	 * @param form form
	 * @param request request
	 * @param response response
	 * @throws Exception Exception
	 * @return ActionForward actionForward
	 */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		HttpSession session = request.getSession();
		Map<Long,OutputTreeDataNode> idNodesMap = (Map<Long,OutputTreeDataNode>)session.getAttribute(Constants.ID_NODES_MAP);
		List<OutputTreeDataNode> rootOutputTreeNodeList = (List<OutputTreeDataNode>)session.getAttribute(Constants.TREE_ROOTS);
		CategorySearchForm actionForm = (CategorySearchForm)form;
		SessionDataBean sessionData = getSessionData(request);
		String outputTreeStr = "";
		String idOfClickedNode = actionForm.getNodeId();		
		QueryOutputTreeBizLogic outputTreeBizLogic = new QueryOutputTreeBizLogic();
		String actualParentNodeId = idOfClickedNode.substring(idOfClickedNode.lastIndexOf(Constants.NODE_SEPARATOR)+2,idOfClickedNode.length());
		String[] nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		String treeNo = nodeIds[0];
		String treeNodeId = nodeIds[1]; 
		String uniqueId = treeNo+"_"+treeNodeId;
		OutputTreeDataNode parentNode = idNodesMap.get(uniqueId);
		OutputTreeDataNode root = QueryModuleUtil.getRootNodeOfTree(rootOutputTreeNodeList, treeNo);
		if(idOfClickedNode.endsWith(Constants.LABEL_TREE_NODE))
		{
			outputTreeStr = outputTreeBizLogic.updateTreeForLabelNode(idOfClickedNode,idNodesMap,sessionData);
		}
		else
		{
			String data = nodeIds[2];
			outputTreeStr = outputTreeBizLogic.updateTreeForDataNode(idOfClickedNode,parentNode, data, sessionData);	
		}
		response.setContentType("text/html");
		response.getWriter().write(outputTreeStr);
		return null;
	}
}
