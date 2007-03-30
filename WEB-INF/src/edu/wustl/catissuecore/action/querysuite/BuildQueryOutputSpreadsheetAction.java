
package edu.wustl.catissuecore.action.querysuite;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * This class is called when user clicks on a node of out put tree, it updates spreadsheet view for results.
 * @author deepti_shelar
 */
public class BuildQueryOutputSpreadsheetAction extends BaseAction
{
	/**
	 * This method loads the data required for Query Output spreadsheet. 
	 * With the help of QueryOutputSpreadsheetBizLogic it generates a string which will be then passed to client side and spreadsheet
	 * is formed accordingly.String outputTreeStr consists of all nodes with comma seperated string for its id, display name, object name , 
	 * parentId, parent Object name.Such string elements for each child node are seperated by "|".
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
		Map<Long, OutputTreeDataNode> idNodesMap = (Map<Long, OutputTreeDataNode>) request.getSession().getAttribute(Constants.ID_NODES_MAP);
		Map<Long, Map<AttributeInterface, String>> columnMap = (Map<Long, Map<AttributeInterface, String>>) request.getSession().getAttribute(
				Constants.ID_COLUMNS_MAP);
		SessionDataBean sessionData = getSessionData(request);
		CategorySearchForm actionForm = (CategorySearchForm) form;
		String nodeId = actionForm.getNodeId();
		Map spreadSheetDatamap = null;
		String actualParentNodeId = nodeId.substring(nodeId.lastIndexOf(Constants.NODE_SEPARATOR) + 2, nodeId.length());
		if (nodeId.endsWith(Constants.LABEL_TREE_NODE))
		{
			String[] nodeIds = nodeId.split(Constants.NODE_SEPARATOR);
			spreadSheetDatamap = processSpreadsheetForLabelNode(idNodesMap, columnMap, sessionData, actualParentNodeId, nodeIds);
		}
		else
		{
			spreadSheetDatamap = processSpreadsheetForDataNode(idNodesMap, columnMap, sessionData, actualParentNodeId);
		}
		String outputSpreadsheetDataStr = QueryModuleUtil.prepareOutputSpreadsheetDataString(spreadSheetDatamap);
		response.setContentType("text/html");
		response.getWriter().write(outputSpreadsheetDataStr);
		return null;
	}

	/**
	 * Processes spreadsheet data for data node which user has clicked.
	 * @param idNodesMap Map<Long, OutputTreeDataNode> map of ids and nodes present in tree
	 * @param columnMap map for column names for attributes for each node in query
	 * @param sessionData session data bean
	 * @param actualParentNodeId string id of parent
	 * @return Map of spreadsheet data 
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	private Map processSpreadsheetForDataNode(Map<Long, OutputTreeDataNode> idNodesMap, Map<Long, Map<AttributeInterface, String>> columnMap, SessionDataBean sessionData, String actualParentNodeId) throws DAOException, ClassNotFoundException
	{
		Map spreadSheetDatamap;
		String[] nodeIds;
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		Long id = new Long(nodeIds[0]);
		String parentNodeId = nodeIds[1];
		OutputTreeDataNode parentNode = idNodesMap.get(id);
		if (parentNode.getChildren().isEmpty())
		{
			spreadSheetDatamap = outputSpreadsheetBizLogic.createSpreadsheetData(id,parentNode, columnMap, parentNodeId, sessionData);
		}
		else
		{
			spreadSheetDatamap = outputSpreadsheetBizLogic.updateSpreadsheet(id,parentNode, columnMap, parentNodeId, sessionData);
		}
		return spreadSheetDatamap;
	}

	/**
	 * Processes spreadsheet data for label node which user has clicked.
	 * @param idNodesMap Map<Long, OutputTreeDataNode> map of ids and nodes present in tree
	 * @param columnMap map for column names for attributes for each node in query
	 * @param sessionData session data bean
	 * @param actualParentNodeId string id of parent
	 * @param nodeIds string array of node ids
	 * @return Map of spreadsheet data 
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 */
	private Map processSpreadsheetForLabelNode(Map<Long, OutputTreeDataNode> idNodesMap, Map<Long, Map<AttributeInterface, String>> columnMap, SessionDataBean sessionData, String actualParentNodeId, String[] nodeIds) throws DAOException, ClassNotFoundException
	{
		Map spreadSheetDatamap;
		String idParent = nodeIds[0];
		nodeIds = nodeIds[1].split(Constants.UNDERSCORE);
		Long id = new Long(nodeIds[0]);
		String parentNodeId = "";
		if (idParent.equalsIgnoreCase(Constants.NULL_ID))
		{
			parentNodeId = null;
		}
		else
		{
			parentNodeId = (idParent.split(Constants.UNDERSCORE))[1];
			id = new Long((idParent.split(Constants.UNDERSCORE))[0]);
		}
		nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		Long curentNodeId = new Long(nodeIds[0]);
		OutputTreeDataNode parentNode = idNodesMap.get(id);
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		spreadSheetDatamap = outputSpreadsheetBizLogic.createSpreadsheetData(curentNodeId,parentNode, columnMap, parentNodeId, sessionData);
		return spreadSheetDatamap;
	}
}
