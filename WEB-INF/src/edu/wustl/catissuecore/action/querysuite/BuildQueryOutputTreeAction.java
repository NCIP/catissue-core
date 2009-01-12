package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputTreeBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryDetails;
import edu.wustl.common.action.BaseAction;
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
		QueryDetails queryDetailsObj = new QueryDetails(session);
		//Map<String,OutputTreeDataNode> idNodesMap = (Map<String,OutputTreeDataNode>)session.getAttribute(Constants.ID_NODES_MAP);
		boolean hasConditionOnIdentifiedField = (Boolean)session.getAttribute(Constants.HAS_CONDITION_ON_IDENTIFIED_FIELD);
		//Map<EntityInterface ,List<EntityInterface>> mainEntityMap =(Map<EntityInterface ,List<EntityInterface>>)session.getAttribute(Constants.MAIN_ENTITY_MAP);
		CategorySearchForm actionForm = (CategorySearchForm)form;
		//SessionDataBean sessionData = getSessionData(request);
		String outputTreeStr = "";
		String nodeId = actionForm.getNodeId();	
		QueryOutputTreeBizLogic outputTreeBizLogic = new QueryOutputTreeBizLogic();
		String actualParentNodeId = nodeId.substring(nodeId.lastIndexOf(Constants.NODE_SEPARATOR)+2,nodeId.length());
		String[] nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		String treeNo = nodeIds[0];
		String treeNodeId = nodeIds[1]; 
		String uniqueId = treeNo+"_"+treeNodeId;
		OutputTreeDataNode parentNode = queryDetailsObj.getUniqueIdNodesMap().get(uniqueId);
		String randomNumber =(String)session.getAttribute("randomNumber");
		if(nodeId.endsWith(Constants.LABEL_TREE_NODE))
		{
			outputTreeStr = outputTreeBizLogic.updateTreeForLabelNode(nodeId,queryDetailsObj,
					hasConditionOnIdentifiedField);
		}
		else
		{
			String data = nodeIds[2];
			outputTreeStr = outputTreeBizLogic.updateTreeForDataNode(nodeId,parentNode, data, queryDetailsObj);	
		}
		response.setContentType("text/html");
		response.getWriter().write(outputTreeStr);
		return null;
	}
}
