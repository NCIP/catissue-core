
package edu.wustl.catissuecore.action.querysuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CategorySearchForm;
import edu.wustl.catissuecore.bizlogic.querysuite.DefineGridViewBizLogic;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryOutputSpreadsheetBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.querysuite.queryobject.impl.OutputTreeDataNode;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;

/**
 * When user is done with selecting the columns to be shown in results, this action is invoked.
 * This action creates metadata for selected columns and keeps it in session for further reference.
 *  
 * @author deepti_shelar
 *
 */
public class ConfigureGridViewAction extends BaseAction
{
	/**
	 * creates metadata for selected columns and keeps it in session for further reference.
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
		CategorySearchForm categorySearchForm = (CategorySearchForm) form;
		HttpSession session = request.getSession();
		SelectedColumnsMetadata selectedColumnsMetadata = (SelectedColumnsMetadata)session.getAttribute(Constants.SELECTED_COLUMN_META_DATA);
		OutputTreeDataNode currentSelectedObject = selectedColumnsMetadata.getCurrentSelectedObject();
		QuerySessionData querySessionData = (QuerySessionData) session.getAttribute(Constants.QUERY_SESSION_DATA);
		String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		int recordsPerPage = new Integer(recordsPerPageStr);

		Map<Long, OutputTreeDataNode> uniqueIdNodesMap = (Map<Long, OutputTreeDataNode>) session.getAttribute(Constants.ID_NODES_MAP);
		DefineGridViewBizLogic defineGridViewBizLogic = new DefineGridViewBizLogic();
		QueryOutputSpreadsheetBizLogic queryOutputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();

		String sql = querySessionData.getSql();
		SessionDataBean sessionData = getSessionData(request);

		Map spreadSheetDataMap = new HashMap();
		List<String> definedColumnsList = new ArrayList<String>();
		List<NameValueBean> selectedColumnNameValueBeanList = categorySearchForm.getSelectedColumnNameValueBeanList();
		String op = (String) request.getParameter(Constants.OPERATION);
		if (op==null)
		{
			op = Constants.FINISH;
		}
		
		if (op.equalsIgnoreCase(Constants.FINISH))
		{
			selectedColumnsMetadata.setDefinedView(true);
			defineGridViewBizLogic.getSelectedColumnsMetadata(categorySearchForm, uniqueIdNodesMap,selectedColumnsMetadata);
			StringBuffer selectedColumnNames = new StringBuffer();
			definedColumnsList = defineGridViewBizLogic.getSelectedColumnList(categorySearchForm, selectedColumnsMetadata, selectedColumnNames);
			String SqlForSelectedColumns = defineGridViewBizLogic.createSQLForSelectedColumn(selectedColumnNames, sql);
			querySessionData = queryOutputSpreadsheetBizLogic.getQuerySessionData(sessionData, recordsPerPage, 0, spreadSheetDataMap,
					SqlForSelectedColumns);
			selectedColumnNameValueBeanList = categorySearchForm.getSelectedColumnNameValueBeanList();
		}
		else if (op.equalsIgnoreCase(Constants.BACK))
		{
			if(!selectedColumnsMetadata.isDefinedView())
			{
				selectedColumnsMetadata.setDefinedView(false);
			}
			selectedColumnNameValueBeanList = selectedColumnsMetadata.getSelectedColumnNameValueBeanList();
			categorySearchForm.setSelectedColumnNameValueBeanList(selectedColumnNameValueBeanList);
			definedColumnsList = (List<String>) session.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
			querySessionData = queryOutputSpreadsheetBizLogic.getQuerySessionData(sessionData, recordsPerPage, 0, spreadSheetDataMap, sql);
		}
		else if (op.equalsIgnoreCase(Constants.RESTORE))
		{
			selectedColumnsMetadata.setDefinedView(false);
			defineGridViewBizLogic.getColumnsMetadataForSelectedNode(currentSelectedObject,selectedColumnsMetadata);
			StringBuffer selectedColumnNames = new StringBuffer();
			definedColumnsList = defineGridViewBizLogic.getSelectedColumnList(categorySearchForm, selectedColumnsMetadata, selectedColumnNames);
			String SqlForSelectedColumns = defineGridViewBizLogic.createSQLForSelectedColumn(selectedColumnNames, sql);
			querySessionData = queryOutputSpreadsheetBizLogic.getQuerySessionData(sessionData, recordsPerPage, 0, spreadSheetDataMap,
					SqlForSelectedColumns);
			selectedColumnNameValueBeanList = null;
		}
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, definedColumnsList);
		selectedColumnsMetadata.setSelectedColumnNameValueBeanList(selectedColumnNameValueBeanList);
		selectedColumnsMetadata.setCurrentSelectedObject(currentSelectedObject);
		spreadSheetDataMap.put(Constants.QUERY_SESSION_DATA, querySessionData);
		spreadSheetDataMap.put(Constants.SELECTED_COLUMN_META_DATA,selectedColumnsMetadata);
		QueryModuleUtil.setGridData(request, spreadSheetDataMap);
		return mapping.findForward(Constants.SUCCESS);
	}
}
