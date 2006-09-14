/**
 * <p>Title: CreateSpecimenAction Class>
 * <p>Description:	CreateSpecimenAction initializes the fields in the Create Specimen page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CreateSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * CreateSpecimenAction initializes the fields in the Create Specimen page.
 * @author aniruddha_phadnis
 */
public class CreateSpecimenAction extends SecureAction
{

	/**
	 * Overrides the execute method of Action class.
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		CreateSpecimenForm createForm = (CreateSpecimenForm) form;

		//List of keys used in map of ActionForm
		List key = new ArrayList();
		key.add("ExternalIdentifier:i_name");
		key.add("ExternalIdentifier:i_value");

		//Gets the map from ActionForm
		Map map = createForm.getExternalIdentifier();

		//Calling DeleteRow of BaseAction class
		MapDataParser.deleteRow(key, map, request.getParameter("status"));

		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);

		//Sets the operation attribute to be used in the Add/Edit User Page. 
		request.setAttribute(Constants.OPERATION, operation);

		String pageOf = request.getParameter(Constants.PAGEOF);
		/*
		 // ---- chetan 15-06-06 ----
		 StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		 Map containerMap = bizLogic.getAllocatedContainerMap();
		 request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
		 // -------------------------
		 request.setAttribute(Constants.PAGEOF,pageOf);
		 */

		CreateSpecimenBizLogic dao = (CreateSpecimenBizLogic) BizLogicFactory.getInstance()
				.getBizLogic(Constants.CREATE_SPECIMEN_FORM_ID);

		StorageContainerBizLogic scbizLogic = (StorageContainerBizLogic) BizLogicFactory
				.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		Map containerMap = new HashMap();
		Vector initialValues = null;
		if (operation.equals(Constants.ADD))
		{
			if (createForm.getParentSpecimenId() != null && !createForm.getParentSpecimenId().equals("") &&
					createForm.getClassName() != null && !createForm.getClassName().equals(""))
			{
				containerMap  = getContainerMap(createForm.getParentSpecimenId(),createForm.getClassName(),dao, scbizLogic)	;
				initialValues = checkForInitialValues(containerMap);
			}
		}
		else
		{
			containerMap = new TreeMap();
			Integer id = new Integer(createForm.getStorageContainer());
			String parentContainerName = "";
			String valueField1 = "id";
			List list = dao.retrieve(StorageContainer.class.getName(), valueField1, new Long(
					createForm.getStorageContainer()));
			if (!list.isEmpty())
			{
				StorageContainer container = (StorageContainer) list.get(0);
				parentContainerName = container.getName();

			}
			Integer pos1 = new Integer(createForm.getPositionDimensionOne());
			Integer pos2 = new Integer(createForm.getPositionDimensionTwo());

			List pos2List = new ArrayList();
			pos2List.add(new NameValueBean(pos2, pos2));

			Map pos1Map = new TreeMap();
			pos1Map.put(new NameValueBean(pos1, pos1), pos2List);
			containerMap.put(new NameValueBean(parentContainerName, id), pos1Map);

			String[] startingPoints = new String[]{"-1", "-1", "-1"};
			if (createForm.getStorageContainer() != null
					&& !createForm.getStorageContainer().equals("-1"))
			{
				startingPoints[0] = createForm.getStorageContainer();

			}
			if (createForm.getPositionDimensionOne() != null
					&& !createForm.getPositionDimensionOne().equals("-1"))
			{
				startingPoints[1] = createForm.getPositionDimensionOne();
			}
			if (createForm.getPositionDimensionTwo() != null
					&& !createForm.getPositionDimensionTwo().equals("-1"))
			{
				startingPoints[2] = createForm.getPositionDimensionTwo();
			}
			initialValues = new Vector();
			Logger.out.info("Starting points[0]" + startingPoints[0]);
			Logger.out.info("Starting points[1]" + startingPoints[1]);
			Logger.out.info("Starting points[2]" + startingPoints[2]);
			initialValues.add(startingPoints);

		}

		// -------------------------

		String[] fields = {"id"};

		// Setting the parent specimen list
		List parentSpecimenList = dao.getList(Specimen.class.getName(), fields, fields[0], true);
		request.setAttribute(Constants.PARENT_SPECIMEN_ID_LIST, parentSpecimenList);

