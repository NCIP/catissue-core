/**
 * <p>
 * Title: SpecimenProtocolAction Class
 * </p>
 * <p>
 * Description: This class initializes the fields in the Collection /
 * Distribution Add/Edit webpage.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Mandar Deshmukh
 * @version 1.00 Created on Mar 22, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenProtocolForm;
import edu.wustl.catissuecore.bizlogic.SpecimenProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.UserBizLogic;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the Collection / Distribution Add/Edit
 * webpage.
 *
 * @author Mandar Deshmukh
 */
public class SpecimenProtocolAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SpecimenProtocolAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping : mapped to ActionMapping
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
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		if (operation == null)
		{
			operation = (String) request.getAttribute(Constants.OPERATION);
		}
		// Sets the operation attribute to be used in the Add/Edit Collection /
		// Distribution Page.
		request.setAttribute(Constants.OPERATION, operation);

		// Sets the activityStatusList attribute to be used in the Site Add/Edit
		// Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final UserBizLogic userBizLogic = (UserBizLogic) factory
				.getBizLogic(Constants.USER_FORM_ID);
		final Collection userCollection = userBizLogic.getUsers(operation);
		request.setAttribute(Constants.USERLIST, userCollection);
		this.logger.debug("1");
		// get the Specimen class and type from the cde
		final List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

		final CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		final Set setPV = specimenClassCDE.getPermissibleValues();
		this.logger.debug("2");
		final Iterator itr = setPV.iterator();

		// String classValues[][] = new String[setPV.size()][];
		final List specimenClassList = new ArrayList();
		final Map subTypeMap = new HashMap();
		this.logger.debug("\n\n\n\n**********MAP DATA************\n");
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

		// Fill the Map with Specimen as Keys and Subtypes as values.
		// Used for dynamically generation of JavaScript arrays for Specimen
		// Type.
		while (itr.hasNext())
		{
			final List innerList = new ArrayList();
			final Object obj = itr.next();
			final PermissibleValue pv = (PermissibleValue) obj;
			final String tmpStr = pv.getValue();
			this.logger.debug(tmpStr);
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));

			final Set list1 = pv.getSubPermissibleValues();
			this.logger.debug("list1 " + list1);
			final Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
			while (itr1.hasNext())
			{
				final Object obj1 = itr1.next();
				final PermissibleValue pv1 = (PermissibleValue) obj1;
				// set specimen type
				final String tmpInnerStr = pv1.getValue();
				this.logger.debug("\t\t" + tmpInnerStr);
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}
			subTypeMap.put(pv.getValue(), innerList);
		} // class and values set
		this.logger.debug("\n\n\n\n**********MAP DATA************\n");

		// sets the Class list
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// set the map to subtype
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);
		// NameValueBean undefinedVal = new
		// NameValueBean(Constants.UNDEFINED,Constants.UNDEFINED);

		/**
		 * Patch ID:TissueSiteCombo_BugID_3 Description: Setting TissueList with
		 * only Leaf node.
		 */
		final List tissueSiteList = AppUtility.tissueSiteList();
		request.setAttribute(Constants.TISSUE_SITE_LIST, tissueSiteList);

		final List pathologyStatusList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_PATHOLOGICAL_STATUS, null);
		request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, pathologyStatusList);

		// Mandar : 03-apr-06 start
		/*
		 * End date is not been refreshed after Protocol is closed or activated.
		 * Refreshing the enddate manually.
		 */
		this.logger.debug("04-Apr-06");
		final SpecimenProtocolForm spForm = (SpecimenProtocolForm) form;
		if (operation.equalsIgnoreCase(Constants.EDIT))
		{
			// Mandar: 25-july-06 bizlogic call updated.
			final SpecimenProtocolBizLogic bizLogic = (SpecimenProtocolBizLogic) factory
					.getBizLogic(spForm.getFormId());
			final String tmpEndDate = bizLogic.getEndDate(spForm.getId(), this
					.getSessionData(request));
			this.logger.debug("tmpendDate : " + tmpEndDate);
			spForm.setEndDate(tmpEndDate);
		}
		// Mandar : 03-apr-06 end

		return mapping.findForward(request.getParameter(Constants.PAGE_OF));
	}
}