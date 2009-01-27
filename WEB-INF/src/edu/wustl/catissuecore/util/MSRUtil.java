package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.bizlogic.AssignPrivilegePageBizLogic;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import gov.nih.nci.security.exceptions.CSException;

public class MSRUtil {
	
	/**
	 * Gets lists of sites,users,actions,and roles from database and sets in request.  
	 * @param mapping
	 * @param request
	 * @return ActionForward
	 */
	public ActionForward onFirstTimeLoad(ActionMapping mapping, HttpServletRequest request) throws BizLogicException
	{
			final AssignPrivilegePageBizLogic apBizLogic=getAssignPrivilegePageBizLogic();
			final HttpSession session=request.getSession();
			final String pageOf = (String) request.getParameter(Constants.PAGE_OF);
			List<String[]> list=null;
			List<NameValueBean> actionList=null;
			Map<String, SiteUserRolePrivilegeBean> map = null;
			
			errorsMesssagesForPriv(request);
			
			//if(!(Constants.PAGE_OF_USER).equalsIgnoreCase(pageOf)&&!(Constants.PAGEOF_USER_PROFILE).equals(pageOf))
			if(pageOf!=null && (Constants.PAGEOF_ASSIGN_PRIVILEGE).equalsIgnoreCase(pageOf))
			{
				if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null)
				{
					map = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
				}
				actionList = apBizLogic.getActionList(false);
				final List<NameValueBean> userList=new ArrayList<NameValueBean>();
				
				request.setAttribute(Constants.USERLIST, userList);
			}
			else
			{
				if (session.getAttribute(Constants.USER_ROW_ID_BEAN_MAP) != null)
				{
					map = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.USER_ROW_ID_BEAN_MAP);
				}
				actionList = apBizLogic.getActionListForUserPage(false);
				List<NameValueBean> cpList =null;
				if(request.getAttribute("userForm")==null)
				{
					cpList = apBizLogic.getCPList(false);
				}
				else
				{
					UserForm userForm = null;
					userForm = (UserForm)request.getAttribute("userForm");
					if(userForm.getSiteIds()!=null && (userForm.getSiteIds()).length>0)
					{
						String[] siteIdsArray = null;
						siteIdsArray = userForm.getSiteIds();
						List<Long> selectedSitesList = null;
						selectedSitesList = new ArrayList<Long>(); 
						for(String siteId:siteIdsArray)
						{
							selectedSitesList.add(Long.valueOf(siteId));
						}
						cpList = apBizLogic.getCPsForSelectedSites(selectedSitesList);
					}
					else
					{
						cpList = apBizLogic.getCPList(false);
					}
				}
				Collections.sort(cpList);
				request.setAttribute(Constants.CPLIST, cpList);
			}
			
			final SessionDataBean sessionDataBean = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
			final List<NameValueBean> siteList = apBizLogic.getSiteList(false, sessionDataBean);
			final List roleList = apBizLogic.getRoleList(pageOf);
			
			list = apBizLogic.privilegeDataOnTabSwitch(map,pageOf);
			
			request.setAttribute(Constants.PRIVILEGE_DATA_LIST_ONLOAD, list);
			request.setAttribute(Constants.ACTIONLIST, actionList);
			request.setAttribute(Constants.SITELIST, siteList);
			request.setAttribute(Constants.ROLELIST, roleList);
			
		return mapping.findForward(Constants.SUCCESS);
	}
	
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
		if (Constants.OPERATION_GET_USERS_FORTHIS_SITES.equals(cpOperation)) 
		{
			usersForSelSitesMethod(request, response);
		}
		else if ((Constants.OPERATION_GET_CPS_FORTHIS_SITES).equals(cpOperation)) 
		{
			cpsForSelSitesMethod(request, response);
		}
		else if (Constants.OPERATION_GET_ACTIONS_FORTHIS_ROLE.equals(cpOperation))
		{
			actionsForSelRoleMethod(request, response);
		}
		else if (Constants.OPERATION_ADD_PRIVILEGE.equals(cpOperation))
		{
			addPrivilegeMethod(request, response);
		}
		else if (Constants.OPERATION_DELETE_ROW.equals(cpOperation))
		{
			deleteRowMethod(request, response);
		}
		else if (Constants.OPERATION_EDIT_ROW.equals(cpOperation))
		{
			editRowMethod(request, response);
		}
		else if ((Constants.OPERATION_GET_ACTIONS_FORTHIS_CPS).equals(cpOperation))
		{
			actionsForSelCPsMethod(request, response);	
		}
		else if ((Constants.OPERATION_GET_ACTIONS_FORTHIS_SITES).equals(cpOperation))
		{
			actionsForSelSitesMethod(request, response);
		}
		return null;
	}

	/**
	 * @param request
	 * @param response
	 * @throws BizLogicException
	 * @throws JSONException
	 * @throws IOException
	 */
	private void actionsForSelSitesMethod(HttpServletRequest request, HttpServletResponse response) throws BizLogicException, JSONException, IOException {
		final AssignPrivilegePageBizLogic apBizLogic = getAssignPrivilegePageBizLogic();
		final String selectedRoleId = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
		final boolean isAllCPChecked =Boolean.parseBoolean(request.getParameter(Constants.IS_ALL_CP_CHECKED));
		
		final List<JSONObject> listOfAction = apBizLogic.getActionsForThisSites(selectedRoleId,isAllCPChecked);
		setResponse(response, listOfAction);
	}

	/**
	 * @param request
	 * @param response
	 * @throws BizLogicException
	 * @throws JSONException
	 * @throws IOException
	 */
	private void actionsForSelCPsMethod(HttpServletRequest request, HttpServletResponse response) throws BizLogicException, JSONException, IOException {
		final AssignPrivilegePageBizLogic apBizLogic = getAssignPrivilegePageBizLogic();
		final String selectedRoleId = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
		List<JSONObject> listOfAction=new ArrayList<JSONObject>();
		
		listOfAction = apBizLogic.getActionsForThisCPs(selectedRoleId);
		setResponse(response, listOfAction);
	}

	/**
	 * @param request
	 * @param response
	 * @throws BizLogicException
	 * @throws JSONException
	 * @throws IOException
	 */
	private void editRowMethod(HttpServletRequest request, HttpServletResponse response) throws BizLogicException, JSONException, IOException {
		final HttpSession session = request.getSession();
		final AssignPrivilegePageBizLogic apBizLogic = getAssignPrivilegePageBizLogic();
		final String selectedRow = (String) request.getParameter("selectedRow");
		final String pageOf = (String) request.getParameter(Constants.PAGE_OF);
		
		List<JSONObject> privilegeList = null;
		Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
		if(!(Constants.PAGE_OF_USER).equals(pageOf))
		{
			if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null) 
			{
				rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
			}
		
			privilegeList=apBizLogic.editPrivilege(rowIdBeanMap,selectedRow);
		}
		else
		{
			if (session.getAttribute(Constants.USER_ROW_ID_BEAN_MAP) != null)
			{
				rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.USER_ROW_ID_BEAN_MAP);
			}
			
			privilegeList=apBizLogic.editPrivilegeForUserPage(rowIdBeanMap,selectedRow);
		}
		setResponse(response, privilegeList);
	}

	/**
	 * @param request
	 * @param response
	 * @throws BizLogicException
	 * @throws IOException
	 * @throws JSONException
	 */
	private void deleteRowMethod(HttpServletRequest request, HttpServletResponse response) throws BizLogicException, IOException, JSONException {
		final HttpSession session = request.getSession();
		final AssignPrivilegePageBizLogic apBizLogic = getAssignPrivilegePageBizLogic();
		final String deletedRowsArray = (String) request.getParameter("deletedRowsArray");
		final String operation = (String) request.getParameter(Constants.OPERATION);
		final String pageOf = (String)request.getParameter(Constants.PAGE_OF);
		 String temp="";
		
		Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
		
		//if (!(Constants.PAGE_OF_USER).equals(pageOf)&& (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null))
		if (pageOf!=null && (Constants.PAGEOF_ASSIGN_PRIVILEGE).equals(pageOf)&& (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null))
		{
			rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
			temp=Constants.ROW_ID_OBJECT_BEAN_MAP;
		}
		else if(pageOf!=null && session.getAttribute(Constants.USER_ROW_ID_BEAN_MAP)!=null)
		{
			rowIdBeanMap =(Map<String, SiteUserRolePrivilegeBean>)session.getAttribute(Constants.USER_ROW_ID_BEAN_MAP);
			temp=Constants.USER_ROW_ID_BEAN_MAP;
		}
		
		rowIdBeanMap=apBizLogic.deletePrivilege(rowIdBeanMap,deletedRowsArray,operation);
		session.setAttribute(temp,	rowIdBeanMap);
		
		final List<JSONObject> nullList=null;
		setResponse(response, nullList);
	}

	/**
	 * @param request
	 * @param response
	 * @throws BizLogicException
	 * @throws JSONException
	 * @throws CSException
	 * @throws IOException
	 */
	private void addPrivilegeMethod(HttpServletRequest request, HttpServletResponse response) throws BizLogicException, JSONException, CSException, IOException {
		final HttpSession session = request.getSession();
		final AssignPrivilegePageBizLogic apBizLogic = getAssignPrivilegePageBizLogic();
		final String pageOf = (String) request.getParameter(Constants.PAGE_OF);
		final String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
		final String actionIds = (String) request.getParameter(Constants.SELECTED_ACTION_IDS);
		final String roleId = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
		final String operation = (String) request.getParameter(Constants.OPERATION);
		final String userIds = (String) request.getParameter(Constants.SELECTED_USER_IDS);
		final String cpIds = (String) request.getParameter(Constants.SELECTED_CP_IDS);
		final boolean isAllCPChecked = Boolean.parseBoolean(request.getParameter(Constants.IS_ALL_CP_CHECKED));
		final boolean isCustChecked = Boolean.parseBoolean(request.getParameter("isCustChecked"));
		
		String temp="";
		
		List<JSONObject> listForUPSummary = new ArrayList<JSONObject>();
		
		Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
		
		if (pageOf!=null && ! pageOf.equals("") && (Constants.PAGEOF_ASSIGN_PRIVILEGE).equals(pageOf))
		{
			if(session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null)
			{
				rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
			}
			listForUPSummary =apBizLogic.addPrivilege(rowIdBeanMap,userIds,siteIds,roleId,actionIds,isCustChecked,operation );
			
			temp = Constants.ROW_ID_OBJECT_BEAN_MAP;
		}
		else if(pageOf!=null && ! pageOf.equals(""))
		{
			if(session.getAttribute(Constants.USER_ROW_ID_BEAN_MAP) != null)
			{
				rowIdBeanMap =(Map<String, SiteUserRolePrivilegeBean>)session.getAttribute(Constants.USER_ROW_ID_BEAN_MAP);
			}
			listForUPSummary =apBizLogic.addPrivilegeForUserPage(rowIdBeanMap,cpIds,siteIds,roleId,actionIds,isAllCPChecked,operation );
		
			temp = Constants.USER_ROW_ID_BEAN_MAP;
		}
		session.setAttribute(temp,rowIdBeanMap);
		setResponse(response, listForUPSummary);
	}

	/**
	 * @param request
	 * @param response
	 * @throws BizLogicException
	 * @throws JSONException
	 * @throws IOException
	 */
	private void actionsForSelRoleMethod(HttpServletRequest request, HttpServletResponse response) throws BizLogicException, JSONException, IOException {
		final AssignPrivilegePageBizLogic apBizLogic = getAssignPrivilegePageBizLogic();
		final String role = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
		final String pageOf = (String) request.getParameter(Constants.PAGE_OF);
		final String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
		final String cpIds = (String) request.getParameter(Constants.SELECTED_CP_IDS);
		final boolean isAllCPChecked = Boolean.parseBoolean(request.getParameter(Constants.IS_ALL_CP_CHECKED));
		
		final List<Long> selectedSiteIds = apBizLogic.getInputData(siteIds);
		final List<Long> selectedCPIds = apBizLogic.getInputData(cpIds);
		
 		final List<JSONObject> listOfAction = apBizLogic.getActionsForThisRole(role,pageOf,selectedSiteIds,selectedCPIds,isAllCPChecked);
		setResponse(response, listOfAction);
	}

	/**
	 * @param request
	 * @param response
	 * @throws BizLogicException
	 * @throws JSONException
	 * @throws IOException
	 */
	private void cpsForSelSitesMethod(HttpServletRequest request, HttpServletResponse response) throws BizLogicException, JSONException, IOException {
		final AssignPrivilegePageBizLogic apBizLogic = getAssignPrivilegePageBizLogic();
		final String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
		
		final List<Long> listOfSiteIds = apBizLogic.getInputData(siteIds);
		final List<JSONObject> listOfCPs = apBizLogic.getCPsForThisSites(listOfSiteIds);
		setResponse(response, listOfCPs);
	}

	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws JSONException
	 */
	private void usersForSelSitesMethod(HttpServletRequest request, HttpServletResponse response) throws BizLogicException,IOException,JSONException 
	{
		final AssignPrivilegePageBizLogic apBizLogic = getAssignPrivilegePageBizLogic();
		final String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
		
		final List<Long> listOfSiteIds = apBizLogic.getInputData(siteIds);
		final List<JSONObject> listOfUsers = apBizLogic.getUsersForThisSites(listOfSiteIds);
		setResponse(response, listOfUsers);
	}
	
	public void errorsMesssagesForPriv (HttpServletRequest request)
	{
		final String  errorMessForRole = ApplicationProperties.getValue("errors.assignPrivileges.role");
		final String  errorMessForSite = ApplicationProperties.getValue("errors.assignPrivileges.site");
		final String  errorMessForUser = ApplicationProperties.getValue("errors.assignPrivileges.user");
		final String  errorMessForPriv = ApplicationProperties.getValue("errors.assignPrivileges.privilege");
		final String  errorMessForCP = ApplicationProperties.getValue("errors.assignPrivileges.collectionProtocol");
		
		request.setAttribute(Constants.ERROR_MESSAGE_FOR_ROLE, errorMessForRole);
		request.setAttribute(Constants.ERROR_MESSAGE_FOR_SITE, errorMessForSite);
		request.setAttribute(Constants.ERROR_MESSAGE_FOR_ACTION, errorMessForPriv);
		request.setAttribute(Constants.ERROR_MESSAGE_FOR_CP, errorMessForCP);
		request.setAttribute(Constants.ERROR_MESSAGE_FOR_USER, errorMessForUser);
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
