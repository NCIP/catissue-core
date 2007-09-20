package edu.wustl.catissuecore.action.querysuite;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class is invoked when user clicks on a node from the tree. It loads the data required for grid formation.
 * @author deepti_shelar
 */
public class ShowGridAction extends BaseAction
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
		Map<String, OutputTreeDataNode> uniqueIdNodesMap = (Map<String, OutputTreeDataNode>) session.getAttribute(Constants.ID_NODES_MAP);
		Map<Long, Map<AttributeInterface, String>> columnMap = (Map<Long, Map<AttributeInterface, String>>) session.getAttribute(
				Constants.ID_COLUMNS_MAP);
		List<OutputTreeDataNode> rootOutputTreeNodeList = (List<OutputTreeDataNode>)session.getAttribute(Constants.TREE_ROOTS);
		SessionDataBean sessionData = getSessionData(request);
		SelectedColumnsMetadata selectedColumnsMetadata = (SelectedColumnsMetadata)session.getAttribute(Constants.SELECTED_COLUMN_META_DATA);
		String idOfClickedNode = request.getParameter("nodeId");
 		Map spreadSheetDatamap = null;
		String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		int recordsPerPage = new Integer(recordsPerPageStr);
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		String actualParentNodeId = idOfClickedNode.substring(idOfClickedNode.lastIndexOf(Constants.NODE_SEPARATOR) + 2, idOfClickedNode.length());
		if (idOfClickedNode.endsWith(Constants.LABEL_TREE_NODE))
		{
			spreadSheetDatamap = outputSpreadsheetBizLogic.processSpreadsheetForLabelNode(uniqueIdNodesMap,rootOutputTreeNodeList, columnMap, sessionData, idOfClickedNode,recordsPerPage,selectedColumnsMetadata);
		}
		else
		{
			spreadSheetDatamap = outputSpreadsheetBizLogic.processSpreadsheetForDataNode(uniqueIdNodesMap, rootOutputTreeNodeList, sessionData, actualParentNodeId,recordsPerPage,selectedColumnsMetadata);
		}
		QueryModuleUtil.setGridData(request, spreadSheetDatamap);
		return mapping.findForward(Constants.SUCCESS);
	}
		
}
