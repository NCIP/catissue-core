/**
 * <p>
 * Title: UserAction Class>
 * <p>
 * Description: This class initializes the fields in the User Add/Edit webpage.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Gautam Shetty
 * @version 1.00 Created on Mar 22, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.cagrid.gaards.saml.encoding.SAMLUtils;
import org.json.JSONException;
import org.json.JSONObject;

import edu.wustl.auth.exception.AuthFileParseException;
import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.authmanager.IDPAuthManager;
import edu.wustl.authmanager.factory.AuthManagerFactory;
import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.ctrp.COPPAUtil;
import edu.wustl.catissuecore.domain.CancerResearchGroup;
import edu.wustl.catissuecore.domain.Department;
import edu.wustl.catissuecore.domain.Institution;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.factory.utils.UserUtility;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.uiobject.UserUIObject;
import edu.wustl.catissuecore.util.CaTissuePrivilegeUtility;
import edu.wustl.catissuecore.util.MSRUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.DefaultValueManager;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.UserDetails;
import edu.wustl.migrator.util.Utility;
import edu.wustl.processor.LoginProcessor;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

/**
 * This class initializes the fields in the User Add/Edit webpage.
 *
 * @author gautam_shetty
 */
public class UserAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(UserAction.class);
	
	private List<JSONObject> ajaxAuthenticate(String loginName , String password , String idp) throws JSONException {
		JSONObject jsonobject = new JSONObject();
		List<JSONObject> userDetailsList = new ArrayList<JSONObject>();
		
		UserBizLogic userBizLogic = new UserBizLogic();
		UserUIObject userUIObject = new UserUIObject();
		userUIObject.setTargetIdp(idp);
		userUIObject.setTargetIdpLoginName(loginName);
		userUIObject.setTargetPassword(password);
		userUIObject.setGrouperUser("yes");
		
		SAMLAssertion saml;
		try {
			saml = userBizLogic.getSAMLAssertion(null, userUIObject);
		} catch (BizLogicException e) {
			// TODO Auto-generated catch block
			logger.debug(e.getMessage());
			jsonobject.put("errorMsg", e.getMessage());
			userDetailsList.add(jsonobject);
			return userDetailsList;
		}

        SAMLUtils sutils = new SAMLUtils();
        
        String nameSpace = "urn:mace:shibboleth:1.0:attributeNamespace:uri";

        String firstName = sutils.getAttributeValue(saml, nameSpace, "urn:mace:dir:attribute-def:givenName");
        String lastName = sutils.getAttributeValue(saml, nameSpace, "urn:mace:dir:attribute-def:sn");
        String email = sutils.getAttributeValue(saml, nameSpace, "urn:mace:dir:attribute-def:mail");
        
        jsonobject.put("firstName", firstName);
        jsonobject.put("lastName", lastName);
        jsonobject.put("emailAddress", email);
        jsonobject.put("errorMsg", "");

        jsonobject.put("success", "Authentication successfull. Please enter your information below to have your account activated for use in caTissue.");
        userDetailsList.add(jsonobject);
        return userDetailsList;
	}

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeSecureAction(final ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
			{
		String ajaxAuthentication = request.getParameter("ajaxAuthentication");
		if (ajaxAuthentication != null) {
			String ajaxLogin = request.getParameter("ajaxLogin");
			String ajaxPassword = request.getParameter("ajaxPassword");
			String ajaxIdp = request.getParameter("ajaxIdp");

			List list = ajaxAuthenticate(ajaxLogin,ajaxPassword,ajaxIdp);
			MSRUtil msrUtil = new MSRUtil();
			msrUtil.setResponse(response, list);
			return mapping.findForward(null);
		}
		
		// Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		String pageOf = request.getParameter(Constants.PAGE_OF);
		final String reqPath = request.getParameter(Constants.REQ_PATH);
		final String submittedFor = (String) request.getAttribute(Constants.SUBMITTED_FOR);
		final String openInCPFrame = request.getParameter(Constants.OPEN_PAGE_IN_CPFRAME);
		final UserForm userForm = (UserForm) form;
		UserDetails details = LoginProcessor.getUserDetails(userForm.getEmailAddress());
		final String grouperUser = request.getParameter("grouperUser");
		final String userRedirectedFromCas = (String) request.getAttribute("casuser");
		if (userRedirectedFromCas!=null) {
			userForm.setTargetLoginName(userRedirectedFromCas);
		}
		if(details != null)
		{
			userForm.setTargetLoginName(details.getMigratedLoginName());
			userForm.setTargetIdp(details.getTargetIDP());
		}
		// WustlKey Check
		checkForWustlKey(userForm, request);
		// method to get myProfile-Add Privilege
		final SessionDataBean sessionDataBean = getSessionData(request);
		// String readOnlyForPrivOnEdit = "";
		// String disablePrivButton = "false";
		long loggedInUserId = 0;
		if ((Constants.PAGE_OF_USER).equals(pageOf) && sessionDataBean.getUserId() != null)
		{
			loggedInUserId = sessionDataBean.getUserId();
		}

		if ((Constants.PAGE_OF_USER).equals(pageOf) && sessionDataBean != null
				&& loggedInUserId == userForm.getId())
		{
			pageOf = Constants.PAGE_OF_USER_PROFILE;
			// readOnlyForPrivOnEdit = "disabled='true'";
			// disablePrivButton ="true";
			// request.setAttribute("readOnlyForPrivOnEdit",
			// readOnlyForPrivOnEdit);
			// request.setAttribute("disablePrivButton", disablePrivButton);

		}
		// method to get myProfile end here

		// method to preserve data on validation
		final MSRUtil msrUtil = new MSRUtil();
		if (operation.equalsIgnoreCase(Constants.ADD))
		{
			final HttpSession session = request.getSession();
			boolean dirtyVar = false;
			dirtyVar = new Boolean(request.getParameter("dirtyVar"));
			if (!dirtyVar)
			{
				session.removeAttribute(Constants.USER_ROW_ID_BEAN_MAP);
			}
		}
		// method to preserve data on validation end here

		String formName, prevPage = null, nextPage = null;
		boolean roleStatus = false;
		if (pageOf.equals(Constants.PAGE_OF_APPROVE_USER))
		{
			Long identifier = (Long) request.getAttribute(Constants.PREVIOUS_PAGE);
			request.setAttribute("prevPageId", identifier);
			prevPage = Constants.USER_DETAILS_SHOW_ACTION + "?" + Constants.SYSTEM_IDENTIFIER + "=" + identifier;
			identifier = (Long) request.getAttribute(Constants.NEXT_PAGE);
			nextPage = Constants.USER_DETAILS_SHOW_ACTION + "?" + Constants.SYSTEM_IDENTIFIER + "=" + identifier;
			request.setAttribute("nextPageId", identifier);

		}
		if (!pageOf.equals(Constants.PAGE_OF_APPROVE_USER))
		{
			if (operation.equals(Constants.EDIT) && (userForm.getCsmUserId() != null))
			{
				if (userForm.getCsmUserId().longValue() == 0)
				{
					final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
					final UserBizLogic bizLogic = (UserBizLogic) factory.getBizLogic(Constants.USER_FORM_ID);
					final String sourceObjName = User.class.getName();
					final String[] selectColName = { "csmUserId" };
					final String[] whereColName = { "id" };
					final String[] whereColCond = { "=" };
					final Object[] whereColVal = { userForm.getId() };

					final List regList = bizLogic.retrieve(sourceObjName, selectColName, whereColName,
							whereColCond, whereColVal, Constants.AND_JOIN_CONDITION);
					if (regList != null && !regList.isEmpty())
					{
						final Object obj = regList.get(0);
						final Long id = (Long) obj;
						userForm.setCsmUserId(id);
					}
				}
			}
		}
		if (operation.equals(Constants.EDIT))
		{
			if (!pageOf.equals(Constants.PAGE_OF_APPROVE_USER))
			{
				setUserPrivileges(request.getSession(), userForm.getId());
			}

			if (pageOf.equals(Constants.PAGE_OF_APPROVE_USER))
			{
				formName = Constants.APPROVE_USER_EDIT_ACTION;
			}
			else if (pageOf.equals(Constants.PAGE_OF_USER_PROFILE))
			{
				formName = Constants.USER_EDIT_PROFILE_ACTION;
			}
			else
			{
				formName = Constants.USER_EDIT_ACTION;
			}
		}
		else
		{
			if (pageOf.equals(Constants.PAGE_OF_APPROVE_USER))
			{
				formName = Constants.APPROVE_USER_ADD_ACTION;
			}
			else
			{
				formName = Constants.USER_ADD_ACTION;
				if (pageOf.equals(Constants.PAGE_OF_SIGNUP))
				{
					formName = Constants.SIGNUP_USER_ADD_ACTION;
				}
			}
		}
		if (pageOf.equals(Constants.PAGE_OF_APPROVE_USER)
				&& (userForm.getStatus().equals(Status.APPROVE_USER_PENDING_STATUS.toString())
						|| userForm.getStatus().equals(Status.APPROVE_USER_REJECT_STATUS.toString()) || userForm
						.getStatus().equals(Constants.SELECT_OPTION)))
		{
			roleStatus = true;
			if (userForm.getStatus().equals(Status.APPROVE_USER_PENDING_STATUS.toString()))
			{
				operation = Constants.EDIT;
			}
		}
		if (pageOf.equals(Constants.PAGE_OF_USER_PROFILE))
		{
			roleStatus = true;
		}
		if (operation.equalsIgnoreCase(Constants.ADD))
		{
			// request.getSession(true).setAttribute(Constants.
					// USER_ROW_ID_BEAN_MAP,
					// null);

			if (userForm.getCountry() == null)
			{
				userForm.setCountry((String) DefaultValueManager.getDefaultValue(Constants.DEFAULT_COUNTRY));
			}

		}
		if (pageOf.equals(Constants.PAGE_OF_SIGNUP))
		{
			String idpSelection;

			if(Validator.isEmpty(userForm.getIdpSelection()))
			{
				idpSelection="no";
			}
			else
			{
				idpSelection=userForm.getIdpSelection();
			}
			if (grouperUser != null) {
				idpSelection="yes";
			}
			request.setAttribute("idpSelection",idpSelection);
			setIDPsListToRequest(request);

			userForm.setStatus(Status.ACTIVITY_STATUS_NEW.toString());
			userForm.setActivityStatus(Status.ACTIVITY_STATUS_NEW.toString());				
			
		}
		userForm.setOperation(operation);
		userForm.setPageOf(pageOf);
		userForm.setSubmittedFor(submittedFor);
		userForm.setRedirectTo(reqPath);

		final String roleStatusforJSP = roleStatus + "";

		request.setAttribute("roleStatus", roleStatusforJSP);
		request.setAttribute("formName", formName);
		request.setAttribute("prevPageURL", prevPage);
		request.setAttribute("nextPageURL", nextPage);

		// Sets the countryList attribute to be used in the Add/Edit User Page.
		final List countryList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COUNTRY_LIST, null);
		request.setAttribute("countryList", countryList);

		// Sets the stateList attribute to be used in the Add/Edit User Page.
		final List stateList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_STATE_LIST,
				null);
		request.setAttribute("stateList", stateList);

		// Sets the pageOf attribute (for Add,Edit or Query Interface).
		String target = pageOf;
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(Constants.USER_FORM_ID);

		// Sets the instituteList attribute to be used in the Add/Edit User
		// Page.
		String sourceObjectName = Institution.class.getName();
		final String[] displayNameFields = { Constants.NAME };
		final String valueField = Constants.SYSTEM_IDENTIFIER;

		final List instituteList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, false);
		request.setAttribute("instituteList", instituteList);

		// Sets the departmentList attribute to be used in the Add/Edit User
		// Page.
		sourceObjectName = Department.class.getName();
		final List departmentList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, false);
		request.setAttribute("departmentList", departmentList);

		// Sets the cancerResearchGroupList attribute to be used in the Add/Edit
		// User Page.
		sourceObjectName = CancerResearchGroup.class.getName();
		final List cancerResearchGroupList = bizLogic.getList(sourceObjectName, displayNameFields, valueField,
				false);
		request.setAttribute("cancerResearchGroupList", cancerResearchGroupList);

		// Populate the activity status dropdown if the operation is edit
		// and the user page is of administrative tab.
		if (operation.equals(Constants.EDIT) && pageOf.equals(Constants.PAGE_OF_USER_ADMIN))
		{
			// String activityStatusList = Constants.ACTIVITYSTATUSLIST;
			request.setAttribute("activityStatusList", Constants.USER_ACTIVITY_STATUS_VALUES);
		}

		// Populate the role dropdown if the page is of approve user or
		// (Add/Edit) user page of adminitraive tab.
		// if (pageOf.equals(Constants.PAGE_OF_APPROVE_USER) ||
		// pageOf.equals(Constants.PAGE_OF_USER_ADMIN)
		// ||pageOf.equals(Constants.PAGE_OF_USER_PROFILE ))
		// {
		// List roleNameValueBeanList = getRoles();
		// request.setAttribute("roleList", roleNameValueBeanList);
		// }

		// Populate the status dropdown for approve user
		// page.(Approve,Reject,Pending)
		if (pageOf.equals(Constants.PAGE_OF_APPROVE_USER))
		{
			request.setAttribute("statusList", Constants.APPROVE_USER_STATUS_VALUES);
		}

		logger.debug("pageOf :---------- " + pageOf);

		// To show Role as Scientist
		final DAO dao = DAOConfigFactory.getInstance().getDAOFactory(Constants.APPLICATION_NAME).getDAO();
		dao.openSession(sessionDataBean);
		final List<User> userList = dao.retrieve(User.class.getName(), "emailAddress", userForm.getEmailAddress());
		if (!userList.isEmpty())
		{
			final User user = userList.get(0);

			if (!Constants.ADMIN_USER.equals(UserUtility.getRoleId(user))&& user.getSiteCollection().isEmpty())
			{
				userForm.setRole(Constants.NON_ADMIN_USER);
			}
		}
		dao.closeSession();

		// For Privilege
		final String roleId = userForm.getRole();
		boolean flagForSARole = false;
		if ((Constants.SUPER_ADMIN_USER).equals(roleId))
		{
			flagForSARole = true;
			// To show empty summary in case User is Super Administrator
			request.getSession(true).setAttribute(Constants.USER_ROW_ID_BEAN_MAP, null);
		}
		request.setAttribute("flagForSARole", flagForSARole);

		msrUtil.onFirstTimeLoad(mapping, request);

		final String cpOperation = request.getParameter("cpOperation");
		if (cpOperation != null)
		{
			return msrUtil.setAJAXResponse(request, response, cpOperation);
		}
		// Parameters for JSP

		final int SELECT_OPTION_VALUE = Constants.SELECT_OPTION_VALUE;
		boolean readOnlyEmail = false;
		if (operation.equals(Constants.EDIT) && pageOf.equals(Constants.PAGE_OF_USER_PROFILE))
		{
			readOnlyEmail = true;
		}
		request.setAttribute("SELECT_OPTION_VALUE", SELECT_OPTION_VALUE);
		request.setAttribute("Approve", Status.APPROVE_USER_APPROVE_STATUS.toString());
		request.setAttribute("pageOfApproveUser", Constants.PAGE_OF_APPROVE_USER);
		request.setAttribute("backPage", Constants.APPROVE_USER_SHOW_ACTION + "?" + Constants.PAGE_NUMBER + "="
				+ Constants.START_PAGE);
		request.setAttribute("redirectTo", Constants.REQ_PATH);
		request.setAttribute("addforJSP", Constants.ADD);
		request.setAttribute("editforJSP", Constants.EDIT);
		request.setAttribute("searchforJSP", Constants.SEARCH);
		request.setAttribute("readOnlyEmail", readOnlyEmail);
		request.setAttribute("pageOfUserProfile", Constants.PAGE_OF_USER_PROFILE);
		request.setAttribute("pageOfUserAdmin", Constants.PAGE_OF_USER_ADMIN);
		request.setAttribute("pageOfSignUp", Constants.PAGE_OF_SIGNUP);
		request.setAttribute("pageOf", pageOf);
		request.setAttribute("operation", operation);
		request.setAttribute("openInCPFrame", openInCPFrame);
		request.setAttribute("isCOPPAEnabled", COPPAUtil.isCOPPAEnabled());
		request.setAttribute("grouperUser", grouperUser);
		//request.setAttribute("targetLoginName", userForm);
		// ------------- add new
		logger.debug("USerAction redirect :---------- " + reqPath);
		if (openInCPFrame != null && Constants.TRUE.equalsIgnoreCase(openInCPFrame))
		{
			target = Constants.OPEN_PAGE_IN_CPFRAME;
		}
		return mapping.findForward(target);
			}

	private void setIDPsListToRequest(final HttpServletRequest request) throws CatissueException
	{
		try
		{
			final List<NameValueBean> idpsList = Utility.getConfiguredIDPNVB(false);
			if(idpsList.size()>0)
			{
				request.setAttribute("idpsList", idpsList);
			}
		}
		catch (final AuthFileParseException parseException)
		{
			logger.debug(parseException);
			throw new CatissueException(parseException);
		}

	}

	/**
	 *
	 * @param session
	 *            : session
	 * @param id
	 *            : id
	 */
	private void setUserPrivileges(final HttpSession session, final long id)
	{
		if (id == 0)
		{
			return;
		}
		try
		{
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic bizLogic = factory.getBizLogic(Constants.USER_FORM_ID);
			final User user = (User) bizLogic.retrieve(User.class.getName(), id);
			final String role = UserUtility.getRoleId(user);
			if (role != null && !role.equalsIgnoreCase(Constants.ADMIN_USER))
			{
				final PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
				// privilegeManager.removePrivilegeCache(user.getLoginName());
				final PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(user.getLoginName());
				privilegeCache.refresh();
				final Map<String, SiteUserRolePrivilegeBean> privilegeMap = CaTissuePrivilegeUtility
				.getAllPrivileges(privilegeCache);
				session.setAttribute(Constants.USER_ROW_ID_BEAN_MAP, privilegeMap);
			}
		}
		catch (final ApplicationException e)
		{
			logger.error(e.getLogMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.wustl.catissuecore.action.SecureAction#isAuthorizedToExecute(javax
	 * .servlet.http.HttpServletRequest)
	 */
	/*
	 * protected boolean isAuthorizedToExecute(HttpServletRequest request)
	 * throws Exception { String pageOf =
	 * request.getParameter(Constants.PAGE_OF); if
	 * (pageOf.equals(Constants.PAGE_OF_USER_ADMIN)) { return
	 * super.isAuthorizedToExecute(request); } return true; }
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * edu.wustl.catissuecore.action.BaseAction#getSessionData(javax.servlet
	 * .http.HttpServletRequest)
	 */
	/**
	 * @param request
	 *            : request
	 * @return SessionDataBean : SessionDataBean
	 */
	@Override
	protected SessionDataBean getSessionData(final HttpServletRequest request)
	{
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		if (pageOf.equals(Constants.PAGE_OF_USER_ADMIN))
		{
			return super.getSessionData(request);
		}
		return new SessionDataBean();
	}

	/**
	 * @param form
	 *            Object of user form
	 * @param request
	 *            Object of HttpServletRequest
	 */
	private void checkForWustlKey(final UserForm form, final HttpServletRequest request)
	{
		final String userFrom = request.getParameter("userFrom");
		final String grouperUser = request.getParameter("grouperUser");
		final String errorPage = request.getParameter(Constants.ERROR);
		if (userFrom != null)
		{
			final String wustlKey = (String) request
			.getAttribute(edu.wustl.wustlkey.util.global.Constants.WUSTLKEY);
			String userRedirectedFromCas = request.getParameter("casuser");
			if (userRedirectedFromCas!=null) {
				form.setTargetLoginName(userRedirectedFromCas);
			}
			request.setAttribute("wustlKey", wustlKey);
			form.setFirstName((String) request.getAttribute(edu.wustl.wustlkey.util.global.Constants.FIRST_NAME));
			form.setLastName((String) request.getAttribute(edu.wustl.wustlkey.util.global.Constants.LAST_NAME));
			final ActionMessages messages = new ActionMessages();
			if (grouperUser == null) {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("app.washuuser"));
			} else {
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("app.grouperuser"));
			}
			saveMessages(request, messages);
		}
		if (Constants.TRUE.equals(errorPage))
		{
			if (form.getTargetLoginName() != null && !"".equals(form.getTargetLoginName()))
			{
				request.setAttribute("wustlKey", edu.wustl.wustlkey.util.global.Constants.WASHU);
			}
		}
	}
}