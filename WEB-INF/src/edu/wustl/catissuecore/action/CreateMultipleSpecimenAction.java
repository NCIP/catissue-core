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
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CreateSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.MapDataParser;

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
		//boolean to indicate whether the suitable containers to be shown in dropdown 
		//is exceeding the max limit.
		String exceedingMaxLimit = "false";
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

		List key = new ArrayList();
		key.add("ExternalIdentifier:i_name");
		key.add("ExternalIdentifier:i_value");

		//Gets the map from ActionForm
		Map externalIdMap = createForm.getExternalIdentifier();

		//Calling DeleteRow of BaseAction class
		MapDataParser.deleteRow(key, externalIdMap, request.getParameter("status"));

		//Gets the value of the operation parameter.
		String derivedOperation = request.getParameter(Constants.DERIVED_OPERATION);

		CreateSpecimenBizLogic dao = (CreateSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.CREATE_SPECIMEN_FORM_ID);

		StorageContainerBizLogic scbizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.STORAGE_CONTAINER_FORM_ID);

		String specimenCollectionGroupName = (String) request.getParameter("derivedSpecimenCollectionGroup");
		String parentSpecimenLabel = (String) request.getParameter("derivedParentSpecimenLabel");
		String specimenClass = (String) request.getParameter("derivedSpecimenClass");
		// TODO which radio is selected
		if (specimenClass == null)
		{
			specimenClass = createForm.getClassName();
		}
		long cpId = -1;
		if (parentSpecimenLabel != null && !parentSpecimenLabel.equals("null") && !parentSpecimenLabel.equals(""))
		{
			List spList = dao.retrieve(Specimen.class.getName(), Constants.SYSTEM_LABEL, parentSpecimenLabel.trim());
			if (spList != null && !spList.isEmpty())
			{
				Specimen sp = (Specimen) spList.get(0);
				cpId = sp.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getId().longValue();

			}
		}
		else
		{
			List specimenCollectionGroupList = dao.retrieve(SpecimenCollectionGroup.class.getName(), Constants.SYSTEM_NAME,
					specimenCollectionGroupName);
			if (specimenCollectionGroupList != null && !specimenCollectionGroupList.isEmpty())
			{
				SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) specimenCollectionGroupList.get(0);
				cpId = specimenCollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getId().longValue();
			}
		}

/*		// commented for storage location remove from derive page --- Ashwin 
  		TreeMap containerMap = new TreeMap();
		Vector initialValues = null;
*/
		if (cpId != -1 && specimenClass != null && !specimenClass.equals("null") && !specimenClass.equals("-- Select --")
				&& !specimenClass.equals("-1"))
		{
/*
 			// commented for storage location remove from derive page --- Ashwin
 			containerMap = scbizLogic.getAllocatedContaienrMapForSpecimen(cpId, specimenClass, 0,exceedingMaxLimit,true);
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
*/			
			request.setAttribute(Constants.COLLECTION_PROTOCOL_ID, cpId + "");
			request.setAttribute(Constants.SPECIMEN_CLASS_NAME, specimenClass);
			;
		}

		if (derivedOperation != null && derivedOperation.equals(Constants.EDIT))
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
			/*
			// commented for storage location remove from derive page --- Ashwin 
			initialValues = new Vector();
			initialValues.add(startingPoints);
			*/
		}
		//commented for storage location remove from derive page --- Ashwin
		//request.setAttribute("initValues", initialValues);
		//request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
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
		//commented for storage location remove from derive page --- Ashwin
		//request.setAttribute("initValues", initialValues);
		
		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT,exceedingMaxLimit);
		
		//commented for storage location remove from derive page --- Ashwin
		//request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
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