package edu.wustl.catissuecore.action.querysuite;

import java.util.HashMap;
import java.util.LinkedHashMap;
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

/**
 * When user is donw with selecting the columns to be shown in results, this action is invoked.
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
		CategorySearchForm categorySearchForm = (CategorySearchForm)form;
		HttpSession session  = request.getSession();
		QuerySessionData querySessionData = (QuerySessionData) session.getAttribute(Constants.QUERY_SESSION_DATA);
		String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		Map<Long,OutputTreeDataNode> uniqueIdNodesMap = (Map<Long,OutputTreeDataNode>) session.getAttribute(Constants.ID_NODES_MAP);
		DefineGridViewBizLogic defineGridViewBizLogic = new DefineGridViewBizLogic();
		StringBuffer selectedColumnNames = new StringBuffer();
		Map<String, String> selectedColumnMetaData = new LinkedHashMap<String, String>();
		List<String> definedColumnsList = defineGridViewBizLogic.getSelectedColumns(categorySearchForm, uniqueIdNodesMap, selectedColumnNames, selectedColumnMetaData);
		List<NameValueBean> selectedColumnNameValueBeanList = categorySearchForm.getSelectedColumnNameValueBeanList();
		session.setAttribute(Constants.SELECTED_COLUMN_NAME_VALUE_BEAN_LIST,selectedColumnNameValueBeanList);
		String sql = querySessionData.getSql();
		String SqlForSelectedColumns = defineGridViewBizLogic.createSQLForSelectedColumn(selectedColumnNames, sql);
		SessionDataBean sessionData = getSessionData(request);
		int recordsPerPage = new Integer(recordsPerPageStr);
		QueryOutputSpreadsheetBizLogic queryOutputSpreadsheetBizLogic = new QueryOutputSpreadsheetBizLogic();
		Map spreadSheetDataMap = new HashMap();
		querySessionData = queryOutputSpreadsheetBizLogic.getQuerySessionData(sessionData, recordsPerPage,0, spreadSheetDataMap, SqlForSelectedColumns);
		querySessionData.setSql(sql);
		OutputTreeDataNode currentSelectedObject = (OutputTreeDataNode)session.getAttribute(Constants.CURRENT_SELECTED_OBJECT);
		spreadSheetDataMap.put(Constants.CURRENT_SELECTED_OBJECT,currentSelectedObject);
		spreadSheetDataMap.put(Constants.QUERY_SESSION_DATA,querySessionData);
		spreadSheetDataMap.put(Constants.SPREADSHEET_COLUMN_LIST,definedColumnsList);
		QueryModuleUtil.setGridData(request, spreadSheetDataMap);
		session.setAttribute(Constants.SELECTED_COLUMN_META_DATA, selectedColumnMetaData);
		return mapping.findForward(Constants.SUCCESS);
	}

	
	
}
