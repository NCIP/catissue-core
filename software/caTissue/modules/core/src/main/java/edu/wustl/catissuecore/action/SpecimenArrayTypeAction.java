/*
 * <p>Title: SpecimenArrayTypeAction Class </p> <p>Description:This class
 * performs action level logic. </p> Copyright: Copyright (c) year 2006 Company:
 * Washington University, School of Medicine, St. Louis.
 * @version 1.1 Created on July 24,2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
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

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Ashwin Gupta
 */
public class SpecimenArrayTypeAction extends SecureAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SpecimenArrayTypeAction.class);
	/**
	 * Key used in map.
	 */
	private final String specimenClassKey = "SPECIMEN_CLASS";

	/**
	 * Key used in map.
	 */
	private final String specimenTypeKey = "SPECIMEN_TYPE";

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param actionForm
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception : Exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// SpecimenArrayTypeForm specimenArrayTypeForm = (SpecimenArrayTypeForm)
		// actionForm;

		final String operation = request.getParameter(Constants.OPERATION);
		request.setAttribute(Constants.OPERATION, operation);

		// Setting the specimen class list
		final List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_CLASS, null);
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// Setting the specimen type list
		final List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

		final Map specimenTypeClassMap = this.getSpecimenClassAndType();
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenTypeClassMap
				.get(this.specimenClassKey));
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, specimenTypeClassMap
				.get(this.specimenTypeKey));

		/*
		 * if(operation.equals(Constants.ADD)) {
		 * specimenArrayTypeForm.setOneDimensionCapacity(0);
		 * specimenArrayTypeForm.setTwoDimensionCapacity(0); }
		 */
		/*
		 * String strMenu = request.getParameter(Constants.MENU_SELECTED);
		 * if(strMenu != null) { request.setAttribute(Constants.MENU_SELECTED
		 * ,strMenu); Logger.out.debug(Constants.MENU_SELECTED + " " +strMenu
		 * +" set successfully"); }
		 */
		String pageOf = request.getParameter(Constants.PAGE_OF);

		if (pageOf == null)
		{
			pageOf = Constants.SUCCESS;
		}
		return mapping.findForward(pageOf);
	}

	/**
	 * Returns the specimen class & type for each class.
	 *
	 * @return map specimen class & type map
	 */
	private Map getSpecimenClassAndType()
	{
		// get the Specimen class and type from the cde
		final CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		final Set setPV = specimenClassCDE.getPermissibleValues();
		final Iterator itr = setPV.iterator();

		final List specimenClassList = new ArrayList();
		final Map subTypeMap = new HashMap();

		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
		Object pvObject = null;
		PermissibleValue pv = null;
		String pvValue = null;
		List specimenTypeList = null;
		while (itr.hasNext())
		{
			pvObject = itr.next();
			pv = (PermissibleValue) pvObject;
			pvValue = pv.getValue();
			this.logger.debug(pvValue);
			specimenClassList.add(new NameValueBean(pvValue, pvValue));
			specimenTypeList = this.getSpecimenTypeList(pv);
			subTypeMap.put(pv.getValue(), specimenTypeList);
		} // class and values set

		final Map specimenClassTypeMap = new HashMap();
		specimenClassTypeMap.put(this.specimenClassKey, specimenClassList);
		specimenClassTypeMap.put(this.specimenTypeKey, subTypeMap);
		return specimenClassTypeMap;
	}

	/**
	 * returns the specimen type list for specific specimen class.
	 *
	 * @param specimenClassPV
	 *            specimen class permissible value
	 * @return list of specimen type for specimen class
	 */
	private List getSpecimenTypeList(PermissibleValue specimenClassPV)
	{
		final List specimenTypeList = new ArrayList();
		final Set subPVList = specimenClassPV.getSubPermissibleValues();
		this.logger.debug("subPVList " + subPVList);
		final Iterator subPVItr = subPVList.iterator();
		specimenTypeList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
		Object subPVObj = null;
		PermissibleValue subPV = null;
		String subPVValue = null;
		while (subPVItr.hasNext())
		{
			subPVObj = subPVItr.next();
			subPV = (PermissibleValue) subPVObj;
			// set specimen type
			subPVValue = subPV.getValue();
			this.logger.debug("\t\t" + subPVValue);
			specimenTypeList.add(new NameValueBean(subPVValue, subPVValue));
		}
		return specimenTypeList;
	}

}
