package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.bizlogic.AssignPrivilegePageBizLogic;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import gov.nih.nci.security.exceptions.CSException;

public class MSRUtil {
	
	/**
	 * Gets lists og sites,users,actions,and roles from database and sets in request.  
	 * @param mapping
	 * @param request
	 * @return ActionForward
	 */
	public ActionForward onFirstTimeLoad(ActionMapping mapping, HttpServletRequest request) throws BizLogicException
	{
			final AssignPrivilegePageBizLogic apBizLogic=getAssignPrivilegePageBizLogic();
			HttpSession session=request.getSession();
			final String pageOf = (String) request.getParameter(Constants.PAGE_OF);
			if(!("pageOfUserAdmin").equalsIgnoreCase(pageOf))
			{
				Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
				if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null)
				{
					List<String[]> list;
					list = apBizLogic.privilegeDataOnTabSwitch(rowIdBeanMap,pageOf);
					
					request.setAttribute(Constants.PRIVILEGE_DATA_LIST_ONLOAD, list);
				}
				
				final List<NameValueBean> userList=new ArrayList<NameValueBean>();
			//	final List<NameValueBean> userList = apBizLogic.getUserList(false);
				final List actionList = apBizLogic.getActionList(false);
				
				request.setAttribute(Constants.USERLIST, userList);
				request.setAttribute(Constants.ACTIONLIST, actionList);
			}
			else
			{
				Map<String, SiteUserRolePrivilegeBean> rowIdBeanMapForUserPage = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute("rowIdBeanMapForUserPage");
				if (session.getAttribute("rowIdBeanMapForUserPage") != null)
				{
					List<String[]> list;
					list = apBizLogic.privilegeDataOnTabSwitch(rowIdBeanMapForUserPage,pageOf);
					request.setAttribute(Constants.PRIVILEGE_DATA_LIST_ONLOAD, list);
				}
				final List<NameValueBean> cpList = new ArrayList<NameValueBean>();
			//	final List<NameValueBean> cpList = apBizLogic.getCPList(false);
				final List<NameValueBean> actionList = apBizLogic.getActionListForUserPage(false);
				
				request.setAttribute(Constants.CPLIST, cpList);
				request.setAttribute(Constants.ACTIONLIST, actionList);
			}
		
			final List<NameValueBean> siteList = apBizLogic.getSiteList(false);
			final List roleList = apBizLogic.getRoleList(pageOf);
			request.setAttribute(Constants.SITELIST, siteList);
			request.setAttribute(Constants.ROLELIST, roleList);
			
