package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.catissuecore.actionForm.CollectionProtocolForm;
import edu.wustl.catissuecore.bizlogic.AssignPrivilegePageBizLogic;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.listener.CatissueCoreServletContextListener;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractBizLogicFactory;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.exceptions.CSException;

/**
 * Sets data for site,user,action and role on page loading and handles the ajax requests.
 * @author vipin_bansal
 */
public class ShowAssignPrivilegePageAction extends BaseAction 
{
	/**
	 * Overrides the execute method of Action class. Initializes the various
	 * fields in AssignPrivilege.jsp Page.
	 */
	private static org.apache.log4j.Logger logger =Logger.getLogger(CatissueCoreServletContextListener.class);  
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception 
	{ 
		ActionForward findForward = null;
		final CollectionProtocolForm  cpForm = (CollectionProtocolForm)form;
		final String cpOperation = (String) request.getParameter("cpOperation");
		request.setAttribute("cpOperation", cpOperation);
		if (("AssignPrivilegePage".equals(cpOperation))) 
		{
			findForward = onFirstTimeLoad(mapping, request);
		}
		else 
		{
				findForward = setAJAXResponse(request, response, cpOperation);
				
		}
		request.setAttribute("CollectionProtocolForm", cpForm);
		request.setAttribute("noOfConsents", cpForm.getConsentTierCounter());
		return findForward;
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
	/**
	 * Gets lists og sites,users,actions,and roles from database and sets in request.  
	 * @param mapping
	 * @param request
	 * @return ActionForward
	 */
	private ActionForward onFirstTimeLoad(ActionMapping mapping, HttpServletRequest request) throws BizLogicException
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
			request.setAttribute(Constants.SITELIST, siteList);
	//		final List<NameValueBean> userList = apBizLogic.getUserList(false);
	//		request.setAttribute(Constants.USERLIST, userList);
			final List roleList = apBizLogic.getRoleList();
			request.setAttribute(Constants.ROLELIST, roleList);
			final List actionList = apBizLogic.getActionList(false);
			request.setAttribute(Constants.ACTIONLIST, actionList);
			
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
	private ActionForward setAJAXResponse(HttpServletRequest request, HttpServletResponse response, String cpOperation) throws IOException, JSONException,BizLogicException, CSException  
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
		else if (Constants.OPERATION_GET_ACTION_FORTHIS_ROLE.equals(cpOperation))
		{
			final String role = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
			
			final List<JSONObject> listOfAction = apBizLogic.getActionsForThisRole(role);
			setResponse(response, listOfAction);
			
		}
		else if (Constants.OPERATION_ADD_PRIVILEGE.equals(cpOperation))
		{
			final String siteIds = (String) request.getParameter(Constants.SELECTED_SITE_IDS);
			final String actionIds = (String) request.getParameter(Constants.SELECTED_ACTION_IDS);
			final String userIds = (String) request.getParameter(Constants.SELECTED_USER_IDS);
 			final String roleId = (String) request.getParameter(Constants.SELECTED_ROLE_IDS);
 			
 			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
 			if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null) 
 			{
 				rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
 			}	
 			
			final List<JSONObject> listForUPSummary =apBizLogic.addPrivilege(rowIdBeanMap,userIds,siteIds,roleId,actionIds );
			session.setAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP,	rowIdBeanMap);
			setResponse(response, listForUPSummary);
			
		}
		else if (Constants.OPERATION_DELETE_ROW.equals(cpOperation))
		{
			final String deletedRowsArray = (String) request.getParameter("deletedRowsArray");
			
			Map<String, SiteUserRolePrivilegeBean> rowIdBeanMap= new HashMap<String, SiteUserRolePrivilegeBean>();
			if (session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP) != null)
			{
				rowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session.getAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP);
			}
			
			rowIdBeanMap=apBizLogic.deletePrivilege(rowIdBeanMap,deletedRowsArray);
			session.setAttribute(Constants.ROW_ID_OBJECT_BEAN_MAP,	rowIdBeanMap);
			
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
		return null;
	}
	/**
	 * Sets  response to the Ajax requests.
	 * @param response
	 * @param arrayList
	 * @throws IOException
	 * @throws JSONException
	 */
	private void setResponse(HttpServletResponse response, List arrayList) throws IOException, JSONException 
	{
		response.flushBuffer();
		response.getWriter().write(new JSONObject().put("locations", arrayList).toString());
	}
}
