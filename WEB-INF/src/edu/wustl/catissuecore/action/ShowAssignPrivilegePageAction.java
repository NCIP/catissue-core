package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.corba.se.impl.orbutil.closure.Constant;

import edu.wustl.catissuecore.bizlogic.AssignPrivilegePageBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

/**
 * sets data for site,user,action and role on page loading and handles the ajax requests.
 * @author deepti_shelar
 * @author vipin_bansal
 * 
 */
public class ShowAssignPrivilegePageAction extends BaseAction {
	/**
	 * Overrides the execute method of Action class. Initializes the various
	 * fields in AssignPrivilege.jsp Page.
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {
		Map<String, SiteUserRolePrivilegeBean> rowIdObjectBeanMap = null;
		HttpSession session = request.getSession();
		
		AssignPrivilegePageBizLogic assignPrivilegePageBizLogic = null;

		try {
			assignPrivilegePageBizLogic = (AssignPrivilegePageBizLogic) AbstractBizLogicFactory
			.getBizLogic(ApplicationProperties
					.getValue("app.bizLogicFactory"), "getBizLogic",
					Constants.ASSIGN_PRIVILEGE_FORM_ID);
		} catch (BizLogicException e) {
			Logger.out.error("BizLogicException in getting Bizlogic for AssignPrivilegePageBizLogic in ShowAssignPrivilegePageAction..."+e);
		}

		if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null) {
			rowIdObjectBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute("rowIdObjectBeanMap");
		}else
		{
			rowIdObjectBeanMap = new HashMap<String, SiteUserRolePrivilegeBean>();
		}
		String operation = (String) request.getParameter(Constants.OPERATION);

		if (operation != null) {
			String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
			try {
				if (operation.equals(Constants.OPERATION_GET_USER_FORTHIS_SITES)) 
				{
					List<Integer> listOfSiteIds = assignPrivilegePageBizLogic.getSiteData(siteIds);
					List<JSONObject> listOfUsers = assignPrivilegePageBizLogic.getUsersForThisSites(listOfSiteIds);
					setResponse(response, listOfUsers);
					
				} else if (operation.equals(Constants.OPERATION_GET_ACTION_FORTHIS_ROLE)) {
					String role = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
					List<JSONObject> listOfAction = assignPrivilegePageBizLogic.getActionsForThisRole(role);
					setResponse(response, listOfAction);
					
				} else if (operation.equals(Constants.OPERATION_ADD_PRIVILEDGE)) {
					addPriviledge(request, response, rowIdObjectBeanMap, assignPrivilegePageBizLogic, siteIds);
				} else if (operation.equals(Constants.OPERATION_DELETE_ROW)) {
					String deletedRowsArray = (String) request.getParameter("deletedRowsArray");
					assignPrivilegePageBizLogic.deletePriviledge(deletedRowsArray, rowIdObjectBeanMap);
					List<JSONObject> nullList=null;
					setResponse(response, nullList);
				}
			} catch (JSONException e) {
				Logger.out.error("JSONException in sending JSON response in ShowAssignPrivilegePageAction..."+e);
			}
			return null;
		} else {
			try {
				List<NameValueBean> siteList = assignPrivilegePageBizLogic.getSiteList(false);
				request.setAttribute(Constants.SITELIST, siteList);
				List<NameValueBean> userList = assignPrivilegePageBizLogic.getUserList(false);
				request.setAttribute(Constants.USERLIST, userList);
				List roleList = assignPrivilegePageBizLogic.getRoleList();
				request.setAttribute(Constants.ROLELIST, roleList);
				List actionList = assignPrivilegePageBizLogic.getActionList(false);
				request.setAttribute(Constants.ACTIONLIST, actionList);
			} catch (DAOException exe) {
				Logger.out.error("DAOException in  getting objectList for AssignPrivilegePageBizLogic  in ShowAssignPrivilegePageAction..."+exe);
			} catch (SMException e) {
				Logger.out.error("SMException in  getting objectList for AssignPrivilegePageBizLogic  in ShowAssignPrivilegePageAction..."+e);
			}
			return mapping.findForward(Constants.SUCCESS);
		}

	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param rowIdObjectBeanMap
	 * @param assignPrivilegePageBizLogic
	 * @param siteIds
	 * @throws IOException
	 */
	private void addPriviledge(HttpServletRequest request, HttpServletResponse response, Map<String, SiteUserRolePrivilegeBean> rowIdObjectBeanMap, AssignPrivilegePageBizLogic assignPrivilegePageBizLogic, String siteIds) throws IOException {
		HttpSession session = request.getSession();
		String actionIds = (String) request.getParameter(Constants.SELECTED_ACTION_IDS);
		String userIds = (String) request.getParameter(Constants.SELECTED_USER_IDS);
		List<JSONObject> arrayListForUPSummary = new ArrayList<JSONObject>();
		List<Integer> siteIdsList = new ArrayList<Integer>();
		List<Long> userIdsList = new ArrayList<Long>();
		List<String> actionIdsList = new ArrayList<String>();

		siteIdsList = assignPrivilegePageBizLogic.getSiteData(siteIds);
		userIdsList = assignPrivilegePageBizLogic.getUserData(userIds);
		actionIdsList = assignPrivilegePageBizLogic.getActionData(actionIds);
		String roleId = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);

		try 
		{
			for (int k = 0; k < userIdsList.size(); k++) 
			{
				long userId = userIdsList.get(k);
				List<Site> userRelatedSites = assignPrivilegePageBizLogic.getUserSiteRelation(userId, siteIdsList);

				List objectList = assignPrivilegePageBizLogic.retrieve(User.class.getName(), Constants.ID, userId);
				User user = (User) objectList.get(0);
				SiteUserRolePrivilegeBean siteUserRolePrivilegeBean = assignPrivilegePageBizLogic.setUserPriviledgeSummary(user, userRelatedSites, roleId, actionIdsList);

				String rowId = "" + userId;
				rowIdObjectBeanMap.put(rowId,siteUserRolePrivilegeBean);
				JSONObject jsonObject=assignPrivilegePageBizLogic.getObjectForUPSummary(userRelatedSites,rowId,user,roleId);
				arrayListForUPSummary.add(jsonObject);
			}
			session.setAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP,	rowIdObjectBeanMap);
			setResponse(response, arrayListForUPSummary);
		} catch (JSONException e) {
			Logger.out.error("JSONException in  sending JSON response in ShowAssignPrivilegePageAction..."+e);
		} catch (DAOException e) {
			Logger.out.error("DAOException in  getting objectList for AssignPrivilegePageBizLogic  in ShowAssignPrivilegePageAction..."+e);
		}
}

	/**
	 * 
	 * @param response
	 * @param arrayList
	 * @throws IOException
	 * @throws JSONException
	 */
	private void setResponse(HttpServletResponse response, List arrayList) throws IOException, JSONException {
		response.flushBuffer();
		response.getWriter().write(new JSONObject().put("locations", arrayList).toString());
	}
}
