package edu.wustl.catissuecore.action.querysuite;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.queryobject.IOutputTreeNode;
import edu.wustl.common.querysuite.queryobject.IQuery;

/**
 * This class is called when user clicks on a node of out put tree, it loads the data to add to spreadsheet.
 * @author deepti_shelar
 *
 */
public class BuildQueryOutputSpreadsheetAction extends BaseAction
{
	/**
	 * This method loads the data required for Query Output spreadsheet. 
	 * With the help of QueryOutputSpreadsheetBizLogic it generates a string which will be then passed to client side and spreadsheet
	 * is formed accordingly.String outputTreeStr consists of all nodes with comma seperated string for its id, display name, object name , 
	 * parentId, parent Object name.Such string elements for child nodes are seperated by "|".
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
		Map<Long,IOutputTreeNode> idNodesMap = (Map<Long,IOutputTreeNode>)request.getSession().getAttribute(Constants.ID_NODES_MAP);
		Map<Long, Map<AttributeInterface, String>>  columnMap = (Map<Long, Map<AttributeInterface, String>> )request.getSession().getAttribute(Constants.ID_COLUMNS_MAP);
		SessionDataBean sessionData = getSessionData(request);
		CategorySearchForm actionForm = (CategorySearchForm)form;
		String nodeId = actionForm.getNodeId();		
		String tableName  = Constants.TEMP_OUPUT_TREE_TABLE_NAME + sessionData.getUserId(); 
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		Map spreadSheetDatamap = null;
		if (nodeId.equalsIgnoreCase(Constants.ALL))
		{
			IQuery query = (IQuery)request.getSession().getAttribute(AppletConstants.QUERY_OBJECT);
			IOutputTreeNode defaultRootNode = query.getRootOutputClass();
			spreadSheetDatamap = outputSpreadsheetBizLogic.createSpreadsheetData(tableName,defaultRootNode,columnMap,null,sessionData);
		}
		else
		{
			String actualParentNodeId = nodeId.substring(nodeId.lastIndexOf("::")+2,nodeId.length());
			String[] nodeIds = actualParentNodeId.split(Constants.UNDERSCORE);
			Long id = new Long(nodeIds[0]); 
			String parentNodeId = nodeIds[1];
			IOutputTreeNode parentNode = idNodesMap.get(id);
			spreadSheetDatamap = outputSpreadsheetBizLogic.createSpreadsheetData(tableName,parentNode,columnMap,parentNodeId,sessionData);		
		}
		String outputSpreadsheetDataStr = prepareOutputSpreadsheetDataString(spreadSheetDatamap);
		response.setContentType("text/html");
		response.getWriter().write(outputSpreadsheetDataStr);
		return null;
	}
	/**
	 * Takes data from the map and generates out put data accordingly so that spreadsheet will be updated.
	 * @param spreadSheetDatamap map which holds data for columns and records.
	 * @return this string consists of two strings seperated by '&', first part is for column names to be displayed in spreadsheet 
	 * and the second part is data in the spreadsheet.
	 */
	String prepareOutputSpreadsheetDataString(Map spreadSheetDatamap)
	{
		List<List<String>> dataList = (List<List<String>>)spreadSheetDatamap.get(Constants.SPREADSHEET_DATA_LIST);
		String outputSpreadsheetDataStr = "";
		String dataStr = "";
		for(List<String> row : dataList)
		{
			String rowStr = row.toString(); 
			rowStr = rowStr.replace("[","");
			rowStr = rowStr.replace("]","");
			dataStr = dataStr + "|" + rowStr;
		}
		List columnsList = (List)spreadSheetDatamap.get(Constants.SPREADSHEET_COLUMN_LIST);
		String columns = columnsList.toString();
		columns = columns.replace("[","");
		columns = columns.replace("]","");
		outputSpreadsheetDataStr = columns + "&" +dataStr;
		return outputSpreadsheetDataStr;
	}
}
