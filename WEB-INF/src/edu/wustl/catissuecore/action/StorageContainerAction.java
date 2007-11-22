/**
 * <p>Title: StorageContainerAction Class>
 * <p>Description:	This class initializes the fields of StorageContainer.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 18, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

import edu.wustl.catissuecore.actionForm.StorageContainerForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

public class StorageContainerAction extends SecureAction
{

	/**
	 * Overrides the execute method of Action class.
	 * Initializes the various fields in StorageContainer.jsp Page.
	 * @author Vaishali Khandelwal
	 * */
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		StorageContainerForm storageContainerForm = (StorageContainerForm) form;
		//boolean to indicate whether the suitable containers to be shown in dropdown 
		//is exceeding the max limit.
		String exceedingMaxLimit = "false";

		if (storageContainerForm.getSpecimenOrArrayType() == null)
		{
			storageContainerForm.setSpecimenOrArrayType("Specimen");
		}
		//	set the menu selection 
		request.setAttribute(Constants.MENU_SELECTED, "7");

		boolean isTypeChange = false;
		boolean isSiteOrParentContainerChange = false;

		String str = request.getParameter("isOnChange");
		str = request.getParameter("typeChange");
		if (str != null && str.equals("true"))
		{
			isTypeChange = true;

		}
		str = request.getParameter("isSiteOrParentContainerChange");
		if (str != null && str.equals("true"))
		{
			isSiteOrParentContainerChange = true;

		}
