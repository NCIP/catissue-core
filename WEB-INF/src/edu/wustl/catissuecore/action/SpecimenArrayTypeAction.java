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
	private transient Logger logger = Logger.getCommonLogger(SpecimenArrayTypeAction.class);
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
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// SpecimenArrayTypeForm specimenArrayTypeForm = (SpecimenArrayTypeForm)
		// actionForm;

		String operation = request.getParameter(Constants.OPERATION);
		request.setAttribute(Constants.OPERATION, operation);

		// Setting the specimen class list
		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_CLASS, null);
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// Setting the specimen type list
		List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

		Map specimenTypeClassMap = getSpecimenClassAndType();
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, (List) specimenTypeClassMap
				.get(specimenClassKey));
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, (Map) specimenTypeClassMap
				.get(specimenTypeKey));

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
		String pageOf = (String) request.getParameter(Constants.PAGE_OF);

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
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator itr = setPV.iterator();

		List specimenClassList = new ArrayList();
		Map subTypeMap = new HashMap();

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
			logger.debug(pvValue);
			specimenClassList.add(new NameValueBean(pvValue, pvValue));
			specimenTypeList = getSpecimenTypeList(pv);
			subTypeMap.put(pv.getValue(), specimenTypeList);
		} // class and values set

		Map specimenClassTypeMap = new HashMap();
		specimenClassTypeMap.put(specimenClassKey, specimenClassList);
		specimenClassTypeMap.put(specimenTypeKey, subTypeMap);
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
		List specimenTypeList = new ArrayList();
		Set subPVList = specimenClassPV.getSubPermissibleValues();
		logger.debug("subPVList " + subPVList);
		Iterator subPVItr = subPVList.iterator();
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
			logger.debug("\t\t" + subPVValue);
			specimenTypeList.add(new NameValueBean(subPVValue, subPVValue));
		}
		return specimenTypeList;
	}

}