		//Setting the specimen class list
		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_CLASS, null);
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		//Setting the specimen type list
		List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

		//Setting biohazard list
		List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_BIOHAZARD, null);
		request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);

		Logger.out.debug("1");
		// get the Specimen class and type from the cde
		CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
		Set setPV = specimenClassCDE.getPermissibleValues();
		Logger.out.debug("2");
		Iterator itr = setPV.iterator();

		specimenClassList = new ArrayList();
		Map subTypeMap = new HashMap();
		Logger.out.debug("\n\n\n\n**********MAP DATA************\n");
		specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));

		while (itr.hasNext())
		{
			List innerList = new ArrayList();
			Object obj = itr.next();
			PermissibleValue pv = (PermissibleValue) obj;
			String tmpStr = pv.getValue();
			Logger.out.debug(tmpStr);
			specimenClassList.add(new NameValueBean(tmpStr, tmpStr));

			Set list1 = pv.getSubPermissibleValues();
			Logger.out.debug("list1 " + list1);
			Iterator itr1 = list1.iterator();
			innerList.add(new NameValueBean(Constants.SELECT_OPTION, "-1"));
			while (itr1.hasNext())
			{
				Object obj1 = itr1.next();
				PermissibleValue pv1 = (PermissibleValue) obj1;
				// set specimen type
				String tmpInnerStr = pv1.getValue();
				Logger.out.debug("\t\t" + tmpInnerStr);
				innerList.add(new NameValueBean(tmpInnerStr, tmpInnerStr));
			}
			subTypeMap.put(pv.getValue(), innerList);
		} // class and values set
		Logger.out.debug("\n\n\n\n**********MAP DATA************\n");

		// sets the Class list
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// set the map to subtype
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);
		Logger.out.debug("************************************\n\n\nDone**********\n");

		//*************  ForwardTo implementation *************
		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");

		if (forwardToHashMap != null)
		{
			Long parentSpecimenId = (Long) forwardToHashMap.get("parentSpecimenId");
			Logger.out.debug("ParentSpecimenID found in forwardToHashMap========>>>>>>"
					+ parentSpecimenId);

			if (parentSpecimenId != null)
			{
				createForm.setParentSpecimenId(parentSpecimenId.toString());
				createForm.setPositionInStorageContainer("");
				createForm.setQuantity("");
				createForm.setPositionDimensionOne("");
				createForm.setPositionDimensionTwo("");
				createForm.setStorageContainer("");
				createForm.setBarcode(null);
				map.clear();
				createForm.setExternalIdentifier(map);
				createForm.setExIdCounter(1);
				createForm.setVirtuallyLocated(false);	
				containerMap  = getContainerMap(createForm.getParentSpecimenId(),createForm.getClassName(),dao, scbizLogic)	;
				initialValues = checkForInitialValues(containerMap);
			}
		}
		//*************  ForwardTo implementation *************
		request.setAttribute("initValues", initialValues);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		return mapping.findForward(Constants.SUCCESS);
	}

	Map getContainerMap(String specimenId, String className , CreateSpecimenBizLogic dao, StorageContainerBizLogic scbizLogic) throws DAOException
	{
		Map containerMap = new HashMap();
		
		List spList = dao.retrieve(Specimen.class.getName(), Constants.SYSTEM_IDENTIFIER,
				new Long(specimenId));

		if (!spList.isEmpty())
		{
			Specimen sp = (Specimen) spList.get(0);
			long cpId = sp.getSpecimenCollectionGroup().getCollectionProtocolRegistration()
					.getCollectionProtocol().getId().longValue();
			String spClass = className;
			Logger.out.info("cpId :" + cpId + "spClass:" + spClass);
			containerMap = scbizLogic.getAllocatedContaienrMapForSpecimen(cpId, spClass);
		}
		
		return containerMap;
	}
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

			Logger.out.info("Starting points[0]" + startingPoints[0]);
			Logger.out.info("Starting points[1]" + startingPoints[1]);
			Logger.out.info("Starting points[2]" + startingPoints[2]);
			initialValues = new Vector();
			initialValues.add(startingPoints);

		}
		return initialValues;

		//request.setAttribute("initValues", initialValues);
	}
}