//		Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		request.setAttribute(Constants.OPERATION,operation);
		
		setRequestAttributes(request);
		setStorageType(request, storageContainerForm);
		
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		//    	 ---- chetan 15-06-06 ----

		TreeMap containerMap = new TreeMap();
		if (storageContainerForm.getTypeId() != -1)
		{
			long start = System.currentTimeMillis();
			containerMap = bizLogic.getAllocatedContaienrMapForContainer(storageContainerForm.getTypeId(), exceedingMaxLimit, null);
			long end = System.currentTimeMillis();
			
			System.out.println("Time taken for getAllocatedMapForCOntainer:"+(end-start));
		}

		if (operation.equals(Constants.ADD))
		{

			SetParentStorageContainersForAdd(containerMap, storageContainerForm, request);
		}

		if (operation.equals(Constants.EDIT))
		{
			if (StorageContainerUtil.chkContainerFull(new Long(storageContainerForm.getId()).toString(), storageContainerForm.getContainerName()))
			{
				storageContainerForm.setIsFull("true");
			}
			List storagetypeList = new ArrayList();
			NameValueBean nvb = new NameValueBean(storageContainerForm.getTypeName(), new Long(storageContainerForm.getTypeId()));
			storagetypeList.add(nvb);
			request.setAttribute(Constants.STORAGETYPELIST, storagetypeList);
			SetParentStorageCOntainersForEdit(containerMap, storageContainerForm, request);
		}

		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);

		if (isSiteOrParentContainerChange)
		{
			onSiteOrParentContChange(request, storageContainerForm);
		}

		
		
		setFormAttributesForAddNew(request, storageContainerForm);
		// -- 24-Jan-06 end
		if (isTypeChange || request.getAttribute(Constants.SUBMITTED_FOR) != null)
		{
			onTypeChange(storageContainerForm, operation, request);
		}

		if (request.getAttribute(Constants.SUBMITTED_FOR) != null)
		{
			parentContChange(request, storageContainerForm);
		}

		// ---------- Add new
		String reqPath = request.getParameter(Constants.REQ_PATH);

		if (reqPath != null)
		{
			request.setAttribute(Constants.REQ_PATH, reqPath);
		}

		return mapping.findForward((String) request.getParameter(Constants.PAGEOF));
	}

	Vector checkForInitialValues(TreeMap containerMap)
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

	}

	private void addPostions(Map containerMap, Long id, String containerName, Integer pos1, Integer pos2)
	{
		int flag = 0;
		NameValueBean xpos = new NameValueBean(pos1, pos1);
		NameValueBean ypos = new NameValueBean(pos2, pos2);
		NameValueBean parentId = new NameValueBean(containerName, id);

		Set keySet = containerMap.keySet();
		Iterator itr = keySet.iterator();
		while (itr.hasNext())
		{
			NameValueBean nvb = (NameValueBean) itr.next();
			if (nvb.getValue().equals(id.toString()))
			{
				Map pos1Map = (Map) containerMap.get(nvb);
				Set keySet1 = pos1Map.keySet();
				Iterator itr1 = keySet1.iterator();
				while (itr1.hasNext())
				{
					NameValueBean nvb1 = (NameValueBean) itr1.next();
					if (nvb1.getValue().equals(pos1.toString()))
					{
						List pos2List = (List) pos1Map.get(nvb1);
						pos2List.add(ypos);
						flag = 1;
						break;
					}
				}
				if (flag != 1)
				{
					List pos2List = new ArrayList();
					pos2List.add(ypos);
					pos1Map.put(xpos, pos2List);
					flag = 1;
				}
			}
		}
		if (flag != 1)
		{
			List pos2List = new ArrayList();
			pos2List.add(ypos);

			Map pos1Map = new TreeMap();
			pos1Map.put(xpos, pos2List);
			containerMap.put(parentId, pos1Map);

		}

	}

	private void setStorageType(HttpServletRequest request, StorageContainerForm storageContainerForm)
	{
		//*************Start Bug:1938  ForwardTo implementation *************
		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
		if (forwardToHashMap != null)
		{
			Long storageTypeId = (Long) forwardToHashMap.get("storageTypeId");
			Logger.out.debug("storageTypeId found in forwardToHashMap========>>>>>>" + storageTypeId);
			storageContainerForm.setTypeId(storageTypeId.longValue());
		}
		else
		{
			if (request.getParameter("storageTypeId") != null)
			{
				Long storageTypeId = new Long(request.getParameter("storageTypeId"));
				storageContainerForm.setTypeId(storageTypeId.longValue());
			}
		}
		//*************End Bug:1938 ForwardTo implementation *************

	}

	private void setCollectionProtocolList(HttpServletRequest request) throws DAOException
	{
		/*
		 * Bug #4564
		 * for tooltips
		 * kalpana
		 * 
		 */
		CollectionProtocolBizLogic bizLogic = (CollectionProtocolBizLogic) BizLogicFactory.getInstance().getBizLogic(
				Constants.COLLECTION_PROTOCOL_FORM_ID);
		String sourceObjectName = CollectionProtocol.class.getName();
		String[] selectColName = {"id", "shortTitle", "title"};
		String[] whereColName = {Constants.ACTIVITY_STATUS};
		String[] whereColCond = {"="};
		Object[] whereColVal = {Constants.ACTIVITY_STATUS_ACTIVE};
		//Smita changes start
		//String[] displayNameFields = {"shortTitle"};
		//Smita changes end
		//List collectionProtocolList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, true);
		List collectionProtocolList = bizLogic.retrieve(sourceObjectName, selectColName, whereColName, whereColCond, whereColVal,
				Constants.AND_JOIN_CONDITION);

		List<NameValueBean> cpWithShortTitleList = new ArrayList<NameValueBean>();
		Map<Long, String> cpIDTitleMap = new HashMap<Long, String>();
		if (collectionProtocolList != null)
		{
			Iterator itr = collectionProtocolList.iterator();
			while (itr.hasNext())
			{
				Object[] obj = (Object[]) itr.next();
				Long cpId = (Long) obj[0];
				String shortTitle = (String) obj[1];
				String title = (String) obj[2];
				cpWithShortTitleList.add(new NameValueBean(shortTitle, cpId));
				cpIDTitleMap.put(cpId, title);

			}
		}
		Collections.sort(cpWithShortTitleList);
		cpWithShortTitleList.add(0, new NameValueBean(Constants.SELECT_OPTION, "-1"));
		//List collectionProtocolList = Utility.getCollectionProtocolList(list1);
		request.setAttribute(Constants.PROTOCOL_LIST, cpWithShortTitleList);
		//Map<Long, String> cpIDTitleMap = Utility.getCPIDTitleMap();
		request.setAttribute(Constants.CP_ID_TITLE_MAP, cpIDTitleMap);
		/*
		 * End :kalpana
		 */

	}

	private void setRequestAttributes(HttpServletRequest request) throws DAOException
	{
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);
		//Sets the operation attribute to be used in the Add/Edit Institute Page. 
		request.setAttribute(Constants.OPERATION, operation);
		//Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		//Populating the Site Array
		String[] siteDisplayField = {"name"};
		String valueField = "id";
		List list = bizLogic.getList(Site.class.getName(), siteDisplayField, valueField, true);
		request.setAttribute(Constants.SITELIST, list);
		//get the Specimen class and type from the cde
		List specimenClassTypeList = Utility.getSpecimenClassTypeListWithAny();
		request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);
		//Gets the Specimen array Type List and sets it in request
		List list3 = bizLogic.retrieve(SpecimenArrayType.class.getName());
		List spArrayTypeList = Utility.getSpecimenArrayTypeList(list3);
		request.setAttribute(Constants.HOLDS_LIST3, spArrayTypeList);

		List list2 = bizLogic.retrieve(StorageType.class.getName());
		List storageTypeListWithAny = Utility.getStorageTypeList(list2, true);
		request.setAttribute(Constants.HOLDS_LIST1, storageTypeListWithAny);

		if (Constants.ADD.equals(request.getAttribute(Constants.OPERATION)))
		{
			List StorageTypeListWithoutAny = Utility.getStorageTypeList(list2, false);
			request.setAttribute(Constants.STORAGETYPELIST, StorageTypeListWithoutAny);
		}

		setCollectionProtocolList(request);
	}

	private void onTypeChange(StorageContainerForm storageContainerForm, String operation, HttpServletRequest request) throws DAOException
	{
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		long typeSelected = -1;
		String valueField = "id";
		String selectedType = String.valueOf(storageContainerForm.getTypeId());
		Logger.out.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> ST : " + selectedType);
		if (selectedType != null && !selectedType.equals("-1"))
		{

			typeSelected = Long.parseLong(selectedType);
			List list = bizLogic.retrieve(StorageType.class.getName(), valueField, new Long(typeSelected));
			if (!list.isEmpty())
			{
				StorageType type = (StorageType) list.get(0);
				//setFormAttributesForSelectedType(type,storageContainerForm);
				if (type.getDefaultTempratureInCentigrade() != null)
					storageContainerForm.setDefaultTemperature(type.getDefaultTempratureInCentigrade().toString());

				storageContainerForm.setOneDimensionCapacity(type.getCapacity().getOneDimensionCapacity().intValue());
				storageContainerForm.setTwoDimensionCapacity(type.getCapacity().getTwoDimensionCapacity().intValue());
				storageContainerForm.setOneDimensionLabel(type.getOneDimensionLabel());
				storageContainerForm.setTwoDimensionLabel(Utility.toString(type.getTwoDimensionLabel()));
				storageContainerForm.setTypeName(type.getName());

				if (type.getHoldsSpecimenClassCollection().size() > 0)
				{
					storageContainerForm.setSpecimenOrArrayType("Specimen");
				}
				Collection holdsSpArrayTypeCollection = (Collection) bizLogic.retrieveAttribute(StorageType.class.getName(), type.getId(),
						"elements(holdsSpecimenArrayTypeCollection)");
				type.setHoldsSpecimenArrayTypeCollection(holdsSpArrayTypeCollection);
				if (holdsSpArrayTypeCollection.size() > 0)
				{
					storageContainerForm.setSpecimenOrArrayType("SpecimenArray");
				}

				//type_name=type.getType();

				Logger.out.debug("Type Name:" + storageContainerForm.getTypeName());

				// If operation is add opeartion then set the holds list according to storage type selected.
				if (operation != null && operation.equals(Constants.ADD))
				{
					StorageTypeBizLogic storageTypebizLogic = (StorageTypeBizLogic) BizLogicFactory.getInstance().getBizLogic(
							Constants.STORAGE_TYPE_FORM_ID);
					long[] defHoldsStorageTypeList = storageTypebizLogic.getDefaultHoldStorageTypeList(type);
					if (defHoldsStorageTypeList != null)
					{
						storageContainerForm.setHoldsStorageTypeIds(defHoldsStorageTypeList);
					}

					String[] defHoldsSpecimenClassTypeList = storageTypebizLogic.getDefaultHoldsSpecimenClasstypeList(type);
					if (defHoldsSpecimenClassTypeList != null)
					{
						storageContainerForm.setHoldsSpecimenClassTypes(defHoldsSpecimenClassTypeList);
					}
					
					long[] defHoldsSpecimenArrayTypeList = storageTypebizLogic.getDefaultHoldSpecimenArrayTypeList(type);
					if (defHoldsSpecimenArrayTypeList != null)
					{
						storageContainerForm.setHoldsSpecimenArrTypeIds(defHoldsSpecimenArrayTypeList);
					}
				}
			}

		}
		else
		{
			request.setAttribute("storageType", null);
			storageContainerForm.setDefaultTemperature("");
			storageContainerForm.setOneDimensionCapacity(0);
			storageContainerForm.setTwoDimensionCapacity(0);
			storageContainerForm.setOneDimensionLabel("Dimension One");
			storageContainerForm.setTwoDimensionLabel("Dimension Two");
			storageContainerForm.setTypeName("");
			//type_name="";
		}

	}

	private void SetParentStorageContainersForAdd(TreeMap containerMap, StorageContainerForm storageContainerForm, HttpServletRequest request)
			throws DAOException
	{
		Vector initialValues = null;
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		initialValues = checkForInitialValues(containerMap);
		if (initialValues != null)
		{
			//Getting the default values in add case
			String[] initValues = new String[3];
			initValues = (String[]) initialValues.get(0);

			//getting collection protocol list and name of the container for default selected parent container
			String valueField = "id";
			List containerList = bizLogic.retrieve(StorageContainer.class.getName(), valueField, new Long(initValues[0]));
			if (!containerList.isEmpty())
			{
				if (storageContainerForm.getCheckedButton() == 2 && storageContainerForm.getStContSelection() == 1)
				{
					StorageContainer container = (StorageContainer) containerList.get(0);
					storageContainerForm.setCollectionIds(bizLogic.getDefaultHoldCollectionProtocolList(container));
				}
				else
				{
					storageContainerForm.setCollectionIds(new long[]{-1});
				}
			}

		}
		request.setAttribute("initValues", initialValues);
	}

	private void SetParentStorageCOntainersForEdit(TreeMap containerMap, StorageContainerForm storageContainerForm, HttpServletRequest request)
			throws DAOException
	{
		Vector initialValues = null;
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		if (storageContainerForm.getCheckedButton() == 2)
		{
			String[] startingPoints = new String[]{"-1", "-1", "-1"};

			String valueField = "id";
			List containerList = bizLogic.retrieve(StorageContainer.class.getName(), valueField, new Long(storageContainerForm.getId()));
			if (!containerList.isEmpty())
			{
				StorageContainer cont = (StorageContainer) containerList.get(0);

				Container parent = (Container) bizLogic.retrieveAttribute(StorageContainer.class.getName(), cont.getId(), "parent");
				if (parent != null)
				{
					Long id = parent.getId();
					Integer pos1 = cont.getPositionDimensionOne();
					Integer pos2 = cont.getPositionDimensionTwo();
					String parentContainerName = parent.getName();

					addPostions(containerMap, id, parentContainerName, pos1, pos2);
				}
			}
			if (storageContainerForm.getParentContainerId() != -1)
			{
				startingPoints[0] = new Long(storageContainerForm.getParentContainerId()).toString();
			}
			if (storageContainerForm.getPositionDimensionOne() != -1)
			{
				startingPoints[1] = new Integer(storageContainerForm.getPositionDimensionOne()).toString();
			}
			if (storageContainerForm.getPositionDimensionTwo() != -1)
			{
				startingPoints[2] = new Integer(storageContainerForm.getPositionDimensionTwo()).toString();
			}

			initialValues = new Vector();
			initialValues.add(startingPoints);
		}
		else if (storageContainerForm.getCheckedButton() == 1)
		{

			initialValues = checkForInitialValues(containerMap);

			//falguni
			//get container name by getting storage container object from db.
			if (storageContainerForm.getContainerName() == null)
			{
				String valueField = "id";
				List containerList = bizLogic.retrieve(StorageContainer.class.getName(), valueField, new Long(storageContainerForm.getId()));
				if (!containerList.isEmpty())
				{
					StorageContainer cont = (StorageContainer) containerList.get(0);
					storageContainerForm.setContainerName(cont.getName());
				}
			}

		}
		request.setAttribute("initValues", initialValues);
	}

	private void setFormAttributesForAddNew(HttpServletRequest request, StorageContainerForm storageContainerForm)
	{
		// Mandar : code for Addnew Storage Type data 23-Jan-06
		String storageTypeID = (String) request.getAttribute(Constants.ADD_NEW_STORAGE_TYPE_ID);
		if (storageTypeID != null && storageTypeID.trim().length() > 0)
		{
			Logger.out.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> ST : " + storageTypeID);
			storageContainerForm.setTypeId(Long.parseLong(storageTypeID));
		}
		// -- 23-Jan-06 end
		// Mandar : code for Addnew Site data 24-Jan-06
		String siteID = (String) request.getAttribute(Constants.ADD_NEW_SITE_ID);
		if (siteID != null && siteID.trim().length() > 0)
		{
			Logger.out.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> ToSite ID in Distribution Action : " + siteID);
			storageContainerForm.setSiteId(Long.parseLong(siteID));
		}

	}

	private void onSiteOrParentContChange(HttpServletRequest request, StorageContainerForm storageContainerForm) throws DAOException
	{

		if (storageContainerForm.getCheckedButton() == 2)
		{
			String[] startingPoints = new String[]{"-1", "-1", "-1"};

			if (request.getParameter("parentContainerId") != null)
			{
				startingPoints[0] = request.getParameter("parentContainerId");
			}
			if (request.getParameter("positionDimensionOne") != null)
			{
				startingPoints[1] = request.getParameter("positionDimensionOne");
			}
			if (request.getParameter("positionDimensionTwo") != null)
			{
				startingPoints[2] = request.getParameter("positionDimensionTwo");
			}

			Vector initialValues = new Vector();
			initialValues.add(startingPoints);
			request.setAttribute("initValues", initialValues);
		}
		parentContChange(request, storageContainerForm);
	}

	private void parentContChange(HttpServletRequest request, StorageContainerForm storageContainerForm) throws DAOException
	{
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		String valueField = "id";
		String parentContId = request.getParameter("parentContainerId");
		if (parentContId != null)
		{
			List list = bizLogic.retrieve(StorageContainer.class.getName(), valueField, new Long(parentContId));
			if (!list.isEmpty())
			{
				StorageContainer container = (StorageContainer) list.get(0);
				//site_name=container.getSite().getName();

				//Site site = (Site) bizLogic.retrieveAttribute(StorageContainer.class.getName(), container.getId(), "site");//container.getSite();
				//storageContainerForm.setSiteName(site.getName());
				storageContainerForm.setCollectionIds(bizLogic.getDefaultHoldCollectionProtocolList(container));
			}
		}
	}
}