		return mapping.findForward(Constants.SUCCESS);
	}
	
	/**
	 * Gets lists og sites,users,actions,and roles from database and sets in request.  
	 * @param mapping
	 * @param request
	 * @return ActionForward
	 */
	/*
	public ActionForward onFirstTimeLoadForUser(ActionMapping mapping, HttpServletRequest request) throws BizLogicException
	{
			final AssignPrivilegePageBizLogic apBizLogic=getAssignPrivilegePageBizLogic();
			HttpSession session=request.getSession();
			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
			if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null)
			{
				List<String[]> list;
					list = apBizLogic.privilegeDataOnTabSwitch(rowIdBeanMap);
					request.setAttribute(Constants.PRIVILEGE_DATA_LIST_ONLOAD, list);
			}
		
			final List<NameValueBean> siteList = apBizLogic.getSiteList(false);
			final List<NameValueBean> roleList = apBizLogic.getRoleList();
		
			final List<NameValueBean> cpList = apBizLogic.getCPList(false);
//			final List<NameValueBean> userList = apBizLogic.getUserList(false);
			
			request.setAttribute(Constants.SITELIST, siteList);
			request.setAttribute(Constants.CPLIST, cpList);
			request.setAttribute(Constants.ROLELIST, roleList);
			request.setAttribute(Constants.ACTIONLIST, actionList);
	//		request.setAttribute(Constants.USERLIST, userList);
			
		return mapping.findForward(Constants.SUCCESS);
	}
	
*/	
	/**
	 * Handles Ajax requests 
	 * @param request
	 * @param response
	 * @param operation
	 * @throws IOException 
	 * @throws IOException
	 * @throws CSException 
	 */
	public ActionForward setAJAXResponse(HttpServletRequest request, HttpServletResponse response, String cpOperation) throws IOException, JSONException,BizLogicException, CSException  
	{
		final HttpSession session = request.getSession();
		final AssignPrivilegePageBizLogic apBizLogic=getAssignPrivilegePageBizLogic();
		if (Constants.OPERATION_GET_USER_FORTHIS_SITES.equals(cpOperation)) 
		{
			final String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
			
		    final List<Long> listOfSiteIds = apBizLogic.getSiteData(siteIds);
		    final List<JSONObject> listOfUsers = apBizLogic.getUsersForThisSites(listOfSiteIds);
			setResponse(response, listOfUsers);
			
		}
		else if (("getCPsForThisSites").equals(cpOperation)) 
		{
			final String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
			
		    final List<Long> listOfSiteIds = apBizLogic.getSiteData(siteIds);
		    final List<JSONObject> listOfCPs = apBizLogic.getCPsForThisSites(listOfSiteIds);
			setResponse(response, listOfCPs);
			
		}
		else if (Constants.OPERATION_GET_ACTION_FORTHIS_ROLE.equals(cpOperation))
		{
			final String role = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
			final String pageOf = (String) request.getParameter(Constants.PAGE_OF);
			final String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
			final String cpIds = (String) request.getParameter("selectedCPIds");
			final boolean isAllCPChecked = new Boolean(request.getParameter("isAllCPChecked"));
			
		    final List<Long> selectedSiteIds = apBizLogic.getSiteData(siteIds);
		    final List<Long> selectedCPIds = apBizLogic.getCPData(cpIds);
		    
			final List<JSONObject> listOfAction = apBizLogic.getActionsForThisRole(role,pageOf,selectedSiteIds,selectedCPIds,isAllCPChecked);
			setResponse(response, listOfAction);
			
		}
		else if (Constants.OPERATION_ADD_PRIVILEGE.equals(cpOperation))
		{
			final String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
			final String actionIds = (String) request.getParameter(Constants.SELECTED_ACTION_IDS);
			final String userIds = (String) request.getParameter(Constants.SELECTED_USER_IDS);
 			final String roleId = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
 			final String operation = (String) request.getParameter("operation");
 			
 			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
 			if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null) 
 			{
 				rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
 			}	
 			
			final List<JSONObject> listForUPSummary =apBizLogic.addPrivilege(rowIdBeanMap,userIds,siteIds,roleId,actionIds,operation );
			session.setAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP,	rowIdBeanMap);
			setResponse(response, listForUPSummary);
			
		}
		else if (Constants.OPERATION_DELETE_ROW.equals(cpOperation))
		{
			final String deletedRowsArray = (String) request.getParameter("deletedRowsArray");
			final String operation = (String) request.getParameter("operation");
			final String pageOf = (String)request.getParameter(Constants.PAGE_OF);
			 String temp="";
			
			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
			
			if (!("pageOfUserAdmin").equals(pageOf)&& (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null))
			{
				rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
				temp=Constants.ROW_ID_OBJECT_BEAN_MAP;
			}
			else if(session.getAttribute("rowIdBeanMapForUserPage")!=null)
			{
				rowIdBeanMap =(Map<String, SiteUserRolePrivilegeBean>)session.getAttribute("rowIdBeanMapForUserPage");
				temp="rowIdBeanMapForUserPage";
			}
			
			rowIdBeanMap=apBizLogic.deletePrivilege(rowIdBeanMap,deletedRowsArray,operation);
			session.setAttribute(temp,	rowIdBeanMap);
			
			final List<JSONObject> nullList=null;
			setResponse(response, nullList);
			
		}
		else if (Constants.OPERATION_EDIT_ROW.equals(cpOperation))
		{
			final String selectedRow = (String) request.getParameter("selectedRow");
			
			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
			if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null) {
				rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
			}
			
			final List<JSONObject> privilegeList=apBizLogic.editPrivilege(rowIdBeanMap,selectedRow);
		//	session.setAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP,	rowIdBeanMap);
			
	//		final List<JSONObject> nullList=null;
			setResponse(response, privilegeList);
		}
		else if (("editRowForUserPage").equals(cpOperation))
		{
			final String selectedRow = (String) request.getParameter("selectedRow");
			
			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
			if (session.getAttribute("rowIdBeanMapForUserPage") != null) {
				rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute("rowIdBeanMapForUserPage");
			}
			
			final List<JSONObject> privilegeList=apBizLogic.editPrivilegeForUserPage(rowIdBeanMap,selectedRow);
			setResponse(response, privilegeList);
		}
		
		// for user page---
		
		else if (("addPrivOnUserPage").equals(cpOperation))
		{
			final String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
			final String actionIds = (String) request.getParameter(Constants.SELECTED_ACTION_IDS);
			final String cpIds = (String) request.getParameter("selectedCPIds");
 			final String roleId = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
 			final boolean isAllCPChecked = new Boolean(request.getParameter("isAllCPChecked"));
 			final String operation = (String) request.getParameter("operation");
 			
 			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
 			if (session.getAttribute("rowIdBeanMapForUserPage") != null) 
 			{
 				rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute("rowIdBeanMapForUserPage");
 			}	
 			
			final List<JSONObject> listForUPSummary =apBizLogic.addPrivilegeForUserPage(rowIdBeanMap,cpIds,siteIds,roleId,actionIds,isAllCPChecked,operation );
			session.setAttribute("rowIdBeanMapForUserPage",	rowIdBeanMap);
			setResponse(response, listForUPSummary);
	
		}
		
		else if (("getActionsForThisCPs").equals(cpOperation))
		{
	//		final String cpIds = (String) request.getParameter("selectedCPIds");
	//		final String siteIds = (String) request.getParameter("selectedSiteIds");
			final String selectedRoleId = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
			List<JSONObject> listOfAction=new ArrayList<JSONObject>();
			
			
//			if(cpIds!=null && ! ("").equalsIgnoreCase(cpIds))
//			{
	//			final List<Long> selectedCPIds = apBizLogic.getCPData(cpIds);
				listOfAction = apBizLogic.getActionsForThisCPs(selectedRoleId);
	//		}
//			else
	//		{
	//			 final List<Long> selectedSiteIds = apBizLogic.getSiteData(siteIds);
//				 listOfAction = apBizLogic.getActionsForThisSites(selectedSiteIds,selectedRoleId);
	//		}
			setResponse(response, listOfAction);	
		}
		else if (("getActionsForThisSites").equals(cpOperation))
		{
	//		final String siteIds = (String) request.getParameter("selectedSiteIds");
			final String selectedRoleId = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
			final boolean isAllCPChecked = new Boolean(request.getParameter("isAllCPChecked"));
			
//		    final List<Long> selectedSiteIds = apBizLogic.getSiteData(siteIds);
		    final List<JSONObject> listOfAction = apBizLogic.getActionsForThisSites(selectedRoleId,isAllCPChecked);
	//	    final List<JSONObject> listOfAction = apBizLogic.getActionsForThisRole(roleId, pageOf, selectedSiteIds);
		    setResponse(response, listOfAction);
		}
		return null;
	}
	
	
	
	/**
	 * Sets  response to the Ajax requests.
	 * @param response
	 * @param arrayList
	 * @throws IOException
	 * @throws JSONException
	 */
	public void setResponse(HttpServletResponse response, List arrayList) throws IOException, JSONException 
	{
		response.flushBuffer();
		response.getWriter().write(new JSONObject().put("locations", arrayList).toString());
	}
	/**
	 * Gives Bizlogic for AssignPrivilege
	 * @return AssignPrivilegePageBizLogic
	 */
	public AssignPrivilegePageBizLogic getAssignPrivilegePageBizLogic() throws BizLogicException
	{
		AssignPrivilegePageBizLogic apBizLogic = null;
		apBizLogic = (AssignPrivilegePageBizLogic) AbstractBizLogicFactory
			.getBizLogic(ApplicationProperties.getValue("app.bizLogicFactory"), "getBizLogic",
					Constants.ASSIGN_PRIVILEGE_FORM_ID);
		return apBizLogic;  
	}
}
