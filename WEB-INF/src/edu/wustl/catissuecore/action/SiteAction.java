/**
 * <p>
 * Title: SiteAction Class>
 * <p>
 * Description: This class initializes the fields of the Site Add/Edit webpage.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Aniruddha Phadnis
 * @version 1.00 Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SiteForm;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields of the Site Add/Edit webpage.
 *
 * @author aniruddha_phadnis
 */
public class SiteAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SiteAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @return ActionForward : ActionForward
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
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final SiteForm siteForm = (SiteForm) form;

		// Gets the value of the operation parameter.
		final String operation = request.getParameter(Constants.OPERATION);

		// Sets the operation attribute to be used in the Add/Edit User Page.

		final String pageOf = request.getParameter(Constants.PAGE_OF);
		final String submittedFor = (String) request.getAttribute(Constants.SUBMITTED_FOR);

		siteForm.setSubmittedFor(submittedFor);
		siteForm.setOperation(operation);
		String formName;
		if (operation.equals(Constants.EDIT))
		{
			formName = Constants.SITE_EDIT_ACTION;
		}
		else
		{
			formName = Constants.SITE_ADD_ACTION;
		}
		request.setAttribute("formName", formName);
		request.setAttribute("operationAdd", Constants.ADD);
		request.setAttribute("operationEdit", Constants.EDIT);
		request.setAttribute("operationForActivityStatus", Constants.OPERATION);

		// Sets the countryList attribute to be used in the Add/Edit User Page.
		final List countryList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_COUNTRY_LIST, null);
		request.setAttribute(Constants.COUNTRYLIST, countryList);

		// Sets the stateList attribute to be used in the Add/Edit User Page.
		final List stateList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_STATE_LIST, null);
		request.setAttribute(Constants.STATELIST, stateList);

		// Sets the activityStatusList attribute to be used in the Site Add/Edit
		// Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.SITE_ACTIVITY_STATUS_VALUES);

		// Sets the siteTypeList attribute to be used in the Site Add/Edit Page.
		final List siteList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SITE_TYPE, null);
		request.setAttribute(Constants.SITETYPELIST, siteList);
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		final Collection coll = userBizLogic.getUsers(operation);
		request.setAttribute(Constants.USERLIST, coll);

		// ------------------------------------------------------------------

		final boolean isOnChange = this.getIsOnChange(request);
		final Long coordinatorId = this.getCoordinatorId(request);

		if (siteForm != null && isOnChange && coordinatorId != null)
		{
			String emailAddress = "";
			String street = "";
			String city = "";
			String state = "";
			String country = "";
			String zipCode = "";
			String phoneNo = "";

			final List userList = userBizLogic.retrieve(User.class.getName(),
					Constants.SYSTEM_IDENTIFIER, coordinatorId);

			if (userList.size() > 0)
			{
				final User user = (User) userList.get(0);
				if (user != null)
				{
					emailAddress = user.getEmailAddress();
					this.logger.debug("Email Id of Coordinator of Site : " + emailAddress);

					siteForm.setEmailAddress(emailAddress);
					if (user.getAddress() != null)
					{

						street = user.getAddress().getStreet();
						siteForm.setStreet(street);

						city = user.getAddress().getCity();
						siteForm.setCity(city);

						state = user.getAddress().getState();
						siteForm.setState(state);

						country = user.getAddress().getCountry();
						siteForm.setCountry(country);

						zipCode = user.getAddress().getZipCode();
						siteForm.setZipCode(zipCode);

						phoneNo = user.getAddress().getPhoneNumber();
						siteForm.setPhoneNumber(phoneNo);

					}

				}
			}
		}

		return mapping.findForward(pageOf);
	}

	/**
	 * method for getting coordinatorId from request.
	 *
	 * @param request
	 *            :object of HttpServletResponse
	 * @return coordinatorId
	 */
	private Long getCoordinatorId(HttpServletRequest request)
	{
		Long coordinatorId = null;
		final String coordinatorIdStr = request.getParameter("coordinatorId");
		new Validator();
		try
		{
			if (!Validator.isEmpty(coordinatorIdStr))
			{
				coordinatorId = new Long(coordinatorIdStr);
			}
		}
		catch (final Exception e)
		{
			this.logger.debug(e.getMessage(), e);
			coordinatorId = null;
		}
		return coordinatorId;
	}

	/**
	 * method for getting isOnChange from request.
	 *
	 * @param request
	 *            :object of HttpServletResponse
	 * @return isOnChange :boolean
	 */
	private boolean getIsOnChange(HttpServletRequest request)
	{
		boolean isOnChange = false;
		final String str = request.getParameter("isOnChange");
		if (str != null && str.equals("true"))
		{
			isOnChange = true;
		}
		return isOnChange;
	}

	/**** code for ajax *****/

}