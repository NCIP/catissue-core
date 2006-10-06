/**
 * <p>Title: CreateMultipleSpecimenAction Class>
 * <p>Description:	CreateSpecimenAction initializes the fields in the Create Specimen page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Santosh Chandak
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CreateSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;

/**
 * CreateMultipleSpecimenAction initializes the fields in the Create Derived Specimen page of multiple specimen.
 * @author santosh_chandak
 */
public class CreateMultipleSpecimenAction extends BaseAction
{

	/**
	 * Overrides the execute method of Action class.
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		CreateSpecimenForm createForm = null;

		if (request.getParameter("retainForm") != null)
		{
			createForm = (CreateSpecimenForm) form;
		}
		else
		{
			if (request.getAttribute(Constants.DERIVED_FORM) != null)
			{
				createForm = (CreateSpecimenForm) request.getAttribute(Constants.DERIVED_FORM);
			}
			else
			{
				createForm = new CreateSpecimenForm();
				createForm.setClassName(request.getParameter("derivedSpecimenClass"));
				createForm.setType(request.getParameter("derivedSpecimenType"));
			}
		}

		//Gets the value of the operation parameter.
		String derivedOperation = request.getParameter(Constants.DERIVED_OPERATION);

		CreateSpecimenBizLogic dao = (CreateSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.CREATE_SPECIMEN_FORM_ID);

		StorageContainerBizLogic scbizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.STORAGE_CONTAINER_FORM_ID);

		String specimenCollectionGroupName = (String) request.getParameter("derivedSpecimenCollectionGroup");
		String specimenClass = null;
		
			specimenClass = (String) request.getParameter("derivedSpecimenClass");
		if(specimenClass == null)
		{
			specimenClass = createForm.getClassName();
		}

		Map containerMap = new TreeMap();
		Vector initialValues = null;
		String columnName = Constants.SYSTEM_NAME;
		Object columnValue = specimenCollectionGroupName;

		List specimenCollectionGroupList = dao.retrieve(SpecimenCollectionGroup.class.getName(), columnName, columnValue);

		if (specimenCollectionGroupList != null && !specimenCollectionGroupList.isEmpty() && specimenClass != null && !specimenClass.equals("null")
				&& !specimenClass.equals("-- Select --") && !specimenClass.equals("-1"))
		{
			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) specimenCollectionGroupList.get(0);
			long cpId = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getId().longValue();
			containerMap = scbizLogic.getAllocatedContaienrMapForSpecimen(cpId, specimenClass, 0);
			if (containerMap.isEmpty())
			{
				ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
				if (errors == null || errors.size() == 0)
				{
					errors = new ActionErrors();
				}
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("storageposition.not.available"));
				saveErrors(request, errors);
			}
			initialValues = checkForInitialValues(containerMap);
		}

		if (derivedOperation!=null && derivedOperation.equals(Constants.EDIT))
		{
			String[] startingPoints = new String[]{"-1", "-1", "-1"};
			if (createForm.getStorageContainer() != null && !createForm.getStorageContainer().equals("-1"))
			{
				startingPoints[0] = createForm.getStorageContainer();

			}
			if (createForm.getPositionDimensionOne() != null && !createForm.getPositionDimensionOne().equals("-1"))
			{
				startingPoints[1] = createForm.getPositionDimensionOne();
			}
			if (createForm.getPositionDimensionTwo() != null && !createForm.getPositionDimensionTwo().equals("-1"))
			{
				startingPoints[2] = createForm.getPositionDimensionTwo();
			}
			initialValues = new Vector();
			initialValues.add(startingPoints);
		}

		request.setAttribute("initValues", initialValues);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		// -------------------------

		String[] fields = {"id"};

		//Setting the specimen class list
		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS, null);
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		//Setting the specimen type list
		List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

		//Setting biohazard list
		List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_BIOHAZARD, null);
		request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);

		// get the Specimen class and type from the cde
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		Iterator itr = setPV.iterator();

		specimenClassList = new ArrayList();
		Map subTypeMap = new HashMap();
		Map map = new HashMap();
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

		while (itr.hasNext())
		{
			List innerList = new ArrayList();
			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			String tmpStr = pv.getValue();
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));

			Set list1 = pv.getSubPermissibleValues();
			Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
			while (itr1.hasNext())
			{
				Object obj1 = itr1.next();
				PermissibleValue pv1 = (PermissibleValue) obj1;
				// set specimen type
				String tmpInnerStr = pv1.getValue();
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}
			subTypeMap.put(pv.getValue(), innerList);
		} // class and values set

		// sets the Class list
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// set the map to subtype
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);

		request.setAttribute("initValues", initialValues);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		request.setAttribute("createSpecimenForm", createForm);
		request.setAttribute("multipleSpecimen", "true");
		return mapping.findForward(Constants.SUCCESS);
	}

	/**
	 * This function returns the initial values
	 * @param containerMap - map 
	 * @return - vector of initial values
	 */
	Vector checkForInitialValues(Map containerMap)
	{
		Vector initialValues = null;

		if (containerMap.size() > 0)
		{
			String[] startingPoints = new String[3];

			Set keySet = containerMap.keySet();
			Iterator itr = keySet.iterator();
			NameValueBean nvb = (NameValueBean) itr.next();
			startingPoints[0] = nvb.getValue();

			Map map1 = (Map) containerMap.get(nvb);
			keySet = map1.keySet();
			itr = keySet.iterator();
			nvb = (NameValueBean) itr.next();
			startingPoints[1] = nvb.getValue();

			List list = (List) map1.get(nvb);
			nvb = (NameValueBean) list.get(0);
			startingPoints[2] = nvb.getValue();

			initialValues = new Vector();
			initialValues.add(startingPoints);

		}
		return initialValues;

	}

}