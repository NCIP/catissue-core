package krishagni.catissueplus.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import krishagni.catissueplus.beans.LoginAuditBean;
import krishagni.catissueplus.bizlogic.LoginAuditDashboardBizLogic;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.action.CatissueBaseAction;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.exception.DAOException;

public class LoginAuditDashboardAction extends CatissueBaseAction
{
	
	private static final Logger LOGGER = Logger.getCommonLogger(LoginAuditDashboardAction.class);
	
	public ActionForward executeCatissueAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		SessionDataBean sessionDataBean = (SessionDataBean)request.getSession().getAttribute(Constants.SESSION_DATA);
		String target = Constants.FAILURE;
		if(sessionDataBean != null && sessionDataBean.isAdmin())
		{
			
			String operation = request.getParameter("operation");
			String recordPerPage = request.getParameter("recordPerPage");
			String startIndex = request.getParameter("startIndex");
		
			if (operation.equals("gridData")) {
				target = this.gridData(request, response, startIndex, recordPerPage);
	//		} else if (operation.equals("showSQL")) {
	//			target = this.showSQL(request, response);
			} else if (operation.equals("init")) {
				JSONObject gridDataJson = getGridData(startIndex, recordPerPage, null);
				request.setAttribute("recordPerPage", recordPerPage);
				request.setAttribute("gridDataJson", gridDataJson.toString());
				target = Constants.SUCCESS;
			}
		}
		
		return mapping.findForward(target);
	}
	
	private String gridData(HttpServletRequest request, HttpServletResponse response, 
			String startIndex, String recordPerPage) 
			throws NumberFormatException, BizLogicException, DAOException, 
				SQLException, JSONException, IOException
	{
		Map<String, String> filterValueMap = getFilterValueMap(request.getParameter("filterValues"));
		JSONObject dataJson = getGridData(startIndex, recordPerPage, filterValueMap);
		
		response.getWriter().write(dataJson.toString());
		response.flushBuffer();
		return null;
	}
	
//	private ActionForward showSQL(HttpServletRequest request, HttpServletResponse response) 
//			throws DAOException, BizLogicException, IOException
//	{
//		LoginDashboardBizLogic loginDashboardBizLogic = new LoginDashboardBizLogic();
//		Long auditId = Long.parseLong(request.getParameter("auditId")); 
//		String sql = loginDashboardBizLogic.getGeneratedSql(auditId);
//		response.getWriter().write(sql);
//		response.flushBuffer();
//		return null;
//	}
	
	private JSONObject getGridData(String startIndexStr, String totalRecordStr, Map<String, String> filterValueMap) 
			throws BizLogicException, DAOException, SQLException, JSONException 
	{
		int startIndex = Integer.parseInt(startIndexStr);
		int totalRecords = Integer.parseInt(totalRecordStr);
		
		LoginAuditDashboardBizLogic loginDashboardBizLogic = new LoginAuditDashboardBizLogic();
		List<LoginAuditBean> results = loginDashboardBizLogic.getQueryLogResults(startIndex, totalRecords, filterValueMap); 
		
		JSONObject dataJson = new JSONObject();
		if(results.isEmpty()){
			return dataJson;
		}
		
		JSONArray rows = new JSONArray();
		int position = startIndex;
		dataJson.put("pos", startIndex); 
	
		for(LoginAuditBean loginAuditBean : results) {
			
			Map<String, Object> rowContent = new HashMap<String, Object>();			
			rowContent.put("id", position); 
			JSONArray rowData =  new JSONArray(); 
//			rowData.put(loginAuditBean.getQueryId());
			rowData.put(loginAuditBean.getUserName());
			rowData.put(loginAuditBean.getIpAddress());
			rowData.put(loginAuditBean.getLoginTimeStamp());
			rowData.put(loginAuditBean.getLastLoginState());
//			rowData.put("<img src='images/advQuery/sql.png' onclick='showGeneratedSQL("+ loginAuditBean.getIdentifier() +")'/>"); 
			rowContent.put("data", rowData);
			rows.put(rowContent);
			dataJson.put("rows", rows);
			position ++;
		}

		dataJson.put("total_count", totalRecords+1);
		return dataJson;
	}
	
	private Map<String, String> getFilterValueMap(String filterValues) {
		Map<String,String> filterValueMap = new HashMap<String, String>();
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(filterValues);
		
			JSONArray columns = jsonObj.getJSONArray("columns");
			JSONArray values = jsonObj.getJSONArray("values");
	
			for(int i = 0; i < columns.length(); i++) {
				filterValueMap.put(columns.getString(i), values.getString(i));
			}
		} catch (JSONException e) {
			LOGGER.error("Exception: while reading filtered json", e);
		}
		return filterValueMap;
	}
}
