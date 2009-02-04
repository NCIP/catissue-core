package edu.wustl.catissuecore.action.querysuite;

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
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;

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
		String idOfClickedNode = request.getParameter("nodeId");
		Map spreadSheetDatamap = null;
		QueryOutputSpreadsheetBizLogic outputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		String actualParentNodeId = idOfClickedNode.substring(idOfClickedNode.lastIndexOf(Constants.NODE_SEPARATOR) + 2, idOfClickedNode.length());
		if (idOfClickedNode.endsWith(Constants.LABEL_TREE_NODE))
		{
			spreadSheetDatamap = outputSpreadsheetBizLogic.processSpreadsheetForLabelNode(uniqueIdNodesMap,rootOutputTreeNodeList, columnMap, sessionData, idOfClickedNode);
		}
		else
		{
			spreadSheetDatamap = outputSpreadsheetBizLogic.processSpreadsheetForDataNode(uniqueIdNodesMap, rootOutputTreeNodeList, sessionData, actualParentNodeId);
		}
		setGridData(request, session, spreadSheetDatamap);
		return mapping.findForward(Constants.SUCCESS);
	}
	/**
	 * @param request HTTPRequest
	 * @param session HTTPSession
	 * @param spreadSheetDatamap Map to store spreadshet data
	 */
	private void setGridData(HttpServletRequest request, HttpSession session, Map spreadSheetDatamap)
	{
		int pageNum = Constants.START_PAGE;
		request.setAttribute(Constants.PAGE_NUMBER,Integer.toString(pageNum));
		List<List<String>> dataList = (List<List<String>>) spreadSheetDatamap.get(Constants.SPREADSHEET_DATA_LIST);
		session.setAttribute(Constants.SPREADSHEET_DATA_LIST,dataList);
		session.setAttribute(Constants.PAGINATION_DATA_LIST,dataList);
		List columnsList = (List) spreadSheetDatamap.get(Constants.SPREADSHEET_COLUMN_LIST);
		session.setAttribute(Constants.SPREADSHEET_COLUMN_LIST,columnsList);
		session.setAttribute(Constants.TOTAL_RESULTS,new Integer(dataList.size()).toString());	  
		String pageOf = (String)request.getParameter(Constants.PAGEOF);
		request.setAttribute(Constants.PAGEOF,pageOf);
	}
}
