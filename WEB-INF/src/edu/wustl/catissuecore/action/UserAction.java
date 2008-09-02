/**
 * <p>Title: UserAction Class>
 * <p>Description:	This class initializes the fields in the User Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 22, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.CaTissuePrivilegeUtility;
import edu.wustl.catissuecore.util.MSRUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.security.PrivilegeCache;
import edu.wustl.common.security.PrivilegeManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 * 
 * @author gautam_shetty
 */
public class UserAction extends SecureAction {

	/**
	 * Overrides the execute method of Action class. Sets the various fields in
	 * User Add/Edit webpage.
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		String pageOf = (String) request.getParameter(Constants.PAGEOF);
		String reqPath = (String) request.getParameter(Constants.REQ_PATH);
		String submittedFor = (String) request
				.getAttribute(Constants.SUBMITTED_FOR);
		String openInCPFrame = (String) request
				.getParameter(Constants.OPEN_PAGE_IN_CPFRAME);
		UserForm userForm = (UserForm) form;

		// method to get myProfile-Add Privilege
		SessionDataBean sessionDataBean=getSessionData(request);
	//	String readOnlyForPrivOnEdit = "";
	//	String disablePrivButton = "false";
		long loggedInUserId = 0;
		if((Constants.PAGE_OF_USER).equals(pageOf)&& sessionDataBean.getUserId()!=null)
		{
			loggedInUserId =sessionDataBean.getUserId();
		}
		
		if((Constants.PAGE_OF_USER).equals(pageOf)&& sessionDataBean!=null && loggedInUserId == userForm.getId())
		{
			pageOf = Constants.PAGEOF_USER_PROFILE;
//			readOnlyForPrivOnEdit = "disabled='true'";
//			disablePrivButton ="true";
//			request.setAttribute("readOnlyForPrivOnEdit", readOnlyForPrivOnEdit);
//			request.setAttribute("disablePrivButton", disablePrivButton);
			
		}
		// method to get myProfile end here
		
		//method to preserve data on validation
		MSRUtil msrUtil = new MSRUtil();
		if (operation.equalsIgnoreCase(Constants.ADD)) {
			HttpSession session = request.getSession();
			boolean dirtyVar = false;
			dirtyVar = new Boolean(request.getParameter("dirtyVar"));
			if (!dirtyVar) {
				session.removeAttribute(Constants.USER_ROW_ID_BEAN_MAP);
			}
		}
		//method to preserve data on validation end here

		String formName, prevPage = null, nextPage = null;
		boolean roleStatus = false;
		if (pageOf.equals(Constants.PAGEOF_APPROVE_USER)) {
			Long identifier = (Long) request
					.getAttribute(Constants.PREVIOUS_PAGE);
			request.setAttribute("prevPageId", identifier);
			prevPage = Constants.USER_DETAILS_SHOW_ACTION + "?"
					+ Constants.SYSTEM_IDENTIFIER + "=" + identifier;
			identifier = (Long) request.getAttribute(Constants.NEXT_PAGE);
			nextPage = Constants.USER_DETAILS_SHOW_ACTION + "?"
					+ Constants.SYSTEM_IDENTIFIER + "=" + identifier;
			request.setAttribute("nextPageId", identifier);

		}
		if (!pageOf.equals(Constants.PAGEOF_APPROVE_USER)) {
			if (operation.equals(Constants.EDIT)
					&& (userForm.getCsmUserId() != null)) {
				if (userForm.getCsmUserId().longValue() == 0) {
					UserBizLogic bizLogic = (UserBizLogic) BizLogicFactory
							.getInstance().getBizLogic(Constants.USER_FORM_ID);
					String sourceObjName = User.class.getName();
					String[] selectColName = { "csmUserId" };
					String[] whereColName = { "id" };
					String[] whereColCond = { "=" };
					Object[] whereColVal = { userForm.getId() };

					List regList = bizLogic.retrieve(sourceObjName,
							selectColName, whereColName, whereColCond,
							whereColVal, Constants.AND_JOIN_CONDITION);
					if (regList != null && !regList.isEmpty()) {
						Object obj = (Object) regList.get(0);
						Long id = (Long) obj;
						userForm.setCsmUserId(id);
					}
				}
			}
		}
		if (operation.equals(Constants.EDIT)) {
			if (!pageOf.equals(Constants.PAGEOF_APPROVE_USER)) {
				setUserPrivileges(request.getSession(), userForm.getId());
			}

			if (pageOf.equals(Constants.PAGEOF_APPROVE_USER)) {
				formName = Constants.APPROVE_USER_EDIT_ACTION;
			} else if (pageOf.equals(Constants.PAGEOF_USER_PROFILE)) {
				formName = Constants.USER_EDIT_PROFILE_ACTION;
			} else {
				formName = Constants.USER_EDIT_ACTION;
			}
		} else {
			if (pageOf.equals(Constants.PAGEOF_APPROVE_USER)) {
				formName = Constants.APPROVE_USER_ADD_ACTION;
			} else {
				formName = Constants.USER_ADD_ACTION;
				if (pageOf.equals(Constants.PAGEOF_SIGNUP)) {
					formName = Constants.SIGNUP_USER_ADD_ACTION;
				}
			}
		}
		if (pageOf.equals(Constants.PAGEOF_APPROVE_USER)
				&& (userForm.getStatus().equals(
						Constants.APPROVE_USER_PENDING_STATUS)
						|| userForm.getStatus().equals(
								Constants.APPROVE_USER_REJECT_STATUS) || userForm
						.getStatus().equals(Constants.SELECT_OPTION))) {
			roleStatus = true;
			if (userForm.getStatus().equals(
					Constants.APPROVE_USER_PENDING_STATUS)) {
				operation = Constants.EDIT;
			}
		}
		if (pageOf.equals(Constants.PAGEOF_USER_PROFILE)) {
			roleStatus = true;
		}
		if (operation.equalsIgnoreCase(Constants.ADD)) {
			// request.getSession(true).setAttribute(Constants.USER_ROW_ID_BEAN_MAP,
			// null);

			if (userForm.getCountry() == null) {
				userForm.setCountry((String) DefaultValueManager
						.getDefaultValue(Constants.DEFAULT_COUNTRY));
			}
		}
		if (pageOf.equals(Constants.PAGEOF_SIGNUP)) {
			userForm.setStatus(Constants.ACTIVITY_STATUS_NEW);
			userForm.setActivityStatus(Constants.ACTIVITY_STATUS_NEW);
		}
		userForm.setOperation(operation);
		userForm.setPageOf(pageOf);
		userForm.setSubmittedFor(submittedFor);
		userForm.setRedirectTo(reqPath);

		String roleStatusforJSP = roleStatus + "";

		request.setAttribute("roleStatus", roleStatusforJSP);
		request.setAttribute("formName", formName);
		request.setAttribute("prevPageURL", prevPage);
		request.setAttribute("nextPageURL", nextPage);

		// Sets the countryList attribute to be used in the Add/Edit User Page.
		List countryList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COUNTRY_LIST, null);
		request.setAttribute("countryList", countryList);

		// Sets the stateList attribute to be used in the Add/Edit User Page.
		List stateList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_STATE_LIST, null);
		request.setAttribute("stateList", stateList);

		// Sets the pageOf attribute (for Add,Edit or Query Interface).
		String target = pageOf;
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(
				Constants.USER_FORM_ID);

		// Sets the instituteList attribute to be used in the Add/Edit User
		// Page.
		String sourceObjectName = Institution.class.getName();
		String[] displayNameFields = { Constants.NAME };
		String valueField = Constants.SYSTEM_IDENTIFIER;

		List instituteList = bizLogic.getList(sourceObjectName,
				displayNameFields, valueField, false);
		request.setAttribute("instituteList", instituteList);

		// Sets the departmentList attribute to be used in the Add/Edit User
		// Page.
		sourceObjectName = Department.class.getName();
		List departmentList = bizLogic.getList(sourceObjectName,
				displayNameFields, valueField, false);
		request.setAttribute("departmentList", departmentList);

		// Sets the cancerResearchGroupList attribute to be used in the Add/Edit
		// User Page.
		sourceObjectName = CancerResearchGroup.class.getName();
		List cancerResearchGroupList = bizLogic.getList(sourceObjectName,
				displayNameFields, valueField, false);
		request
				.setAttribute("cancerResearchGroupList",
						cancerResearchGroupList);

		// Populate the activity status dropdown if the operation is edit
		// and the user page is of administrative tab.
		if (operation.equals(Constants.EDIT)
				&& pageOf.equals(Constants.PAGEOF_USER_ADMIN)) {
			String activityStatusList = Constants.ACTIVITYSTATUSLIST;
			request.setAttribute("activityStatusList",
					Constants.USER_ACTIVITY_STATUS_VALUES);
		}

		// Populate the role dropdown if the page is of approve user or
		// (Add/Edit) user page of adminitraive tab.
		// if (pageOf.equals(Constants.PAGEOF_APPROVE_USER) ||
		// pageOf.equals(Constants.PAGEOF_USER_ADMIN)
		// ||pageOf.equals(Constants.PAGEOF_USER_PROFILE ))
		// {
		// List roleNameValueBeanList = getRoles();
		// request.setAttribute("roleList", roleNameValueBeanList);
		// }

		// Populate the status dropdown for approve user
		// page.(Approve,Reject,Pending)
		if (pageOf.equals(Constants.PAGEOF_APPROVE_USER)) {
			request.setAttribute("statusList",
					Constants.APPROVE_USER_STATUS_VALUES);
		}

		Logger.out.debug("pageOf :---------- " + pageOf);

		// For Privilege
		String roleId = userForm.getRole();
		boolean flagForSARole = false;
		if((Constants.SUPER_ADMIN_USER).equals(roleId))
		{
			flagForSARole = true;
		}
		request.setAttribute("flagForSARole", flagForSARole);

		msrUtil.onFirstTimeLoad(mapping, request);

		final String cpOperation = (String) request.getParameter("cpOperation");
		if (cpOperation != null) {
			return msrUtil.setAJAXResponse(request, response, cpOperation);
		}
		// Parameters for JSP

		int SELECT_OPTION_VALUE = Constants.SELECT_OPTION_VALUE;
		boolean readOnlyEmail = false;
		if (operation.equals(Constants.EDIT)
				&& pageOf.equals(Constants.PAGEOF_USER_PROFILE)) {
			readOnlyEmail = true;
		}
		request.setAttribute("SELECT_OPTION_VALUE", SELECT_OPTION_VALUE);
		request.setAttribute("Approve", Constants.APPROVE_USER_APPROVE_STATUS);
		request
				.setAttribute("pageOfApproveUser",
						Constants.PAGEOF_APPROVE_USER);
		request.setAttribute("backPage", Constants.APPROVE_USER_SHOW_ACTION
				+ "?" + Constants.PAGE_NUMBER + "=" + Constants.START_PAGE);
		request.setAttribute("redirectTo", Constants.REQ_PATH);
		request.setAttribute("addforJSP", Constants.ADD);
		request.setAttribute("editforJSP", Constants.EDIT);
		request.setAttribute("searchforJSP", Constants.SEARCH);
		request.setAttribute("readOnlyEmail", readOnlyEmail);
		request
				.setAttribute("pageOfUserProfile",
						Constants.PAGEOF_USER_PROFILE);
		request.setAttribute("pageOfUserAdmin", Constants.PAGEOF_USER_ADMIN);
		request.setAttribute("pageOfSignUp", Constants.PAGEOF_SIGNUP);
		request.setAttribute("pageOf", pageOf);
		request.setAttribute("operation", operation);
		request.setAttribute("openInCPFrame", openInCPFrame);
		// ------------- add new
	      Logger.out.debug("USerAction redirect :---------- "+ reqPath  );
        if(openInCPFrame != null && Constants.TRUE.equalsIgnoreCase(openInCPFrame))
        	target=Constants.OPEN_PAGE_IN_CPFRAME;
        return mapping.findForward(target);
    }
    
  
    private void setUserPrivileges(HttpSession session, long id)
	{
    	if (id == 0)
    	{
    		return;
    	}
    	try
    	{
	    	IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
	    	User user = (User)bizLogic.retrieve(User.class.getName(), id);
            String role = user.getRoleId();
            if (role != null && !role.equalsIgnoreCase(Constants.ADMIN_USER))
            {
                PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
                // privilegeManager.removePrivilegeCache(user.getLoginName());
                PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user.getLoginName());
                privilegeCache.refresh();
                Map<String, SiteUserRolePrivilegeBean> privilegeMap = CaTissuePrivilegeUtility.getAllPrivileges(privilegeCache);
                session.setAttribute(Constants.USER_ROW_ID_BEAN_MAP, privilegeMap);
            }
    	}
    	catch(DAOException e)
    	{
    		e.printStackTrace();
    	}
    	catch (Exception e) 
    	{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.action.SecureAction#isAuthorizedToExecute(javax.servlet.http.HttpServletRequest)
	 */
	protected boolean isAuthorizedToExecute(HttpServletRequest request)
			throws Exception {
		String pageOf = request.getParameter(Constants.PAGEOF);
		if (pageOf.equals(Constants.PAGEOF_USER_ADMIN)) {
			return super.isAuthorizedToExecute(request);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.action.BaseAction#getSessionData(javax.servlet.http.HttpServletRequest)
	 */
	protected SessionDataBean getSessionData(HttpServletRequest request) {
		String pageOf = request.getParameter(Constants.PAGEOF);
		if (pageOf.equals(Constants.PAGEOF_USER_ADMIN)) {
			return super.getSessionData(request);
		}
		return new SessionDataBean();
	}
}