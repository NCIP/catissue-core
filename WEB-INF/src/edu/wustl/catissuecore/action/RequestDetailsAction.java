/**
 * <p>Title: RequestDetailsAction Class>
 * <p>Description:	This class initializes the fields of RequestDetails.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Oct 05,2006
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.RequestDetailsForm;
import edu.wustl.catissuecore.bean.DefinedArrayDetailsBean;
import edu.wustl.catissuecore.bean.DefinedArrayRequestBean;
import edu.wustl.catissuecore.bean.ExistingArrayDetailsBean;
import edu.wustl.catissuecore.bean.RequestDetailsBean;
import edu.wustl.catissuecore.bean.RequestViewBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.NewSpecimenOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.OrderingSystemUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.dbManager.DAOException;

public class RequestDetailsAction extends BaseAction
{
	/**
	 * Overrides the execute method of Action class.
	 * Initializes the various fields in RequestDetails.jsp Page.
	 * @param mapping object
	 * @param form object
	 * @param request object
	 * @param response object
	 * @return ActionForward object
	 * @throws Exception object
	 * */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		RequestDetailsForm requestDetailsForm = (RequestDetailsForm) form;
		// The request Id on which the user has clicked
		String requestId = "";
		if (request.getParameter("id") != null && !request.getParameter("id").equals("0"))
		{
			requestId = request.getParameter("id"); //		
			//Setting the order id in the form to retrieve the corresponding orderitems from the db in CommonAddEditAction.
			requestDetailsForm.setId((new Long(requestId)).longValue());
		}
		else
		// while returning from specimen page
		{
			Object obj = request.getSession().getAttribute("REQUEST_DETAILS_FORM");
			RequestDetailsForm rDForm = null;
			if (obj != null)
				rDForm = (RequestDetailsForm) obj;
			requestId = "" + rDForm.getId();
			requestDetailsForm.setId((new Long(requestId)).longValue());
			
		}

		// The request details  corresponding to the request Id
		RequestViewBean requestListBean = getRequestObject(requestId);
		request.setAttribute(Constants.REQUEST_HEADER_OBJECT, requestListBean);

		// order items status to display
		List requestedItemsStatusList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_REQUESTED_ITEMS_STATUS, null);
		request.setAttribute(Constants.REQUESTED_ITEMS_STATUS_LIST, requestedItemsStatusList);

		//Setting the Site List
		List siteList = getSiteListToDisplay();
		request.setAttribute(Constants.SITE_LIST_OBJECT, siteList);

		//setting Item Status List in Request
		request.setAttribute(Constants.ITEM_STATUS_LIST, OrderingSystemUtil.getPossibleStatusList(Constants.ORDER_REQUEST_STATUS_NEW));

		//setting Items status list without "Distributed" option.
		List tempList = OrderingSystemUtil.getPossibleStatusList(Constants.ORDER_REQUEST_STATUS_NEW);
		Iterator tempListIter = tempList.iterator();
		while (tempListIter.hasNext())
		{
			NameValueBean nameValueBean = (NameValueBean) tempListIter.next();
			if (nameValueBean.getValue().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED))
			{
				tempList.remove(nameValueBean);
				tempList.add(new NameValueBean(Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION,
						Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION));
				break;
			}
		}
		//For orderitems in defined array, "Ready for array preparation" status is present instead of "Distribute"		
		request.setAttribute(Constants.ITEM_STATUS_LIST_FOR_ITEMS_IN_ARRAY, tempList);

		//The order items list corresponding to the request Id
		getRequestDetailsList(requestId, requestDetailsForm, request);

		if(request.getParameter(Constants.TAB_INDEX_ID)!=null)
		{
			Integer tabIndexId = new Integer(request.getParameter(Constants.TAB_INDEX_ID));
			requestDetailsForm.setTabIndex(tabIndexId.intValue());
		}
		return mapping.findForward("success");
	}

	/**
	 * This function constructs lists of RequestDetails bean objects,map of definedarrays and ExistingArrayDetails bean instances to display
	 * on RequestDetails.jsp and ArrayRequests.jsp by setting all the lists in HttpServletRequest object.
	 * @param id String containing the requestId
	 * @param requestDetailsForm RequestDetailsForm object
	 * @param request HttpServletRequest object
	 * @throws DAOException object
	 */
	private void getRequestDetailsList(String id, RequestDetailsForm requestDetailsForm, HttpServletRequest request) throws DAOException
	{
		//fetching the order object corresponding to obtained id.
		List orderList = getOrderListFromDB(id);
		OrderDetails order = (OrderDetails) orderList.get(0);

		Collection orderItemsListFromDB = order.getOrderItemCollection();
		getDetailedDataForOrderItemCollection(orderItemsListFromDB);

		//populating Map in the form only on loading. If error is present, use form present in request.
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (errors == null)
		{//Showing specimen tab by default.
			requestDetailsForm.setTabIndex(1);
			requestDetailsForm.setAllValuesForOrder(order, request);
		}
		else
		{
			populateRequestForMap(requestDetailsForm);
		}

		List requestDetailsList = new ArrayList();
		OrderItem orderItem = null;
		//List containing the list of defined arrays and existing arrays  
		List arrayRequestDetailsList = new ArrayList();
		List arrayRequestDetailsMapList = new ArrayList();
		if ((orderItemsListFromDB != null) && (orderItemsListFromDB.isEmpty() == false))
		{
			Iterator iter = orderItemsListFromDB.iterator();
			while (iter.hasNext())
			{
				orderItem = (OrderItem) iter.next();
				if (orderItem instanceof SpecimenOrderItem)
				{
					requestDetailsList = populateRequestDetailsListForSpecimenOrderItems(orderItem, request, requestDetailsList);
				}
				//In case of Defined Array
				if (orderItem instanceof NewSpecimenArrayOrderItem)
				{
					Map arrayMap = new HashMap();
					NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) orderItem;
					arrayMap = populateArrayMap(newSpecimenArrayOrderItem);
					//Add defined-array list into the arrayRequestDetails list 
					arrayRequestDetailsMapList.add(arrayMap);
				}
				//In case of Existing BioSpecimen Array Items
				if (orderItem instanceof ExistingSpecimenArrayOrderItem)
				{
					ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem) orderItem;
					ExistingArrayDetailsBean existingArrayDetailsBean = populateExistingArrayItemsList(existingSpecimenArrayOrderItem);
					//Add existingArrayBEan list into arrayRequestDetails list
					arrayRequestDetailsList.add(existingArrayDetailsBean);
				}
			}//End while
		}
		//Call to populateItemStatusList() when order items present in OrderList
		if (requestDetailsList.size() > 0)
		{
			populateItemStatusList(requestDetailsList, request, "addToOrderList");
		}
		//Call to populateItemStatusList() when order items are added to Defined Array
		if (arrayRequestDetailsMapList.size() > 0)
		{
			populateItemStatusList(arrayRequestDetailsMapList, request, "addToDefinedArrays");
		}
		//Call to populateItemStatusList() when order items are added to Existing Array
		if (arrayRequestDetailsList.size() > 0)
		{
			populateItemStatusList(arrayRequestDetailsList, request, "addToExistingArrays");
		}
	}

	/**
	 * @param requestDetailsForm
	 * @param orderItemsListFromDB
	 */
	private void populateRequestForMap(RequestDetailsForm requestDetailsForm) throws DAOException
	{
		Map valuesMap = requestDetailsForm.getValues();
		Set keySet = valuesMap.keySet();
		Iterator iter = keySet.iterator();
		Map requestForMap = new HashMap();
		while (iter.hasNext())
		{
			String specimenId = "";
			String rowNumber = "";
			String orderItemId = "";
			String specimenCollGrpId = "";

			String key = (String) iter.next();
			if (key.endsWith("orderItemId"))
			{
				rowNumber = getRowNumber(key);
				//				Fetching the order item id
				orderItemId = (String) valuesMap.get(key);
				List orderItemListFromDb = getOrderItemFromDB(orderItemId);
				OrderItem orderItem = (OrderItem) orderItemListFromDb.get(0);

				Iterator specimenIdIter = keySet.iterator();
				while (specimenIdIter.hasNext())
				{
					String specimenIdKey = (String) specimenIdIter.next();

					if ((specimenIdKey.endsWith(rowNumber + "_specimenId")) && !key.startsWith("DefinedArrayRequestBean")
							&& !key.startsWith("ExistingArrayDetailsBean") && !(orderItem instanceof ExistingSpecimenOrderItem)
							&& !(orderItem instanceof PathologicalCaseOrderItem))
					{
						specimenId = (String) valuesMap.get(specimenIdKey);
						break;
					}
					else if ((specimenIdKey.endsWith(rowNumber + "_specimenCollGroupId")) && !key.startsWith("DefinedArrayRequestBean")
							&& !key.startsWith("ExistingArrayDetailsBean") && !(orderItem instanceof ExistingSpecimenOrderItem)
							&& !(orderItem instanceof DerivedSpecimenOrderItem))
					{
						specimenCollGrpId = (String) valuesMap.get(specimenIdKey);
						break;
					}
				}
				if (!specimenId.equals(""))
				{
					List finalChildrenSpecimenList = new ArrayList();
					//Fetching the specimen object from db.
					List specimenList = getSpecimenFromDB(specimenId);
					Specimen specimen = (Specimen) specimenList.get(0);
					getChildrenSpecimen(specimen);
					Collection childrenSpecimenList = OrderingSystemUtil.getAllChildrenSpecimen(specimen, specimen.getChildrenSpecimen());

					if (orderItem instanceof DerivedSpecimenOrderItem)
					{
						DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) orderItem;
						finalChildrenSpecimenList = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childrenSpecimenList,
								derivedSpecimenOrderItem.getSpecimenClass(), derivedSpecimenOrderItem.getSpecimenType());
					}
					else if (orderItem instanceof PathologicalCaseOrderItem)
					{
						PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) orderItem;
						finalChildrenSpecimenList = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childrenSpecimenList,
								pathologicalCaseOrderItem.getSpecimenClass(), pathologicalCaseOrderItem.getSpecimenType());
					}
					List childrenSpecimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(finalChildrenSpecimenList);
					if ((orderItem instanceof SpecimenOrderItem) && !(orderItem instanceof ExistingSpecimenOrderItem))
					{
						SpecimenOrderItem specimenOrderItem = (SpecimenOrderItem) orderItem;
						if (specimenOrderItem.getNewSpecimenArrayOrderItem() == null)
						{
							requestForMap.put("RequestForDropDownList:" + rowNumber, childrenSpecimenListToDisplay);
						}
						else
						{
							requestForMap.put("RequestForDropDownListArray:" + rowNumber, childrenSpecimenListToDisplay);
						}
					}
				}
				else if (!specimenCollGrpId.equals(""))
				{
					if (orderItem instanceof PathologicalCaseOrderItem)
					{
						List scgList = getSpecimenCollGrpFromDB(specimenCollGrpId);
						// Fetching the SCG from db
						SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup) scgList.get(0);
						
						/* code for getting specimen list and there child specimens for particular Specimen Collection Group */
						OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
						Collection specimenList = (Collection)orderBizLogic.retrieveAttribute(SpecimenCollectionGroup.class.getName(),specimenCollectionGroup.getId(),"elements(specimenCollection)");
						if(specimenList != null)
						{
							specimenCollectionGroup.setSpecimenCollection(specimenList);
							if(specimenList != null)
							{
								Iterator itr = specimenList.iterator();
								while(itr.hasNext())
								{
									Specimen specimen = (Specimen)itr.next();
									getChildrenSpecimen(specimen);
								}
							}
						}
						/* chnages finsh */
						PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) orderItem;
						List totalChildrenSpecimenColl = OrderingSystemUtil.getRequestForListForPathologicalCases(specimenCollectionGroup,
								pathologicalCaseOrderItem);
						List childrenSpecimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(totalChildrenSpecimenColl);
						SpecimenOrderItem specimenOrderItem = (SpecimenOrderItem) orderItem;
						if (specimenOrderItem.getNewSpecimenArrayOrderItem() == null)
						{
							requestForMap.put("RequestForDropDownList:" + rowNumber, childrenSpecimenListToDisplay);
						}
						else
						{
							requestForMap.put("RequestForDropDownListArray:" + rowNumber, childrenSpecimenListToDisplay);
						}
					}
				}
			}
		}
		requestDetailsForm.setRequestForDropDownMap(requestForMap);
	}

	/**
	 * @param str
	 * @return
	 */
	private String getRowNumber(String str)
	{
		StringTokenizer stringTokenizer = new StringTokenizer(str, "_");
		String firstToken = stringTokenizer.nextToken();
		int indexOfColon = firstToken.indexOf(":");
		String rowNumber = firstToken.substring(indexOfColon + 1);
		return rowNumber;
	}

	/**
	 * @param orderItem
	 * @param request
	 * @return
	 * @throws DAOException 
	 */
	private List populateRequestDetailsListForSpecimenOrderItems(OrderItem orderItem, HttpServletRequest request, List requestDetailsList) throws DAOException
	{
		// The row number to update available quantity on selecting the required specimen from 'request for' drop down.
		int finalSpecimenListId = 0;
		SpecimenOrderItem specimenOrderItem = (SpecimenOrderItem) orderItem;
		//Incase of order items in the Order List
		if (specimenOrderItem.getNewSpecimenArrayOrderItem() == null)
		{
			RequestDetailsBean requestDetailsBean = new RequestDetailsBean();
			if (orderItem instanceof ExistingSpecimenOrderItem)
			{
				ExistingSpecimenOrderItem existingSpecimenorderItem = (ExistingSpecimenOrderItem) orderItem;
				requestDetailsBean = populateRequestDetailsBeanForExistingSpecimen(requestDetailsBean, existingSpecimenorderItem);
				requestDetailsList.add(requestDetailsBean);
				finalSpecimenListId++;
			}
			else if (orderItem instanceof DerivedSpecimenOrderItem)
			{
				DerivedSpecimenOrderItem derivedSpecimenorderItem = (DerivedSpecimenOrderItem) orderItem;
				requestDetailsBean = populateRequestDetailsBeanForDerivedSpecimen(requestDetailsBean, derivedSpecimenorderItem, request,
						finalSpecimenListId);
				requestDetailsList.add(requestDetailsBean);
				finalSpecimenListId++;
			}
			else if (orderItem instanceof PathologicalCaseOrderItem)
			{
				PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) orderItem;
				requestDetailsBean = populateRequestDetailsBeanForPathologicalCase(requestDetailsBean, pathologicalCaseOrderItem, request,
						finalSpecimenListId);
				requestDetailsList.add(requestDetailsBean);
				finalSpecimenListId++;
			}
		}
		return requestDetailsList;
	}

	/**
	 * This function populates existingArrayDetailsList with the existing Bio Speicmen Array Information by fetching the data from
	 * ExistingSpecimenArrayOrderItem domain object.
	 * @param existingArrayDetailsBean ExistingArrayDetailsBean object
	 * @param existingSpecimenArrayOrderItem ExistingSpecimenArrayOrderItem object
	 * @return existingArrayDetailsBean ExistingArrayDetailsBean object
	 */
	private ExistingArrayDetailsBean populateExistingArrayItemsList(ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem)
	{
		ExistingArrayDetailsBean existingArrayDetailsBean = new ExistingArrayDetailsBean();
		existingArrayDetailsBean.setOrderItemId(existingSpecimenArrayOrderItem.getId().toString());
		existingArrayDetailsBean.setBioSpecimenArrayName(existingSpecimenArrayOrderItem.getSpecimenArray().getName());
		existingArrayDetailsBean.setDescription(existingSpecimenArrayOrderItem.getDescription());
		existingArrayDetailsBean.setAssignedStatus(existingSpecimenArrayOrderItem.getStatus());
		existingArrayDetailsBean.setArrayId(existingSpecimenArrayOrderItem.getSpecimenArray().getId().toString());
		existingArrayDetailsBean.setRequestedQuantity(existingSpecimenArrayOrderItem.getRequestedQuantity().getValue().toString());

		return existingArrayDetailsBean;
	}

	/**
	 * This function populates the arrayDetailsList with map objects.Each map contains arrayRequestBean object as teh key and list of order items
	 * (for that defined array) as the value.
	 * @param newSpecimenArrayOrderItem NewSpecimenArrayOrderItem object
	 * @return definedArrayMap Map object
	 * @throws DAOException object
	 */
	private Map populateArrayMap(NewSpecimenArrayOrderItem newSpecimenArrayOrderItem) throws DAOException
	{
		Map definedArrayMap = new HashMap();
		List arrayItemsList = new ArrayList();
		//Create new instance of ArrayRequestBean to save the name,type,dimensions of the defined array 
		DefinedArrayRequestBean arrayRequestBean = new DefinedArrayRequestBean();
		arrayRequestBean = populateArrayRequestBean(arrayRequestBean, newSpecimenArrayOrderItem);
		Collection specimenOrderItemCollection = newSpecimenArrayOrderItem.getSpecimenOrderItemCollection();
		//Calculating the condition to enable or disable "Create Array Button"
		String condition = OrderingSystemUtil.determineCreateArrayCondition(specimenOrderItemCollection);

		if (arrayRequestBean.getAssignedStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED))
		{
			condition = "true";
		}
		arrayRequestBean.setCreateArrayButtonDisabled(condition);
		Iterator specimenOrderItemCollectionItr = specimenOrderItemCollection.iterator();
		while (specimenOrderItemCollectionItr.hasNext())
		{
			SpecimenOrderItem specimenOrderItem = (SpecimenOrderItem) specimenOrderItemCollectionItr.next();
			DefinedArrayDetailsBean arrayDetailsBean = new DefinedArrayDetailsBean();
			if (specimenOrderItem instanceof ExistingSpecimenOrderItem)
			{
				arrayDetailsBean = populateExistingSpecimensForArrayDetails(arrayDetailsBean, specimenOrderItem);
			}
			else if (specimenOrderItem instanceof DerivedSpecimenOrderItem)
			{
				arrayDetailsBean = populateDerivedSpecimensForArrayDetails(arrayDetailsBean, specimenOrderItem);
			}
			else if (specimenOrderItem instanceof PathologicalCaseOrderItem)
			{
				getSpecimenCollectionGroup(specimenOrderItem);
				arrayDetailsBean = populatePathologicalCasesForArrayDetails(arrayDetailsBean, specimenOrderItem);
			}
			//arrayDetailsBean.setArrayRequestBean(arrayRequestBean);
			//Add all the arrayDetailsBean in the list
			arrayItemsList.add(arrayDetailsBean);
		}
		definedArrayMap.put(arrayRequestBean, arrayItemsList);

		//Return the list containing arrayDetailsBean instances
		return definedArrayMap;
	}

	/**
	 * @param arrayRequestBean object
	 * @param newSpecimenArrayOrderItem object
	 * @return DefinedArrayRequestBean object
	 */
	private DefinedArrayRequestBean populateArrayRequestBean(DefinedArrayRequestBean arrayRequestBean,
			NewSpecimenArrayOrderItem newSpecimenArrayOrderItem)
	{
		arrayRequestBean.setArrayName(newSpecimenArrayOrderItem.getName());
		arrayRequestBean.setArrayClass(newSpecimenArrayOrderItem.getSpecimenArrayType().getSpecimenClass());
		arrayRequestBean.setOneDimensionCapacity((newSpecimenArrayOrderItem.getSpecimenArrayType().getCapacity().getOneDimensionCapacity())
				.toString());
		arrayRequestBean.setTwoDimensionCapacity((newSpecimenArrayOrderItem.getSpecimenArrayType().getCapacity().getTwoDimensionCapacity())
				.toString());
		arrayRequestBean.setArrayType(newSpecimenArrayOrderItem.getSpecimenArrayType().getName());
		arrayRequestBean.setArrayTypeId(newSpecimenArrayOrderItem.getSpecimenArrayType().getId().toString());
		arrayRequestBean.setAssignedStatus(newSpecimenArrayOrderItem.getStatus());
		arrayRequestBean.setOrderItemId(newSpecimenArrayOrderItem.getId().toString());

		SpecimenArray specimenArrayObj = newSpecimenArrayOrderItem.getSpecimenArray();
		if (specimenArrayObj != null)
		{
			arrayRequestBean.setArrayId(specimenArrayObj.getId().toString());
		}
		//Populate status list of individual array
		List arrayStatusList = OrderingSystemUtil.getPossibleStatusList(Constants.ORDER_REQUEST_STATUS_NEW);
		arrayRequestBean.setArrayStatusList(arrayStatusList);
		return arrayRequestBean;
	}

	/**
	 * @param arrayDetailsBean object
	 * @param specimenOrderItem object
	 * @return DefinedArrayDetailsBean object
	 */
	private DefinedArrayDetailsBean populateExistingSpecimensForArrayDetails(DefinedArrayDetailsBean arrayDetailsBean,
			SpecimenOrderItem specimenOrderItem)
	{
		ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem) specimenOrderItem;
		arrayDetailsBean.setRequestedItem(existingSpecimenOrderItem.getSpecimen().getLabel());
		//Add empty list since it is the case of existing speicmen
		arrayDetailsBean.setSpecimenList(new ArrayList());
		arrayDetailsBean.setSpecimenId((existingSpecimenOrderItem.getSpecimen().getId()).toString());
		arrayDetailsBean.setRequestedQuantity(existingSpecimenOrderItem.getRequestedQuantity().getValue().toString());
		arrayDetailsBean.setAvailableQuantity(existingSpecimenOrderItem.getSpecimen().getAvailableQuantity().getValue().toString());
		arrayDetailsBean.setAssignedStatus(existingSpecimenOrderItem.getStatus());
		arrayDetailsBean.setClassName(existingSpecimenOrderItem.getSpecimen().getClassName());
		arrayDetailsBean.setType(existingSpecimenOrderItem.getSpecimen().getType());
		arrayDetailsBean.setDescription(existingSpecimenOrderItem.getDescription());
		arrayDetailsBean.setOrderItemId(existingSpecimenOrderItem.getId().toString());
		arrayDetailsBean.setInstanceOf("Existing");

		return arrayDetailsBean;
	}

	/**
	 * @param arrayDetailsBean object
	 * @param specimenOrderItem object
	 * @return DefinedArrayDetailsBean object
	 */
	private DefinedArrayDetailsBean populateDerivedSpecimensForArrayDetails(DefinedArrayDetailsBean arrayDetailsBean,
			SpecimenOrderItem specimenOrderItem)
	{
		DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) specimenOrderItem;
		arrayDetailsBean.setRequestedItem(derivedSpecimenOrderItem.getParentSpecimen().getLabel());
		arrayDetailsBean.setSpecimenId(derivedSpecimenOrderItem.getParentSpecimen().getId().toString());
		//Obtain all children specimens
		Collection childrenSpecimenList = OrderingSystemUtil.getAllChildrenSpecimen(derivedSpecimenOrderItem.getParentSpecimen(),
				derivedSpecimenOrderItem.getParentSpecimen().getChildrenSpecimen());
		//Obtain only those specimens of this class and type from the above list
		List finalChildrenSpecimenList = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childrenSpecimenList, derivedSpecimenOrderItem
				.getSpecimenClass(), derivedSpecimenOrderItem.getSpecimenType());
		List childrenSpecimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(finalChildrenSpecimenList);
		arrayDetailsBean.setSpecimenList(childrenSpecimenListToDisplay);

		arrayDetailsBean.setRequestedQuantity(derivedSpecimenOrderItem.getRequestedQuantity().getValue().toString());
		arrayDetailsBean.setAvailableQuantity(derivedSpecimenOrderItem.getParentSpecimen().getAvailableQuantity().getValue().toString());
		arrayDetailsBean.setAssignedStatus(derivedSpecimenOrderItem.getStatus());
		arrayDetailsBean.setClassName(derivedSpecimenOrderItem.getSpecimenClass());
		arrayDetailsBean.setType(derivedSpecimenOrderItem.getSpecimenType());
		arrayDetailsBean.setDescription(derivedSpecimenOrderItem.getDescription());
		arrayDetailsBean.setOrderItemId(derivedSpecimenOrderItem.getId().toString());
		arrayDetailsBean.setInstanceOf("Derived");

		return arrayDetailsBean;
	}

	/**
	 * @param arrayDetailsBean object
	 * @param specimenOrderItem object
	 * @return DefinedArrayDetailsBean object
	 */
	private DefinedArrayDetailsBean populatePathologicalCasesForArrayDetails(DefinedArrayDetailsBean arrayDetailsBean,
			SpecimenOrderItem specimenOrderItem)
	{
		PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) specimenOrderItem;
		boolean isDerived = false;
		arrayDetailsBean.setRequestedItem(pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
		Collection childrenSpecimenList = pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection();
		//		 Removing distributed option if no specimens are present in that SCG. ie. childrenSpecimenList.size() == 0
		//TODO
		if (childrenSpecimenList.size() == 0)
		{
			isDerived = true;
		}
		List totalChildrenSpecimenColl = null;
		List childrenSpecimenListToDisplay = null;
		Iterator childrenSpecimenListIterator = childrenSpecimenList.iterator();
		while (childrenSpecimenListIterator.hasNext())
		{
			Specimen specimen = (Specimen) childrenSpecimenListIterator.next();
			List childSpecimenCollection = OrderingSystemUtil.getAllChildrenSpecimen(specimen, specimen.getChildrenSpecimen());
			List finalChildrenSpecimenCollection = null;
			if (pathologicalCaseOrderItem.getSpecimenClass() != null && pathologicalCaseOrderItem.getSpecimenType() != null
					&& !pathologicalCaseOrderItem.getSpecimenClass().trim().equalsIgnoreCase("")
					&& !pathologicalCaseOrderItem.getSpecimenType().trim().equalsIgnoreCase(""))
			{ //"Derived"	   
				finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childSpecimenCollection,
						pathologicalCaseOrderItem.getSpecimenClass(), pathologicalCaseOrderItem.getSpecimenType());
				isDerived = true;
			}
			else
			{ //"Block" . Specimen class = "Tissue" , Specimen Type = "Block".
				finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childSpecimenCollection, "Tissue", "Block");
			}
			if (finalChildrenSpecimenCollection != null)
			{
				Iterator finalChildrenSpecimenCollectionIterator = finalChildrenSpecimenCollection.iterator();
				while (finalChildrenSpecimenCollectionIterator.hasNext())
				{
					totalChildrenSpecimenColl.add((Specimen) (finalChildrenSpecimenCollectionIterator.next()));
				}
			}
		}
		if (totalChildrenSpecimenColl == null)
		{
			totalChildrenSpecimenColl = new ArrayList();
		}
		childrenSpecimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(totalChildrenSpecimenColl);
		arrayDetailsBean.setSpecimenList(childrenSpecimenListToDisplay);
		arrayDetailsBean.setSpecimenCollGroupId(pathologicalCaseOrderItem.getSpecimenCollectionGroup().getId().toString());
		if (isDerived)
		{
			arrayDetailsBean.setInstanceOf("DerivedPathological");
		}
		else
		{
			arrayDetailsBean.setInstanceOf("Pathological");
		}
		arrayDetailsBean.setRequestedQuantity(pathologicalCaseOrderItem.getRequestedQuantity().getValue().toString());
		//Displaying the quantity of the first specimen in the request for drop down.
		if (childrenSpecimenListToDisplay.size() != 0)
		{
			arrayDetailsBean.setAvailableQuantity(((Specimen) totalChildrenSpecimenColl.get(0)).getAvailableQuantity().getValue().toString());
		}
		else
		{
			arrayDetailsBean.setAvailableQuantity("-");
		}
		//Assigned Quantity
		if (pathologicalCaseOrderItem.getDistributedItem() != null)
		{
			arrayDetailsBean.setAssignedQty(pathologicalCaseOrderItem.getDistributedItem().getQuantity().toString());
		}
		arrayDetailsBean.setAssignedStatus(pathologicalCaseOrderItem.getStatus());
		arrayDetailsBean.setClassName(pathologicalCaseOrderItem.getSpecimenClass());
		arrayDetailsBean.setType(pathologicalCaseOrderItem.getSpecimenType());
		arrayDetailsBean.setDescription(pathologicalCaseOrderItem.getDescription());
		arrayDetailsBean.setOrderItemId(pathologicalCaseOrderItem.getId().toString());

		return arrayDetailsBean;
	}

	//	 Populates a list of Requset object to display  as header info on RequestDetails.jsp	
	/**
	 * @param id String
	 * @return RequestViewBean object
	 * @throws DAOException object
	 */
	private RequestViewBean getRequestObject(String id) throws DAOException
	{
		List orderListFromDB = getOrderListFromDB(id);
		RequestViewBean requestViewBean = null;
		OrderDetails order = null;
		if ((orderListFromDB != null) && (orderListFromDB.isEmpty() == false))
		{
			Iterator iter = orderListFromDB.iterator();
			while (iter.hasNext())
			{
				order = (OrderDetails) iter.next();
				requestViewBean = OrderingSystemUtil.getRequestViewBeanToDisplay(order);
				requestViewBean.setComments(order.getComment());
			}
		}
		return requestViewBean;
	}

	/**
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	private List getOrderListFromDB(String id) throws DAOException
	{
		OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);

		String[] selectColumnName = {"id", "status", "distributionProtocol", "distributionProtocol.principalInvestigator", "name", "requestedDate",
				"comment"};
		String[] wherecolName = {"id"};
		Object[] whereColumnValue = {id};
		String[] whereColumnCond = {"="};
		List orderListFromDB = orderBizLogic.retrieve(OrderDetails.class.getName(), selectColumnName, wherecolName, whereColumnCond,
				whereColumnValue, Constants.AND_JOIN_CONDITION);
		List orderList = new ArrayList();

		Iterator itr = orderListFromDB.iterator();
		while (itr.hasNext())
		{
			Object[] obj = (Object[]) itr.next();
			Long identifier = (Long) obj[0];
			String status = (String) obj[1];
			DistributionProtocol dp = (DistributionProtocol) obj[2];
			User user = (User) obj[3];
			String name = (String) obj[4];
			Date requestedDate = (Date) obj[5];
			String comment = (String) obj[6];

			dp.setPrincipalInvestigator(user);

			OrderDetails orderDetails = new OrderDetails();
			orderDetails.setId(identifier);
			orderDetails.setStatus(status);
			orderDetails.setDistributionProtocol(dp);
			orderDetails.setName(name);
			orderDetails.setRequestedDate(requestedDate);
			orderDetails.setComment(comment);

			Collection orderItemCollection = (Collection) orderBizLogic.retrieveAttribute(OrderDetails.class.getName(), new Long(id),
					"elements(orderItemCollection)");

			/*Iterator itr2 = orderItemCollection.iterator();
			 while (itr2.hasNext())
			 {
			 OrderItem orderItem = (OrderItem) itr2.next();

			 String[] selectColumnName2 = {"requestedQuantity"};
			 String[] wherecolName2 = {"id"};
			 Object[] whereColumnValue2 = {orderItem.getId()};
			 String[] whereColumnCond2 = {"="};
			 List requestQtyFromDB = orderBizLogic.retrieve(OrderItem.class.getName(), selectColumnName2, wherecolName2, whereColumnCond2,
			 whereColumnValue2, Constants.AND_JOIN_CONDITION);
			 if (requestQtyFromDB != null && requestQtyFromDB.size() > 0 && requestQtyFromDB.get(0)!=null)
			 {
			 Quantity requestedQty = (Quantity)requestQtyFromDB.get(0);
			 orderItem.setRequestedQuantity(requestedQty);
			 System.out.println("Rq Qty :" + orderItem.getRequestedQuantity());
			 }
			 }*/
			orderDetails.setOrderItemCollection(orderItemCollection);
			orderList.add(orderDetails);
		}
		return orderList;
	}

	/**
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	private List getSpecimenFromDB(String id) throws DAOException
	{
		NewSpecimenBizLogic newSpecimenBizLogic = (NewSpecimenBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		List specimenListFromDB = newSpecimenBizLogic.retrieve(Specimen.class.getName(), "id", id);
		return specimenListFromDB;
	}

	/**
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	private List getSpecimenCollGrpFromDB(String id) throws DAOException
	{
		SpecimenCollectionGroupBizLogic specimenCollectionGroupBizLogic = (SpecimenCollectionGroupBizLogic) BizLogicFactory.getInstance()
				.getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		List specimenCollGrpListFromDB = specimenCollectionGroupBizLogic.retrieve(SpecimenCollectionGroup.class.getName(), "id", id);
		
		return specimenCollGrpListFromDB;
	}

	/**
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	private List getOrderItemFromDB(String id) throws DAOException
	{
		OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		List orderItemListFromDB = orderBizLogic.retrieve(OrderItem.class.getName(), "id", id);
		return orderItemListFromDB;
	}

	/**
	 * @return List of site objects.
	 * @throws DAOException object
	 */
	private List getSiteListToDisplay() throws DAOException
	{
		OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);

		//Sets the Site list.
		String sourceObjectName = Site.class.getName();
		String[] displayNameFields = {"name"};
		String valueField = Constants.SYSTEM_IDENTIFIER;

		List siteList = orderBizLogic.getList(sourceObjectName, displayNameFields, valueField, true);

		return siteList;
	}

	/**
	 * @param requestDetailsBean RequestDetailsBean
	 * @param existingSpecimenorderItem ExistingSpecimenOrderItem
	 * @return RequestDetailsBean object
	 * @throws DAOException 
	 */
	private RequestDetailsBean populateRequestDetailsBeanForExistingSpecimen(RequestDetailsBean requestDetailsBean,
			ExistingSpecimenOrderItem existingSpecimenorderItem) throws DAOException
	{
		requestDetailsBean.setRequestedItem(existingSpecimenorderItem.getSpecimen().getLabel());
		requestDetailsBean.setSpecimenId(existingSpecimenorderItem.getSpecimen().getId().toString());
		List childrenSpecimenListToDisplay = new ArrayList();
		requestDetailsBean.setSpecimenList(childrenSpecimenListToDisplay);
		
			
		requestDetailsBean.setInstanceOf("Existing");
	
		if(existingSpecimenorderItem.getConsentTierStatusCollection().isEmpty())
		{	
			requestDetailsBean.setConsentVerificationkey(Constants.NO_CONSENTS);
			
		}	
		else
		{
			requestDetailsBean.setConsentVerificationkey(Constants.VIEW_CONSENTS);
			
		}		

		
		// condition added by vaishali beacause requested Qty is coming NULL.
		//@FIX ME
	
		if (existingSpecimenorderItem.getRequestedQuantity() != null)
			requestDetailsBean.setRequestedQty(existingSpecimenorderItem.getRequestedQuantity().getValue().toString());
		requestDetailsBean.setAvailableQty(existingSpecimenorderItem.getSpecimen().getAvailableQuantity().getValue().toString());

		requestDetailsBean.setAssignedStatus(existingSpecimenorderItem.getStatus());
		requestDetailsBean.setClassName(existingSpecimenorderItem.getSpecimen().getClassName());
		requestDetailsBean.setType(existingSpecimenorderItem.getSpecimen().getType());
		requestDetailsBean.setDescription(existingSpecimenorderItem.getDescription());
		requestDetailsBean.setOrderItemId(existingSpecimenorderItem.getId().toString());
		//	  Assigned Quantity
		if (existingSpecimenorderItem.getDistributedItem() != null)
		{
			requestDetailsBean.setAssignedQty(existingSpecimenorderItem.getDistributedItem().getQuantity().toString());
		}

		return requestDetailsBean;
	}

	/**
	 * This function populates RequestDetailsBean object by fethcing data from DerivedSpecimenOrderItem domain object
	 * @param requestDetailsBean RequestDetailsBean object
	 * @param derivedSpecimenorderItem DerivedSpecimenOrderItem object
	 * @param request HttpServletRequest object
	 * @param finalSpecimenListId primitive integer value
	 * @return RequestDetailsBean object
	 */
	private RequestDetailsBean populateRequestDetailsBeanForDerivedSpecimen(RequestDetailsBean requestDetailsBean,
			DerivedSpecimenOrderItem derivedSpecimenorderItem, HttpServletRequest request, int finalSpecimenListId)
	{
		requestDetailsBean.setRequestedItem(derivedSpecimenorderItem.getParentSpecimen().getLabel());
		Long specimenId = derivedSpecimenorderItem.getParentSpecimen().getId();
		Collection childrenSpecimenList = OrderingSystemUtil.getAllChildrenSpecimen(derivedSpecimenorderItem.getParentSpecimen(),
				derivedSpecimenorderItem.getParentSpecimen().getChildrenSpecimen());
		List finalChildrenSpecimenList = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childrenSpecimenList, derivedSpecimenorderItem
				.getSpecimenClass(), derivedSpecimenorderItem.getSpecimenType());
		//	  removing final specimen List from session
		request.getSession().removeAttribute("finalSpecimenList" + finalSpecimenListId);
		//To display the available quantity of the selected specimen from RequestFor dropdown.
		//request.getSession().setAttribute("finalSpecimenList"+finalSpecimenListId, finalChildrenSpecimenList);

		List childrenSpecimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(finalChildrenSpecimenList);

		//setting requestFor list in request
		//request.setAttribute(Constants.REQUEST_FOR_LIST, childrenSpecimenListToDisplay);

		requestDetailsBean.setSpecimenList(childrenSpecimenListToDisplay);
		requestDetailsBean.setSpecimenId(specimenId.toString());
		requestDetailsBean.setInstanceOf("Derived");
		requestDetailsBean.setRequestedQty(derivedSpecimenorderItem.getRequestedQuantity().getValue().toString());
		//Displaying the quantity of the first specimen in the request for drop down.
		if (childrenSpecimenListToDisplay.size() != 0)
		{
			requestDetailsBean.setAvailableQty(((Specimen) finalChildrenSpecimenList.get(0)).getAvailableQuantity().getValue().toString());
		}
		else
		{
			requestDetailsBean.setAvailableQty("NA");//derivedSpecimenorderItem.getSpecimen().getAvailableQuantity().getValue().toString()	  		
		}
		//Assigned Quantity
		if (derivedSpecimenorderItem.getDistributedItem() != null)
		{
			requestDetailsBean.setAssignedQty(derivedSpecimenorderItem.getDistributedItem().getQuantity().toString());
		}
		requestDetailsBean.setAssignedStatus(derivedSpecimenorderItem.getStatus());
		requestDetailsBean.setClassName(derivedSpecimenorderItem.getSpecimenClass());
		requestDetailsBean.setType(derivedSpecimenorderItem.getSpecimenType());
		requestDetailsBean.setDescription(derivedSpecimenorderItem.getDescription());
		requestDetailsBean.setOrderItemId(derivedSpecimenorderItem.getId().toString());

		return requestDetailsBean;
	}

	/**
	 * This function populates RequestDetailsBean instances by fetching data from PathologicalCaseOrderItem domain instance
	 * @param requestDetailsBean RequestDetailsBean object
	 * @param pathologicalCaseOrderItem DerivedSpecimenOrderItem object
	 * @param request HttpServletRequest object
	 * @param finalSpecimenListId primitive integer value
	 * @return requestDetailsBean RequestDetailsBean object
	 */
	private RequestDetailsBean populateRequestDetailsBeanForPathologicalCase(RequestDetailsBean requestDetailsBean,
			PathologicalCaseOrderItem pathologicalCaseOrderItem, HttpServletRequest request, int finalSpecimenListId)
	{
		boolean isDerived = false;
		requestDetailsBean.setRequestedItem(pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSurgicalPathologyNumber());

		Collection childrenSpecimenList = pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSpecimenCollection();
		// Removing distributed option if no specimens are present in that SCG. ie. childrenSpecimenList.size() == 0
		//TODO find better option for this
		if (childrenSpecimenList.size() == 0)
		{
			isDerived = true;
		}
		Iterator childrenSpecimenListIterator = childrenSpecimenList.iterator();
		List totalChildrenSpecimenColl = null;
		List childrenSpecimenListToDisplay = null;
		while (childrenSpecimenListIterator.hasNext())
		{
			Specimen specimen = (Specimen) childrenSpecimenListIterator.next();
			List childSpecimenCollection = OrderingSystemUtil.getAllChildrenSpecimen(specimen, specimen.getChildrenSpecimen());
			List finalChildrenSpecimenCollection = null;
			if (pathologicalCaseOrderItem.getSpecimenClass() != null && pathologicalCaseOrderItem.getSpecimenType() != null
					&& !pathologicalCaseOrderItem.getSpecimenClass().trim().equalsIgnoreCase("")
					&& !pathologicalCaseOrderItem.getSpecimenType().trim().equalsIgnoreCase(""))
			{
				//"Derived"	   
				finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childSpecimenCollection,
						pathologicalCaseOrderItem.getSpecimenClass(), pathologicalCaseOrderItem.getSpecimenType());
				isDerived = true;
			}
			else
			{
				//"Block" . Specimen class = "Tissue" , Specimen Type = "Block".
				finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childSpecimenCollection, "Tissue", "Block");
			}
			if (finalChildrenSpecimenCollection != null)
			{
				Iterator finalChildrenSpecimenCollectionIterator = finalChildrenSpecimenCollection.iterator();
				while (finalChildrenSpecimenCollectionIterator.hasNext())
				{
					totalChildrenSpecimenColl.add((Specimen) (finalChildrenSpecimenCollectionIterator.next()));
				}
			}
		}
		if (totalChildrenSpecimenColl == null)
		{
			totalChildrenSpecimenColl = new ArrayList();
		}
		//	    	removing final specimen List from session
		request.getSession().removeAttribute("finalSpecimenList" + finalSpecimenListId);
		//To display the available quantity of the selected specimen from RequestFor dropdown.
		//request.getSession().setAttribute("finalSpecimenList"+finalSpecimenListId, totalChildrenSpecimenColl);

		childrenSpecimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(totalChildrenSpecimenColl);
		requestDetailsBean.setSpecimenList(childrenSpecimenListToDisplay);

		requestDetailsBean.setSpecimenCollGroupId(pathologicalCaseOrderItem.getSpecimenCollectionGroup().getId().toString());
		if (isDerived)
		{
			requestDetailsBean.setInstanceOf("DerivedPathological");
		}
		else
		{
			requestDetailsBean.setInstanceOf("Pathological");
		}
		requestDetailsBean.setRequestedQty(pathologicalCaseOrderItem.getRequestedQuantity().getValue().toString());
		//Displaying the quantity of the first specimen in the request for drop down.
		if (childrenSpecimenListToDisplay.size() != 0)
		{
			requestDetailsBean.setAvailableQty(((Specimen) totalChildrenSpecimenColl.get(0)).getAvailableQuantity().getValue().toString());
		}
		else
		{
			requestDetailsBean.setAvailableQty("-");
		}
		//	  Assigned Quantity
		if (pathologicalCaseOrderItem.getDistributedItem() != null)
		{
			requestDetailsBean.setAssignedQty(pathologicalCaseOrderItem.getDistributedItem().getQuantity().toString());
		}
		requestDetailsBean.setAssignedStatus(pathologicalCaseOrderItem.getStatus());
		requestDetailsBean.setClassName(pathologicalCaseOrderItem.getSpecimenClass());
		requestDetailsBean.setType(pathologicalCaseOrderItem.getSpecimenType());
		requestDetailsBean.setDescription(pathologicalCaseOrderItem.getDescription());
		requestDetailsBean.setOrderItemId(pathologicalCaseOrderItem.getId().toString());

		return requestDetailsBean;
	}

	/**
	 * This function populates the items status for individual order items in OrderList,DefinedArray List and Existing Array List
	 * and sets each of the list in the request attribute. 
	 * @param requestList ArrayList containing the list of orderitem objects 
	 * @param request HttpServletRequest object
	 * @param itemsAddedToArray String indicating whether items are to be added to defined array,existing array or to the speicmen orderlist
	 */
	void populateItemStatusList(List requestList, HttpServletRequest request, String itemsAddedToArray)
	{
		Iterator requestlistItr = requestList.iterator();
		while (requestlistItr.hasNext())
		{
			Object orderItemObj = requestlistItr.next();
			//In case of define array
			if (orderItemObj instanceof Map)
			{
				Map defineArrayMap = (HashMap) orderItemObj;
				Set keys = defineArrayMap.keySet();
				Iterator keySetItr = keys.iterator();
				while (keySetItr.hasNext())
				{
					DefinedArrayRequestBean definedArrayRequestBean = (DefinedArrayRequestBean) keySetItr.next();
					//Obtain request list for each defined array 
					List defineArrayDetailsList = (ArrayList) defineArrayMap.get(definedArrayRequestBean);
					Iterator defineArrayDetailsListItr = defineArrayDetailsList.iterator();
					while (defineArrayDetailsListItr.hasNext())
					{
						DefinedArrayDetailsBean definedArrayDetailsBean = (DefinedArrayDetailsBean) defineArrayDetailsListItr.next();
						//Get possible next statuses for a given status
						List possibleStatusList = (List) request.getAttribute(Constants.ITEM_STATUS_LIST_FOR_ITEMS_IN_ARRAY);
						//Set possibleStatusList in the DefinedArrayDetails bean instance.
						definedArrayDetailsBean.setItemStatusList(possibleStatusList);
					} //End while (while loop to iterate definedArrayList)
				}//End while (While loop to iterate list containing map objects for deifned array)				
			}//End if(orderItemObj instanceof Map)
			else
			{ //In case of Existing Array
				if (orderItemObj instanceof ExistingArrayDetailsBean)
				{
					ExistingArrayDetailsBean existingArrayDetailsBean = (ExistingArrayDetailsBean) orderItemObj;
					List possibleStatusList = OrderingSystemUtil.getPossibleStatusList(Constants.ORDER_REQUEST_STATUS_NEW);
					existingArrayDetailsBean.setItemStatusList(possibleStatusList);
				}
				//In case of Request Details List displayed in RequestDetails.jsp
				if (orderItemObj instanceof RequestDetailsBean)
				{
					RequestDetailsBean requestDetailsBean = (RequestDetailsBean) orderItemObj;
					List possibleStatusList = null;
					possibleStatusList = OrderingSystemUtil.getPossibleStatusList(Constants.ORDER_REQUEST_STATUS_NEW);
					requestDetailsBean.setItemsStatusList(possibleStatusList);
				}
			}//End else			
		}//End OuterMost while		
		/*Set the requestList in request Attribute*/
		if (itemsAddedToArray.equals("addToDefinedArrays"))
		{ //Set the request attribute ARRAY_REQUESTSMAP_LIST when items are to be displayed in the DefinedArrays List(ArrayRequests.jsp)
			request.setAttribute(Constants.DEFINEDARRAY_REQUESTS_LIST, requestList);
		}
		if (itemsAddedToArray.equals("addToExistingArrays"))
		{ //Set the request attribute ARRAY_REQUESTSMAP_LIST when items are to be displayed in the Existing Arrays List(ArrayRequests.jsp)
			request.setAttribute(Constants.EXISISTINGARRAY_REQUESTS_LIST, requestList);
		}
		if (itemsAddedToArray.equals("addToOrderList"))
		{ //Set the request attribute REQUEST_DETAILS_LIST when items are to be displayed in the RequestDetails.jsp
			request.setAttribute(Constants.REQUEST_DETAILS_LIST, requestList);
		}
	}

	private void getDetailedDataForOrderItemCollection(Collection orderItemColl) throws DAOException
	{
		OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		Iterator itr1 = orderItemColl.iterator();
		String[] whereColumnName = {"id"};
		String[] whereColumnCond = {"="};
		while (itr1.hasNext())
		{
			OrderItem orderItem = (OrderItem) itr1.next();
			String[] selectColumnName = {"distributedItem"};
			Object[] whereColumnValue = {orderItem.getId()};

			//Getting distributed Item for each order Item
			List distributedItemList = orderBizLogic.retrieve(OrderItem.class.getName(), selectColumnName, whereColumnName, whereColumnCond,
					whereColumnValue, Constants.AND_JOIN_CONDITION);
			if (distributedItemList != null && distributedItemList.size() > 0)
			{
				DistributedItem distributedItem = (DistributedItem) distributedItemList.get(0);
				orderItem.setDistributedItem(distributedItem);
			}

			//If orderItem is of type SpecimenOrderItem then get the newSPecimenArrayOrderItem.
			if (orderItem instanceof SpecimenOrderItem)
			{
				SpecimenOrderItem spOrderItem = (SpecimenOrderItem) orderItem;
				String[] selectColumnName3 = {"newSpecimenArrayOrderItem"};

				Object[] whereColumnValue3 = {orderItem.getId()};

				List newSpArrayOrderItemList = orderBizLogic.retrieve(SpecimenOrderItem.class.getName(), selectColumnName3, whereColumnName,
						whereColumnCond, whereColumnValue3, Constants.AND_JOIN_CONDITION);

				if (newSpArrayOrderItemList != null && newSpArrayOrderItemList.size() > 0)
				{
					NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) newSpArrayOrderItemList.get(0);
					//getNewSPecimenArrayOrderItemDetails(newSpecimenArrayOrderItem);				
					spOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayOrderItem);

				}
			}

			if (orderItem instanceof edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem)
			{
				ExistingSpecimenOrderItem existingSpOrderItem = (ExistingSpecimenOrderItem) orderItem;
				getSpecimenForExSpOrderItem(existingSpOrderItem, orderBizLogic);

			}
			else if (orderItem instanceof DerivedSpecimenOrderItem)
			{
				DerivedSpecimenOrderItem derivedSpOrderItem = (DerivedSpecimenOrderItem) orderItem;
				getParentSpForDericedSpOrderItem(derivedSpOrderItem, orderBizLogic);

			}
			else if (orderItem instanceof edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem)
			{
				getNewSpecimenArrayOrderItemDetails(orderItem);

			}
			else if(orderItem instanceof ExistingSpecimenArrayOrderItem)
			{
				ExistingSpecimenArrayOrderItem existingSpArrayOrderItem = (ExistingSpecimenArrayOrderItem)orderItem;
				String[] selectColumnName1 = {"specimenArray"};

				Object[] whereColumnValue1 = {orderItem.getId()};

				List specimenArrayList = orderBizLogic.retrieve(ExistingSpecimenArrayOrderItem.class.getName(), selectColumnName1, whereColumnName, whereColumnCond,
						whereColumnValue1, Constants.AND_JOIN_CONDITION);
				if(specimenArrayList != null && specimenArrayList.size()>0)
				{
					existingSpArrayOrderItem.setSpecimenArray((SpecimenArray)specimenArrayList.get(0));
				}
				System.out.println("SpecimenArrayList:"+specimenArrayList);
				
			}
			else if(orderItem instanceof PathologicalCaseOrderItem)
			{
				getSpecimenCollectionGroup(orderItem);
			}
		}

		//This function is for setting the same instace of newSpecimenArrayOrderItem to specimenOrderItem.
		setNewSpArrayOrderItemToSPOrderItem(orderItemColl);

	}

	private void getNewSpecimenArrayOrderItemDetails(OrderItem orderItem) throws DAOException
	{
		OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);

		String[] whereColumnName = {"id"};
		String[] whereColumnCond = {"="};

		NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) orderItem;
		String[] selectColumnName1 = {"specimenArrayType"};

		Object[] whereColumnValue1 = {orderItem.getId()};

		List orderItemList = orderBizLogic.retrieve(NewSpecimenArrayOrderItem.class.getName(), selectColumnName1, whereColumnName, whereColumnCond,
				whereColumnValue1, Constants.AND_JOIN_CONDITION);
		if (orderItemList != null && orderItemList.size() > 0)
		{
			SpecimenArrayType specimenArrayType = (SpecimenArrayType) orderItemList.get(0);
			newSpecimenArrayOrderItem.setSpecimenArrayType(specimenArrayType);
		}

		Collection specimenOrderItemCollection = (Collection) orderBizLogic.retrieveAttribute(NewSpecimenArrayOrderItem.class.getName(), orderItem
				.getId(), "elements(specimenOrderItemCollection)");
		if (specimenOrderItemCollection != null && specimenOrderItemCollection.size() > 0)
		{
			Iterator itr3 = specimenOrderItemCollection.iterator();
			//Collection existingSpOrderItemColl = new HashSet();
			while (itr3.hasNext())
			{
				OrderItem spOrderItem = (OrderItem) itr3.next();

				if (spOrderItem instanceof edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem)
				{
					ExistingSpecimenOrderItem existingSpOrderItem = (ExistingSpecimenOrderItem) spOrderItem;
					// setitng bidirectional relationship
					existingSpOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayOrderItem);

					getSpecimenForExSpOrderItem(existingSpOrderItem, orderBizLogic);
				}
				else if (spOrderItem instanceof DerivedSpecimenOrderItem)
				{
					DerivedSpecimenOrderItem derivedSpOrderItem = (DerivedSpecimenOrderItem) spOrderItem;
					//Setting bi-directional relationship
					derivedSpOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayOrderItem);

					getParentSpForDericedSpOrderItem(derivedSpOrderItem, orderBizLogic);

				}

			}
			//newSpecimenArrayOrderItem.setSpecimenOrderItemCollection(existingSpOrderItemColl);
		}

		newSpecimenArrayOrderItem.setSpecimenOrderItemCollection(specimenOrderItemCollection);

	}

	private void setNewSpArrayOrderItemToSPOrderItem(Collection orderItemColl)
	{
		Iterator itr2 = orderItemColl.iterator();

		while (itr2.hasNext())
		{
			OrderItem orderItem = (OrderItem) itr2.next();
			if (orderItem instanceof NewSpecimenArrayOrderItem)
			{
				NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) orderItem;
				Iterator itr3 = orderItemColl.iterator();
				while (itr3.hasNext())
				{
					OrderItem orderItem1 = (OrderItem) itr3.next();
					if (orderItem1 instanceof SpecimenOrderItem)
					{
						SpecimenOrderItem spOrderItem = (SpecimenOrderItem) orderItem1;
						if (spOrderItem.getNewSpecimenArrayOrderItem() != null
								&& spOrderItem.getNewSpecimenArrayOrderItem().getId().toString().equals(newSpecimenArrayOrderItem.getId().toString()))
						{
							spOrderItem.setNewSpecimenArrayOrderItem(newSpecimenArrayOrderItem);
						}
					}
				}
			}
		}
	}

	private void getSpecimenForExSpOrderItem(ExistingSpecimenOrderItem existingSpOrderItem, OrderBizLogic orderBizLogic) throws DAOException
	{
		String[] whereColumnName = {"id"};
		String[] whereColumnCond = {"="};
		String[] selectColumnName1 = {"specimen"};
		Object[] whereColumnValue1 = {existingSpOrderItem.getId()};

		List orderItemList = orderBizLogic.retrieve(ExistingSpecimenOrderItem.class.getName(), selectColumnName1, whereColumnName, whereColumnCond,
				whereColumnValue1, Constants.AND_JOIN_CONDITION);
		if (orderItemList != null && orderItemList.size() > 0)
		{
			Specimen specimen = (Specimen) orderItemList.get(0);
			existingSpOrderItem.setSpecimen(specimen);
			Collection consentTierStatusCollection =(Collection)orderBizLogic.retrieveAttribute(Specimen.class.getName(),specimen.getId(),"elements(consentTierStatusCollection)");
			existingSpOrderItem.setConsentTierStatusCollection(consentTierStatusCollection);
		}

	}

	private void getParentSpForDericedSpOrderItem(DerivedSpecimenOrderItem derivedSpOrderItem, OrderBizLogic orderBizLogic) throws DAOException
	{
		String[] whereColumnName = {"id"};
		String[] whereColumnCond = {"="};
		String[] selectColumnName1 = {"parentSpecimen"};

		Object[] whereColumnValue1 = {derivedSpOrderItem.getId()};

		List orderItemList = orderBizLogic.retrieve(DerivedSpecimenOrderItem.class.getName(), selectColumnName1, whereColumnName, whereColumnCond,
				whereColumnValue1, Constants.AND_JOIN_CONDITION);
		if (orderItemList != null && orderItemList.size() > 0)
		{
			Specimen specimen = (Specimen) orderItemList.get(0);
			derivedSpOrderItem.setParentSpecimen(specimen);
			getChildrenSpecimen(specimen);

		}
	}

	private void getChildrenSpecimen(Specimen specimen) throws DAOException
	{
		OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		if (specimen != null)
		{
			Collection childrenSpecimenCollection = (Collection) orderBizLogic.retrieveAttribute(Specimen.class.getName(), specimen.getId(),
					"elements(childrenSpecimen)");
			if (childrenSpecimenCollection != null)
			{
				specimen.setChildrenSpecimen(childrenSpecimenCollection);
				Iterator iterator = childrenSpecimenCollection.iterator();
				while (iterator.hasNext())
				{
					Specimen childSpecimen = (Specimen) iterator.next();
					getChildrenSpecimen(childSpecimen);
				}
			}
		}
	}
	
	//kalpana:Bug#5950
	private void getSpecimenCollectionGroup(OrderItem orderItem) throws DAOException
	{
		
		OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) orderItem;
		
		String[] whereColumnName = {"id"};
		String[] whereColumnCond = {"="};
		String[] selectColumnName1 = {"specimenCollectionGroup"};
		Object[] whereColumnValue1 = {orderItem.getId()};
		List specimenColGroupList = orderBizLogic.retrieve(PathologicalCaseOrderItem.class.getName(), selectColumnName1, whereColumnName, whereColumnCond,
				whereColumnValue1, Constants.AND_JOIN_CONDITION);
		if(specimenColGroupList != null && specimenColGroupList.size()>0)
		{
			SpecimenCollectionGroup specimenColGroup = (SpecimenCollectionGroup)specimenColGroupList.get(0);
			pathologicalCaseOrderItem.setSpecimenCollectionGroup(specimenColGroup);
			Collection specimenList = (Collection)orderBizLogic.retrieveAttribute(SpecimenCollectionGroup.class.getName(),specimenColGroup.getId(),"elements(specimenCollection)");
			if(specimenList != null)
			{
				specimenColGroup.setSpecimenCollection(specimenList);
				if(specimenList != null)
				{
					Iterator itr = specimenList.iterator();
					while(itr.hasNext())
					{
						Specimen specimen = (Specimen)itr.next();
						getChildrenSpecimen(specimen);
					}
				}
			}
		}
		
		
	}
	
	
}