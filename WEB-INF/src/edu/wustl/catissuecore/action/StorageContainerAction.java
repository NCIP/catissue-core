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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
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
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.MapDataParser;
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
		Logger.out.info("Add New Attribute in StorageContainerAction:" + request.getAttribute(Constants.SUBMITTED_FOR));
		//List of keys used in map of ActionForm
		List key = new ArrayList();
		key.add("StorageContainerDetails:i_parameterName");
		key.add("StorageContainerDetails:i_parameterValue");

		//Gets the map from ActionForm
		Map map = storageContainerForm.getValues();
		//Calling DeleteRow of BaseAction class
		MapDataParser.deleteRow(key, map, request.getParameter("status"));

		boolean isOnChange = false;
		boolean isTypeChange = false;

		boolean isSiteOrParentContainerChange = false;

		String str = request.getParameter("isOnChange");

		if (str != null && str.equals("true"))
		{
			isOnChange = true;
		}

		str = request.getParameter("isNameChange");

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
		Logger.out.info("Onchange parameter in StorageContainerAction:" + isOnChange);

		//Gets the value of the operation parameter.
		String operation = request.getParameter(Constants.OPERATION);

		//Sets the operation attribute to be used in the Add/Edit Institute Page. 
		request.setAttribute(Constants.OPERATION, operation);

		//Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		//Sets the isContainerFullList attribute to be used in the StorageContainer Add/Edit Page.

		//request.setAttribute(Constants.IS_CONTAINER_FULL_LIST, Constants.IS_CONTAINER_FULL_VALUES );

		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

		long container_number = bizLogic.getNextContainerNumber();
		if(operation.equals(Constants.EDIT))
		{
			container_number = storageContainerForm.getId();
		}
		request.setAttribute("ContainerNumber", new Long(container_number).toString());

		Logger.out.info("is container full:" + storageContainerForm.getIsFull());

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

		//    	 ---- chetan 15-06-06 ----

		TreeMap containerMap = new TreeMap();
		List mapSiteList = new ArrayList();
		List siteList = new ArrayList();
		if (storageContainerForm.getTypeId() != -1)
		{
			mapSiteList = bizLogic.getAllocatedContaienrMapForContainer(storageContainerForm.getTypeId(),exceedingMaxLimit,null);
			containerMap = (TreeMap) mapSiteList.get(0);
			siteList = (List) mapSiteList.get(1);

		}
		Vector initialValues = null;
		if (operation.equals(Constants.ADD))
		{

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
					if(storageContainerForm.getCheckedButton() == 2 && storageContainerForm.getStContSelection() == 1)
					{
					StorageContainer container = (StorageContainer) containerList.get(0);
					storageContainerForm.setCollectionIds(getDefaultHoldCPList(container));
					}     
					else
					{
						storageContainerForm.setCollectionIds(new long[]{-1});
					}
				}
				if (storageContainerForm.getContainerName().equals(""))
				{
					storageContainerForm.setContainerName(bizLogic.getContainerName(storageContainerForm.getSiteName(), storageContainerForm
							.getTypeName(), operation, new Long(initValues[0]).longValue()));

				}
			}

		}
		if ((operation.equals(Constants.EDIT) || isSiteOrParentContainerChange) && storageContainerForm.getCheckedButton() == 2)
		{
			String[] startingPoints = new String[]{"-1", "-1", "-1"};

			if (operation.equals(Constants.EDIT))
			{
				
				String valueField = "id";
				List containerList = bizLogic.retrieve(StorageContainer.class.getName(), valueField, new Long(storageContainerForm.getId()));
				if (!containerList.isEmpty())
				{
					StorageContainer cont = (StorageContainer) containerList.get(0);

					if (cont.getParent() != null)
					{
						Long id = cont.getParent().getId();
						Integer pos1 = cont.getPositionDimensionOne();
						Integer pos2 = cont.getPositionDimensionTwo();

						String parentContainerName = cont.getParent().getName();

						/*List containerList1 = bizLogic.retrieve(StorageContainer.class.getName(), valueField, cont.getParent().getId());
						if (!containerList1.isEmpty())
						{
							StorageContainer container = (StorageContainer) containerList1.get(0);
							parentContainerName = container.getName();

						}*/
						Logger.out.info("-------------ParentcontainerId:" + id);
						Logger.out.info("-------------Pos1:" + pos1);
						Logger.out.info("-------------Pos2:" + pos2);
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
			}
			if (isSiteOrParentContainerChange)
			{
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
			}
			initialValues = new Vector();
			Logger.out.info("Starting points[0]" + startingPoints[0]);
			Logger.out.info("Starting points[1]" + startingPoints[1]);
			Logger.out.info("Starting points[2]" + startingPoints[2]);
			initialValues.add(startingPoints);
		}

		if (operation.equals(Constants.EDIT) && storageContainerForm.getCheckedButton() == 1)
		{
			initialValues = checkForInitialValues(containerMap);

		}
		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT,exceedingMaxLimit);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		request.setAttribute("siteForParentList", siteList);
		request.setAttribute("initValues", initialValues);

		//Populating the Site Array
		String[] siteDisplayField = {"name"};
		String valueField = "id";
		List list = bizLogic.getList(Site.class.getName(), siteDisplayField, valueField, true);
		request.setAttribute(Constants.SITELIST, list);

		//populating collection protocol list.
		List list1 = bizLogic.retrieve(CollectionProtocol.class.getName());
		List collectionProtocolList = Utility.getCollectionProtocolList(list1);
		request.setAttribute(Constants.PROTOCOL_LIST, collectionProtocolList);

		//Gets the Storage Type List and sets it in request 
		List list2 = bizLogic.retrieve(StorageType.class.getName());
		List storageTypeListWithAny = Utility.getStorageTypeList(list2, true);
		request.setAttribute(Constants.HOLDS_LIST1, storageTypeListWithAny);

		if (operation.equals(Constants.ADD))
		{
			List StorageTypeListWithoutAny = Utility.getStorageTypeList(list2, false);
			request.setAttribute(Constants.STORAGETYPELIST, StorageTypeListWithoutAny);
		}
		else
		{
			/*if (bizLogic.isContainerFull(new Long(storageContainerForm.getId()).toString(),
					storageContainerForm.getOneDimensionCapacity()+1,storageContainerForm.getTwoDimensionCapacity()+1))
			{
				storageContainerForm.setIsFull("true");
			}*/
			
			if (StorageContainerUtil.chkContainerFull(new Long(storageContainerForm.getId()).toString(),storageContainerForm.getContainerName()))
			{
				storageContainerForm.setIsFull("true");
			}

			List storagetypeList = new ArrayList();
			NameValueBean nvb = new NameValueBean(storageContainerForm.getTypeName(), new Long(storageContainerForm.getTypeId()));
			storagetypeList.add(nvb);
			request.setAttribute(Constants.STORAGETYPELIST, storagetypeList);
		}

		// get the Specimen class and type from the cde
		List specimenClassTypeList = Utility.getSpecimenClassTypeListWithAny();
		request.setAttribute(Constants.HOLDS_LIST2, specimenClassTypeList);

		//Gets the Specimen array Type List and sets it in request
		List list3 = bizLogic.retrieve(SpecimenArrayType.class.getName());
		List spArrayTypeList = Utility.getSpecimenArrayTypeList(list3);
		request.setAttribute(Constants.HOLDS_LIST3, spArrayTypeList);

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
		// -- 24-Jan-06 end

		if (isTypeChange || request.getAttribute(Constants.SUBMITTED_FOR) != null)
		{
			long typeSelected = -1;
			String selectedType = String.valueOf(storageContainerForm.getTypeId());
			Logger.out.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> ST : " + selectedType);
			if (selectedType != null && !selectedType.equals("-1"))
			{

				typeSelected = Long.parseLong(selectedType);
				list = bizLogic.retrieve(StorageType.class.getName(), valueField, new Long(typeSelected));
				if (!list.isEmpty())
				{
					StorageType type = (StorageType) list.get(0);
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
					if (type.getHoldsSpecimenArrayTypeCollection().size() > 0)
					{
						storageContainerForm.setSpecimenOrArrayType("SpecimenArray");
					}

					//type_name=type.getType();

					Logger.out.debug("Type Name:" + storageContainerForm.getTypeName());

					// If operation is add opeartion then set the holds list according to storage type selected.
					if (operation != null && operation.equals(Constants.ADD))
					{
						long[] defHoldsStorageTypeList = getDefaultHoldStorageTypeList(type);
						if (defHoldsStorageTypeList != null)
						{
							storageContainerForm.setHoldsStorageTypeIds(defHoldsStorageTypeList);
						}

						String[] defHoldsSpecimenClassTypeList = getDefaultHoldsSpecimenClasstypeList(type);
						if (defHoldsSpecimenClassTypeList != null)
						{
							storageContainerForm.setHoldsSpecimenClassTypes(defHoldsSpecimenClassTypeList);
						}
						for (int i = 0; i < storageContainerForm.getHoldsSpecimenClassTypes().length; i++)
						{
							Logger.out.info("Specimen class in form:" + storageContainerForm.getHoldsSpecimenClassTypes()[i]);
						}
						long[] defHoldsSpecimenArrayTypeList = getDefaultHoldSpecimenArrayTypeList(type);
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

		if (isSiteOrParentContainerChange || request.getAttribute(Constants.SUBMITTED_FOR) != null
				&& storageContainerForm.getContainerName().equals(""))
		{

			if (storageContainerForm.getCheckedButton() == 1)
			{
				Logger.out.debug("storageContainerForm.getSiteId()......................." + storageContainerForm.getSiteId());
				Logger.out.debug("storageContainerForm.getTypeId()......................." + storageContainerForm.getTypeId());
				list = bizLogic.retrieve(Site.class.getName(), valueField, new Long(storageContainerForm.getSiteId()));
				if (!list.isEmpty())
				{
					Site site = (Site) list.get(0);
					storageContainerForm.setSiteName(site.getName());
					//site_name=site.getName();
					Logger.out.debug("Site Name :" + storageContainerForm.getSiteName());
				}
			}
			else
			{
				Logger.out.debug("Long.parseLong(request.getParameter(parentContainerId)......................."
						+ request.getParameter("parentContainerId"));
				Logger.out.debug("storageContainerForm.getTypeId()......................." + storageContainerForm.getTypeId());
				String parentContId = request.getParameter("parentContainerId");
				if (parentContId != null)
				{
					list = bizLogic.retrieve(StorageContainer.class.getName(), valueField, new Long(parentContId));
					if (!list.isEmpty())
					{
						StorageContainer container = (StorageContainer) list.get(0);
						//site_name=container.getSite().getName();
						storageContainerForm.setSiteName(container.getSite().getName());
						storageContainerForm.setCollectionIds(getDefaultHoldCPList(container));
						Logger.out.debug("Site Name :" + storageContainerForm.getSiteName());
					}
				}
			}
		}
		Logger.out.info("Container name:" + storageContainerForm.getContainerName());
		Logger.out.info("Site Name:" + storageContainerForm.getSiteName());
		Logger.out.info("type:" + storageContainerForm.getTypeName());
		if (storageContainerForm.getContainerName().equals(""))
		{
			storageContainerForm.setContainerName(bizLogic.getContainerName(storageContainerForm.getSiteName(), storageContainerForm.getTypeName(),
					operation, storageContainerForm.getId()));

		}

		// ---------- Add new
		String reqPath = request.getParameter(Constants.REQ_PATH);

		if (reqPath != null)
		{
			request.setAttribute(Constants.REQ_PATH, reqPath);
		}

		Logger.out.info("storagecontainer cp:" + storageContainerForm.getCollectionIds().length);
		return mapping.findForward((String) request.getParameter(Constants.PAGEOF));
	}

	/* this function finds out the storage type holds list for a storage type given 
	 * and sets the container's storage type holds list
	 * */
	private long[] getDefaultHoldStorageTypeList(StorageType type)
	{
		//Populating the storage type-id array

		Logger.out.info("Storage type size:" + type.getHoldsStorageTypeCollection().size());
		Collection storageTypeCollection = type.getHoldsStorageTypeCollection();

		if (storageTypeCollection != null)
		{
			long holdsStorageTypeList[] = new long[storageTypeCollection.size()];
			int i = 0;
			Iterator it = storageTypeCollection.iterator();
			while (it.hasNext())
			{
				StorageType holdStorageType = (StorageType) it.next();
				holdsStorageTypeList[i] = holdStorageType.getId().longValue();
				i++;
			}
			return holdsStorageTypeList;
		}
		return null;
	}

	/* this function finds out the specimen class holds list for a storage type given 
	 * and sets the container's specimen class holds list
	 * */
	private String[] getDefaultHoldsSpecimenClasstypeList(StorageType type)
	{
		String[] holdsSpecimenClassList = null;
		//Populating the specimen class type-id array
		Logger.out.info("Specimen class type size:" + type.getHoldsSpecimenClassCollection().size());
		Collection specimenClassTypeCollection = type.getHoldsSpecimenClassCollection();

		if (specimenClassTypeCollection != null)
		{
			if (specimenClassTypeCollection.size() == Utility.getSpecimenClassTypes().size())
			{
				holdsSpecimenClassList = new String[1];
				holdsSpecimenClassList[0] = "-1";
			}
			else
			{
				holdsSpecimenClassList = new String[specimenClassTypeCollection.size()];
				int i = 0;

				Iterator it = specimenClassTypeCollection.iterator();
				while (it.hasNext())
				{
					String specimenClassType = (String) it.next();
					Logger.out.info("specimen class type:" + specimenClassType);
					holdsSpecimenClassList[i] = specimenClassType;
					i++;
				}
			}
			return holdsSpecimenClassList;

		}
		return null;
	}

	/* this function finds out the specimen array type holds list for a storage type given 
	 * and sets the container's storage type holds list
	 * */
	private long[] getDefaultHoldSpecimenArrayTypeList(StorageType type)
	{
		//Populating the storage type-id array

		Logger.out.info("Storage type size:" + type.getHoldsSpecimenArrayTypeCollection().size());
		Collection spcimenArrayTypeCollection = type.getHoldsSpecimenArrayTypeCollection();

		if (spcimenArrayTypeCollection != null)
		{
			long holdsSpecimenArrayTypeList[] = new long[spcimenArrayTypeCollection.size()];
			int i = 0;
			Iterator it = spcimenArrayTypeCollection.iterator();
			while (it.hasNext())
			{
				SpecimenArrayType holdSpArrayType = (SpecimenArrayType) it.next();
				holdsSpecimenArrayTypeList[i] = holdSpArrayType.getId().longValue();
				i++;
			}
			return holdsSpecimenArrayTypeList;
		}
		return null;
	}

	/* this function finds out the collection protocol holds list for a storage container given 
	 * and sets the container's collection protocol holds list
	 * */
	private long[] getDefaultHoldCPList(StorageContainer container)
	{
		//Populating the storage type-id array
		Logger.out.info("---------------- container Id :" + container.getId());
		Collection cpCollection = container.getCollectionProtocolCollection();

		if (cpCollection != null && cpCollection.size() > 0)
		{
			long holdsCPList[] = new long[cpCollection.size()];
			int i = 0;
			Iterator it = cpCollection.iterator();
			while (it.hasNext())
			{
				CollectionProtocol cp = (CollectionProtocol) it.next();
				holdsCPList[i] = cp.getId().longValue();
				Logger.out.info("----------holdsCPList[" + i + "]=" + holdsCPList[i]);
				i++;
			}
			Logger.out.info("holdsCPList:" + holdsCPList);
			return holdsCPList;
		}
		else
		{
			long holdsCPList[] = new long[]{-1};

			Logger.out.info("holdsCPList size:" + holdsCPList.length);

			return holdsCPList;
		}

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

		//request.setAttribute("initValues", initialValues);
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

}