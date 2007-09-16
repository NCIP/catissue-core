
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
		OutputTreeDataNode currentSelectedObject = (OutputTreeDataNode) session.getAttribute(Constants.CURRENT_SELECTED_OBJECT);
		QuerySessionData querySessionData = (QuerySessionData) session.getAttribute(Constants.QUERY_SESSION_DATA);
		String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		int recordsPerPage = new Integer(recordsPerPageStr);

		Map<Long, OutputTreeDataNode> uniqueIdNodesMap = (Map<Long, OutputTreeDataNode>) session.getAttribute(Constants.ID_NODES_MAP);
		DefineGridViewBizLogic defineGridViewBizLogic = new DefineGridViewBizLogic();
		QueryOutputSpreadsheetBizLogic queryOutputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();

		String sql = querySessionData.getSql();
		SessionDataBean sessionData = getSessionData(request);

		Map spreadSheetDataMap = new HashMap();
		SelectedColumnsMetadata selectedColumnsMetadata = null;
		List<String> definedColumnsList = new ArrayList<String>();
		List<NameValueBean> selectedColumnNameValueBeanList = categorySearchForm.getSelectedColumnNameValueBeanList();
		String op = (String) request.getParameter(Constants.OPERATION);
		if (op==null)
		{
			op = Constants.FINISH;
		}
		
		if (op.equalsIgnoreCase(Constants.FINISH))
		{
			selectedColumnsMetadata = defineGridViewBizLogic.getSelectedColumnsMetadata(categorySearchForm, uniqueIdNodesMap);
			StringBuffer selectedColumnNames = new StringBuffer();
			definedColumnsList = defineGridViewBizLogic.getSelectedColumnList(categorySearchForm, selectedColumnsMetadata, selectedColumnNames);
			String SqlForSelectedColumns = defineGridViewBizLogic.createSQLForSelectedColumn(selectedColumnNames, sql);
			querySessionData = queryOutputSpreadsheetBizLogic.getQuerySessionData(sessionData, recordsPerPage, 0, spreadSheetDataMap,
					SqlForSelectedColumns);
			selectedColumnNameValueBeanList = categorySearchForm.getSelectedColumnNameValueBeanList();
		}
		else if (op.equalsIgnoreCase(Constants.BACK))
		{
			selectedColumnsMetadata = (SelectedColumnsMetadata) session.getAttribute(Constants.SELECTED_COLUMN_META_DATA);
			selectedColumnNameValueBeanList = (List<NameValueBean>) session.getAttribute(Constants.SELECTED_COLUMN_NAME_VALUE_BEAN_LIST);
			categorySearchForm.setSelectedColumnNameValueBeanList(selectedColumnNameValueBeanList);
			definedColumnsList = (List<String>) session.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);
			querySessionData = queryOutputSpreadsheetBizLogic.getQuerySessionData(sessionData, recordsPerPage, 0, spreadSheetDataMap, sql);
		}
		else if (op.equalsIgnoreCase(Constants.RESTORE))
		{
			selectedColumnsMetadata = defineGridViewBizLogic.getColumnsMetadataForSelectedNode(currentSelectedObject);
			StringBuffer selectedColumnNames = new StringBuffer();
			definedColumnsList = defineGridViewBizLogic.getSelectedColumnList(categorySearchForm, selectedColumnsMetadata, selectedColumnNames);
			String SqlForSelectedColumns = defineGridViewBizLogic.createSQLForSelectedColumn(selectedColumnNames, sql);
			querySessionData = queryOutputSpreadsheetBizLogic.getQuerySessionData(sessionData, recordsPerPage, 0, spreadSheetDataMap,
					SqlForSelectedColumns);
			selectedColumnNameValueBeanList = null;
			selectedColumnsMetadata = null;
		}
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST, definedColumnsList);
		session.setAttribute(Constants.SELECTED_COLUMN_NAME_VALUE_BEAN_LIST, selectedColumnNameValueBeanList);
		//querySessionData.setSql(sql);

		spreadSheetDataMap.put(Constants.CURRENT_SELECTED_OBJECT, currentSelectedObject);
		spreadSheetDataMap.put(Constants.QUERY_SESSION_DATA, querySessionData);
		QueryModuleUtil.setGridData(request, spreadSheetDataMap);
		session.setAttribute(Constants.SELECTED_COLUMN_META_DATA, selectedColumnsMetadata);
		return mapping.findForward(Constants.SUCCESS);
	}
}
