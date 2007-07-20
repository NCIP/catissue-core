/*
package edu.wustl.catissuecore.action.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import edu.wustl.common.querysuite.queryengine.impl.SqlGenerator;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.util.dbManager.DAOException;

*//**
 * This class is called when user clicks on a node of out put tree, it updates spreadsheet view for results.
 * @author deepti_shelar
 *//*
public class BuildQueryOutputSpreadsheetAction extends BaseAction
{
	*//**
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
	 *//*
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		HttpSession session = request.getSession();
		Map<String, OutputTreeDataNode> uniqueIdNodesMap = (Map<String, OutputTreeDataNode>) session.getAttribute(Constants.ID_NODES_MAP);
		Map<Long, Map<AttributeInterface, String>> columnMap = (Map<Long, Map<AttributeInterface, String>>) session.getAttribute(
				Constants.ID_COLUMNS_MAP);
		Map<OutputTreeDataNode,Map<Long, Map<AttributeInterface, String>>> outputTreeMap = 
			(Map<OutputTreeDataNode,Map<Long, Map<AttributeInterface, String>>>)session.getAttribute(Constants.OUTPUT_TREE_MAP);
		SessionDataBean sessionData = getSessionData(request);
		CategorySearchForm actionForm = (CategorySearchForm) form;
		String idOfClickedNode = actionForm.getNodeId();
		Map spreadSheetDatamap = null;
		String actualParentNodeId = idOfClickedNode.substring(idOfClickedNode.lastIndexOf(Constants.NODE_SEPARATOR) + 2, idOfClickedNode.length());
		if (idOfClickedNode.endsWith(Constants.LABEL_TREE_NODE))
		{
			
			spreadSheetDatamap = processSpreadsheetForLabelNode(uniqueIdNodesMap,outputTreeMap, columnMap, sessionData, idOfClickedNode);
		}
		else
		{
			spreadSheetDatamap = processSpreadsheetForDataNode(uniqueIdNodesMap, outputTreeMap, sessionData, actualParentNodeId);
		}
		String outputSpreadsheetDataStr = QueryModuleUtil.prepareOutputSpreadsheetDataString(spreadSheetDatamap);
		response.setContentType("text/html");
		response.getWriter().write(outputSpreadsheetDataStr);
		return null;
	}

	*//**
	 * Processes spreadsheet data for data node which user has clicked.
	 * @param idNodesMap Map<Long, OutputTreeDataNode> map of ids and nodes present in tree
	 * @param columnMap map for column names for attributes for each node in query
	 * @param sessionData session data bean
	 * @param actualParentNodeId string id of parent
	 * @return Map of spreadsheet data 
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 *//*
	public Map processSpreadsheetForDataNode(Map<String, OutputTreeDataNode> idNodesMap,
			Map<OutputTreeDataNode,Map<Long, Map<AttributeInterface, String>>> outputTreeMap, SessionDataBean sessionData, String actualParentNodeId)
	throws DAOException, ClassNotFoundException
	{
		Map spreadSheetDatamap = null;
		String[] nodeIds;
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
		String treeNo = nodeIds[0];
		String parentId = nodeIds[1];
		String parentData = nodeIds[2];
		String uniqueNodeId = treeNo+"_"+parentId;
		OutputTreeDataNode parentNode = idNodesMap.get(uniqueNodeId);
		if (parentNode.getChildren().isEmpty())
		{
			spreadSheetDatamap = outputSpreadsheetBizLogic.createSpreadsheetData(treeNo,parentNode, outputTreeMap, sessionData,parentData);
		}
		else
		{
			spreadSheetDatamap = outputSpreadsheetBizLogic.updateSpreadsheet(treeNo,parentNode, outputTreeMap, sessionData,parentData);
		}
		return spreadSheetDatamap;
	}

	*//**
	 * Processes spreadsheet data for label node which user has clicked.
	 * @param idNodesMap Map<Long, OutputTreeDataNode> map of ids and nodes present in tree
	 * @param columnMap map for column names for attributes for each node in query
	 * @param sessionData session data bean
	 * @param actualParentNodeId string id of parent
	 * @param nodeIds string array of node ids
	 * @return Map of spreadsheet data 
	 * @throws DAOException DAOException
	 * @throws ClassNotFoundException ClassNotFoundException
	 *//*
	public Map processSpreadsheetForLabelNode(Map<String, OutputTreeDataNode> idNodesMap,Map<OutputTreeDataNode,Map<Long, Map<AttributeInterface, String>>> outputTreeMap, Map<Long, Map<AttributeInterface, String>> columnMap, SessionDataBean sessionData, String idOfClickedNode) throws DAOException, ClassNotFoundException
	{
		Map spreadSheetDataMap = new HashMap();
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		String tableName = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId();
		String[] nodeIds = idOfClickedNode.split(Constants.NODE_SEPARATOR);
		String parentNode = nodeIds[0];//data
		String[] spiltParentNodeId = parentNode.split(Constants.UNDERSCORE);
		String treeNo = spiltParentNodeId[0];
		String parentNodeId = spiltParentNodeId[1];
		if(parentNode.contains("NULL"))
		{
			OutputTreeDataNode root = QueryModuleUtil.getNodeForTree(outputTreeMap,treeNo);
			spreadSheetDataMap = outputSpreadsheetBizLogic.createSpreadsheetData(treeNo,root, outputTreeMap, sessionData,null);
		} else
		{
			String parentData = spiltParentNodeId[2];
			String uniqueParentNodeId = treeNo+"_"+parentNodeId;
			OutputTreeDataNode parentTreeNode = idNodesMap.get(uniqueParentNodeId);
			OutputTreeDataNode root = QueryModuleUtil.getNodeForTree(outputTreeMap,treeNo);
			Map<AttributeInterface, String> columnsMap = outputTreeMap.get(root).get(parentTreeNode.getId());
			String parentIdColumnName = "";
			Set<AttributeInterface> setForParent = columnsMap.keySet();
			for (Iterator<AttributeInterface> iterator = setForParent.iterator(); iterator.hasNext();)
			{
				AttributeInterface attr = iterator.next();
				if (attr.getName().equalsIgnoreCase(Constants.ID))
				{
					parentIdColumnName = columnsMap.get(attr);
					break;
				}
			}
			String currentNode = nodeIds[1];//label
			String[] spiltCurrentNodeId = currentNode.split(Constants.UNDERSCORE);
			String currentNodeId = spiltCurrentNodeId[1];
			String uniqueCurrentNodeId = treeNo+"_"+currentNodeId;
			OutputTreeDataNode currentTreeNode = idNodesMap.get(uniqueCurrentNodeId);
			columnsMap = outputTreeMap.get(root).get(currentTreeNode.getId());
			String selectSql = createSQL(spreadSheetDataMap, columnsMap);
			selectSql = selectSql.substring(0, selectSql.lastIndexOf(","));
			selectSql = selectSql + " from " + tableName;
			if (parentData != null)
			{
				selectSql = selectSql + " where " + parentIdColumnName + " = '" + parentData + "'";
			}
			List spreadsheetDataList = QueryModuleUtil.executeQuery(selectSql, sessionData);
			spreadSheetDataMap.put(Constants.SPREADSHEET_DATA_LIST, spreadsheetDataList);
		}
		return spreadSheetDataMap;
	}

	*//**
	 * @param spreadSheetDataMap
	 * @param columnsMap
	 * @return
	 *//*
	private String createSQL(Map spreadSheetDataMap, Map<AttributeInterface, String> columnsMap)
	{
		Set<AttributeInterface> set = columnsMap.keySet();
		String selectSql = "select distinct ";
		List<String> columnsList = new ArrayList<String>();
		columnsList.add("");
		for (Iterator<AttributeInterface> iterator = set.iterator(); iterator.hasNext();)
		{
			AttributeInterface attribute = iterator.next();
			String className = attribute.getEntity().getName();
			className = className.substring(className.lastIndexOf('.') + 1, className.length());
			String sqlColumnName = columnsMap.get(attribute);
			selectSql = selectSql + sqlColumnName + ",";
			sqlColumnName = sqlColumnName.substring(SqlGenerator.COLUMN_NAME.length(), sqlColumnName.length());
			String attrLabel = QueryModuleUtil.getAttributeLabel(attribute.getName());
			columnsList.add(attrLabel + " : " + className);
		}
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, columnsList);
		return selectSql;
	}

}
*/