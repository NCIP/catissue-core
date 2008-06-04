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
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.NewSpecimenOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.domain.Quantity;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.OrderingSystemUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
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
	 * @throws ClassNotFoundException 
	 */
	private void getRequestDetailsList(String id, RequestDetailsForm requestDetailsForm, HttpServletRequest request) throws DAOException, ClassNotFoundException
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
	
		if(existingSpecimenorderItem.getConsentTierStatusCollection()!=null && !existingSpecimenorderItem.getConsentTierStatusCollection().isEmpty())
		{	
			requestDetailsBean.setConsentVerificationkey(Constants.VIEW_CONSENTS);
			
		}	
		else
		{
			requestDetailsBean.setConsentVerificationkey(Constants.NO_CONSENTS);
			
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

	
	/**This will retrieve the distributedItem and set to orderItem  
	 * @param orderItemIds
	 * @param orderItemMap
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setDistributedItemToOrderItem(List orderItemIds,Map orderItemMap) throws DAOException, ClassNotFoundException
	{
		String ids = Utility.getCommaSeparatedIds(orderItemIds);
		
		if(orderItemIds!=null && !orderItemIds.isEmpty())
		{
			String hqlString=" select orderItem.id ,orderItem.distributedItem" +
			" from edu.wustl.catissuecore.domain.OrderItem as orderItem " +
			" where orderItem.id in ("+ids+")";
			
			List distributedItemList = Utility.executeQuery(hqlString);
			
			if(distributedItemList!=null && !distributedItemList.isEmpty())
			{	
				for(int i=0;i<distributedItemList.size();i++)
				{
					Object[] obj = (Object[])distributedItemList.get(i);
					OrderItem orderItem = (OrderItem)orderItemMap.get((Long)obj[0]);
					orderItem.setDistributedItem((DistributedItem)obj[1]);
				}
			}
		}
			
	}
	/**
	 * This will retrieve and set NewSpecimenArrayOrderItem to SpecimenOrderItem
	 * @param specimenOrderItemIds
	 * @param orderItemMap
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setNewSpecimenArrayOrderItemToSpecimenOrderItem(List specimenOrderItemIds,Map orderItemMap) throws DAOException, ClassNotFoundException
	{
		String ids = Utility.getCommaSeparatedIds(specimenOrderItemIds);
		if(specimenOrderItemIds!=null && !specimenOrderItemIds.isEmpty())
		{
			String hqlString=" select spOrderItem.id ,spOrderItem.newSpecimenArrayOrderItem" +
			" from edu.wustl.catissuecore.domain.SpecimenOrderItem as spOrderItem " +
			" where spOrderItem.id in ("+ids+")";
			
			List newSpecimenArrayOrderItemList = Utility.executeQuery(hqlString);
			if(newSpecimenArrayOrderItemList !=null && !newSpecimenArrayOrderItemList.isEmpty())
			{
				for(int i=0; i<newSpecimenArrayOrderItemList.size();i++)
				{
					Object[] obj = (Object[])newSpecimenArrayOrderItemList.get(i);
					SpecimenOrderItem specimenOrderItem = (SpecimenOrderItem)orderItemMap.get((Long)obj[0]);
					specimenOrderItem.setNewSpecimenArrayOrderItem((NewSpecimenArrayOrderItem)obj[1]);
				}
			}
		}
		
	}
	
	/**
	 * This will retrieve the consentTierStatusCollection for each specimen
	 * and set it ExistingSpecimenOrderItem 
	 * @param ids
	 * @param orderItemMap
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setConsentTierCollectionToExistingSpecimenOrderItem(String ids, Map orderItemMap) throws DAOException, ClassNotFoundException
	{
		
		Map consentierMap = new HashMap();
		String hqlString="select extOrderItem.id ,elements(extOrderItem.specimen.consentTierStatusCollection)" +
		" from edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem as extOrderItem  " +
		" where extOrderItem.id in ("+ids+") ";
		
		List consentTierStatusCollectionList =  Utility.executeQuery(hqlString);
		if(consentTierStatusCollectionList!=null && !consentTierStatusCollectionList.isEmpty())
		{	
			for(int i=0;i<consentTierStatusCollectionList.size();i++)
			{
				Object[] obj = (Object[]) consentTierStatusCollectionList.get(i);
				if(consentierMap.containsKey((Long)obj[0]))
				{
					List list = (List)consentierMap.get((Long)obj[0]);
					list.add(obj[1]);
					consentierMap.put((Long)obj[0], list);
									
				} else {
					List list = new ArrayList();
					list.add(obj[1]);
					consentierMap.put((Long)obj[0], list);
					
				}
			}	
		}
		
		Iterator keyIterator = consentierMap.keySet().iterator();
		while(keyIterator.hasNext())
		{
			Long existingSpecimenOrderItemId = (Long)keyIterator.next();
			ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem)orderItemMap.get(existingSpecimenOrderItemId);
			existingSpecimenOrderItem.setConsentTierStatusCollection((Collection)consentierMap.get(existingSpecimenOrderItemId));
		}
			
	}
	
	/**
	 * This will retrieve the required attributes of specimen,create the specimen object
	 * and set it ExistingSpecimenOrderItem. 
	 * @param existingSpecimenOrderItemIds
	 * @param orderItemMap
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setSpecimenToExistingSpecimenOrderItem(List existingSpecimenOrderItemIds,Map orderItemMap) throws DAOException, ClassNotFoundException
	{
		String ids = Utility.getCommaSeparatedIds(existingSpecimenOrderItemIds);
		if(existingSpecimenOrderItemIds!=null && !existingSpecimenOrderItemIds.isEmpty())
		{
						
			String hqlString = "select extOrderItem.id,extOrderItem.specimen.id ,extOrderItem.specimen.type ," +
			" extOrderItem.specimen.label,extOrderItem.specimen.initialQuantity," +
			" extOrderItem.specimen.availableQuantity,extOrderItem.specimen.class " +
			" from edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem as extOrderItem  " +
			" where extOrderItem.id in ("+ids+") ";
			
			List specimenList =  Utility.executeQuery(hqlString);
			if(specimenList!=null && !specimenList.isEmpty())
			{	
				for(int i=0;i<specimenList.size();i++)
				{
								
					Object[] obj = (Object[]) specimenList.get(i);	
					
					ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem)orderItemMap.get((Long)obj[0]);
					
					Specimen specimen =(Specimen)Utility.getSpecimenObject((String)obj[6]);
								
					specimen.setId((Long) obj[1]);
					specimen.setType((String)obj[2]);
					specimen.setLabel((String)obj[3]);
					specimen.setInitialQuantity((Quantity)obj[4]);
					specimen.setAvailableQuantity((Quantity)obj[5]);
					
					existingSpecimenOrderItem.setSpecimen(specimen);
				}
				setConsentTierCollectionToExistingSpecimenOrderItem(ids,orderItemMap);
			}	
			
		}
	}
	
	/**
	 * This will retrieve the required attribute of all the specimens associated to SCG,create the specimen object and 
	 * hierarchy of specimen ,created the specimen collection and set it SCG.
	 * @param scg
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setSpecimenToSCG(SpecimenCollectionGroup scg) throws DAOException, ClassNotFoundException
	{
		Map specimenMap = new HashMap();
		String hql = " select spec.label,spec.id ,spec.parentSpecimen.id ,spec.type,spec.initialQuantity," +
				" spec.availableQuantity,spec.class " +
				" from edu.wustl.catissuecore.domain.Specimen as spec " +
				" where spec.specimenCollectionGroup.id ="+(Long)scg.getId()+" ";
		
		List list = Utility.executeQuery(hql);
		List listOfAllSpecimen = new ArrayList();
		if(list!=null && !list.isEmpty())
		{
			/**
			 * create the specimen object and map having key as
			 * parentSpecimenId and value as the list of child specimens
			**/ 
			for(int i=0;i<list.size();i++)
			{
				  Object[] obj = (Object[])list.get(i);
				  Specimen specimen =(Specimen)Utility.getSpecimenObject((String)obj[6]);
				  			 
				  specimen.setLabel((String)obj[0]);
				  specimen.setId((Long)obj[1]);
				  specimen.setType((String)obj[3]);
				  specimen.setInitialQuantity((Quantity)obj[4]);
				  specimen.setAvailableQuantity((Quantity)obj[5]);
				  
				  Specimen parentSpecimen = new Specimen();
				  parentSpecimen.setId((Long)obj[2]);
				  specimen.setParentSpecimen(parentSpecimen);
				  
				  listOfAllSpecimen.add(specimen);
				  if(specimenMap.containsKey((Long)obj[2]) && specimen.getParentSpecimen().getId()!=null)
				  {
						List specimenList = (List)specimenMap.get((Long)obj[2]);
						specimenList.add(specimen);
						specimenMap.put((Long)obj[2], specimenList);
										
				  } else if(specimen.getParentSpecimen().getId()!=null){
						List specimenList = new ArrayList();
						specimenList.add(specimen);
						specimenMap.put((Long)obj[2], specimenList);
						
				 }
				
			}
			
			/**
			 * created the specimenCollection and set the
			 * specimenCollection to SCG
			 */
			List specimenCollection = new ArrayList();
			for(int i=0;i<listOfAllSpecimen.size();i++)
			{
				Specimen specimen = (Specimen)listOfAllSpecimen.get(i);
				if(specimenMap.containsKey((Long)specimen.getId()))
				{
					
					specimen.setChildrenSpecimen((Collection)specimenMap.get((Long)specimen.getId()));
					specimenCollection.add(specimen);
				} else if(specimen.getParentSpecimen().getId()==null || specimen.getParentSpecimen().getId().equals(""))
				{
					
					specimen.setChildrenSpecimen(new ArrayList());
					specimenCollection.add(specimen);
				} else {
					specimen.setChildrenSpecimen(new ArrayList());
				}
				
			}
			scg.setSpecimenCollection(specimenCollection);
			
		} else {
			
			scg.setSpecimenCollection(list);
		}
			
	}
	
	
	
	/**
	 * This retrieve the required attributes SCG and creats the SCG object
	 * @param pathologicalCaseOrderItemIds
	 * @param orderItemMap
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setSCGToPathologicalCaseOrderItem(List pathologicalCaseOrderItemIds,Map orderItemMap) throws DAOException, ClassNotFoundException
	{
		String ids = Utility.getCommaSeparatedIds(pathologicalCaseOrderItemIds);
		if(pathologicalCaseOrderItemIds!=null && !pathologicalCaseOrderItemIds.isEmpty())
		{
			String hql =" select pathoCase.id ,pathoCase.specimenCollectionGroup.id, pathoCase.specimenCollectionGroup.surgicalPathologyNumber " +
			" from edu.wustl.catissuecore.domain.PathologicalCaseOrderItem as pathoCase " +
			" where pathoCase.id in ("+ids+")";
					
			List specimenCollectionGroupList = Utility.executeQuery(hql);
			if(specimenCollectionGroupList!=null && !specimenCollectionGroupList.isEmpty())
			{
				for(int i=0 ; i<specimenCollectionGroupList.size();i++)
				{	
					Object[] obj = (Object[])specimenCollectionGroupList.get(i);
					PathologicalCaseOrderItem  pathologicalCaseOrderItem = (PathologicalCaseOrderItem)orderItemMap.get((Long)obj[0]);
					SpecimenCollectionGroup specimenCollectionGroup = new SpecimenCollectionGroup();
					specimenCollectionGroup.setId((Long)obj[1]);
					specimenCollectionGroup.setSurgicalPathologyNumber((String)obj[2]);
					pathologicalCaseOrderItem.setSpecimenCollectionGroup(specimenCollectionGroup);
					
					setSpecimenToSCG(specimenCollectionGroup);
					
										
				}	
					
			}
			
		}
	}

	
	/**
	 * This will retrieve SpecimenArray and set it to ExistingSpecimenArrayOrderItem
	 * @param existingSpecimenArrayOrderItemIds
	 * @param orderItemMap
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setSpecimenArrayToExistingSpecimenArrayOrderItem(List existingSpecimenArrayOrderItemIds,Map orderItemMap) throws DAOException, ClassNotFoundException
	{
		String ids = Utility.getCommaSeparatedIds(existingSpecimenArrayOrderItemIds);
		if(existingSpecimenArrayOrderItemIds!=null && !existingSpecimenArrayOrderItemIds.isEmpty())
		{
			String hql =" select exSpArrayOrItm.id ,exSpArrayOrItm.specimenArray " +//,elements(specimenArray.specimenArrayContentCollection)" +
			" from edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem as exSpArrayOrItm " +
			" where exSpArrayOrItm.id in ("+ids+")";
			
			List specimenArrayList = Utility.executeQuery(hql);
			if(specimenArrayList!=null && !specimenArrayList.isEmpty())
			{
				for(int i=0;i<specimenArrayList.size();i++)
				{
					Object[] obj = (Object[])specimenArrayList.get(i);
					ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem)orderItemMap.get((Long)obj[0]);
					existingSpecimenArrayOrderItem.setSpecimenArray((SpecimenArray)obj[1]);
					
				}
				
			}
			
		}
		
	}
	
	/**
	 * This will recursively retrieves the required attributes of
	 * specimens and creates the specimen Object
	 * @param parentSpecimen
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setChildSpecimen(Specimen parentSpecimen) throws DAOException, ClassNotFoundException
	{
		
		String hql =" select spec.label,spec.id,spec.type,spec.initialQuantity," +
		" spec.availableQuantity,spec.class" +
		" from edu.wustl.catissuecore.domain.Specimen as spec " +
		" where spec.parentSpecimen.id ="+(Long)parentSpecimen.getId() +" ";
		
		//List list = dao.executeQuery(hql, null, false, null);
		List list = Utility.executeQuery(hql);
		List specimenCol = new ArrayList();
		if(list!=null && !list.isEmpty())
		{
			for(int i=0;i<list.size();i++)
			{
				  Object[] obj = (Object[])list.get(i);
				  Specimen specimen = (Specimen)Utility.getSpecimenObject((String)obj[5]);
				  specimen.setLabel((String)obj[0]);
				  specimen.setId((Long)obj[1]);
				  specimen.setType((String)obj[2]);
				  specimen.setInitialQuantity((Quantity)obj[3]);
				  specimen.setAvailableQuantity((Quantity)obj[4]);
				  specimenCol.add(specimen);
				  setChildSpecimen(specimen);
			}
			
		}
		parentSpecimen.setChildrenSpecimen(specimenCol);
		
	}
		
	/**
	 * This method will retrieve the required attributes of ParentSpecimen associated to DerivedSpecimenOrderItem
	 * and creates the object of ParentSpecimen and set it to DerivedSpecimenOrderItem
	 * @param derivedSpecimenOrderItemIds
	 * @param orderItemMap
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setParentSpecimenToDerivedSpecimenOrderItem(List derivedSpecimenOrderItemIds,Map orderItemMap) throws DAOException, ClassNotFoundException
	{
		String ids = Utility.getCommaSeparatedIds(derivedSpecimenOrderItemIds);
		if(derivedSpecimenOrderItemIds!=null && !derivedSpecimenOrderItemIds.isEmpty())
		{
			String hql =" select deSpecOrdrItm.id ,deSpecOrdrItm.parentSpecimen.label ,deSpecOrdrItm.parentSpecimen.id," +
						" deSpecOrdrItm.parentSpecimen.type ,deSpecOrdrItm.parentSpecimen.availableQuantity, " +
						" deSpecOrdrItm.parentSpecimen.initialQuantity, deSpecOrdrItm.parentSpecimen.class  " +
						" from edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem as deSpecOrdrItm " +
						" where deSpecOrdrItm.id in ("+ids+")";
		
			List list = Utility.executeQuery(hql);
			if(list!=null && !list.isEmpty())
			{
				for(int i=0 ; i<list.size();i++)
				{
				   Object[] obj = (Object[])list.get(i);
				   Specimen parentSpecimen = (Specimen)Utility.getSpecimenObject((String)obj[6]);
				   
				   parentSpecimen.setLabel((String)obj[1]);
				   parentSpecimen.setId((Long)obj[2]);
				   parentSpecimen.setType((String)obj[3]);
				   parentSpecimen.setAvailableQuantity((Quantity)obj[4]);
				   parentSpecimen.setInitialQuantity((Quantity)obj[5]);
				   
				  			   
				   DerivedSpecimenOrderItem DerivedSpecimenOrderItem = (DerivedSpecimenOrderItem)orderItemMap.get((Long)obj[0]);
				   DerivedSpecimenOrderItem.setParentSpecimen(parentSpecimen);
				   setChildSpecimen(parentSpecimen) ;
				   
				}
				
				
			}
									
		}
		
	}
	
	/**
	 * This method iterates the list of specimenOrderItemIds get the specimenOrderItem 
	 * and check if it has NewSpecimenArrayOrderItem associated to it.
	 * It checks wheather specimenOrderItem is instance of DerivedSpecimenOrderItem or 
	 * ExistingSpecimenOrderItem and creates list of ids for each and 
	 * accrodingly it sets the necessary fields of specimenOrderItem
	 * @param ids
	 * @param specimenOrderItemIds
	 * @param orderItemMap
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setSpecimenOrderItemsToNewSpecOrderItem(String ids,List specimenOrderItemIds,Map orderItemMap)
	throws DAOException, ClassNotFoundException
	{
		/**
		 * specimenOrderItemMap Map holds OrderItem Id as key and collection of SpecimenOrderItem
		 * to that OrderItem as value
		 */
		HashMap<Long, List<Object>>specimenOrderItemMap = new HashMap<Long, List<Object>>();
		/**
		 * specOrderItemsMap holds SpecimenOrderItem Id as key 
		 * and value as SpecimenOrderItem
		 */
		Map<Long,Object> specOrderItemsMap = new HashMap<Long, Object>();
		List <Long> derivedSpecimenOrderItemIds = new ArrayList<Long>();
		List <Long> existingSpecimenOrderItemIds = new ArrayList<Long>();
		
		/*String hql="select newSpecArrOrdrItm.id ,elements(newSpecArrOrdrItm.specimenOrderItemCollection)" +
		" from edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem as newSpecArrOrdrItm  " +
		" where newSpecArrOrdrItm.id in ("+ids+") ";
		
		List specimenOrderItemsList = Utility.executeQuery(hql);
		System.out.println("List prev "+specimenOrderItemsList.size());
		*/
		if(specimenOrderItemIds!=null && !specimenOrderItemIds.isEmpty())
		{
			for(int i=0;i<specimenOrderItemIds.size();i++)
			{
				//Object[] obj = (Object[]) specimenOrderItemIds.get(i);
				SpecimenOrderItem specimenOrderItem = (SpecimenOrderItem)orderItemMap.get((Long)specimenOrderItemIds.get(i));
				
				if(specimenOrderItem.getNewSpecimenArrayOrderItem()!=null && specimenOrderItem.getNewSpecimenArrayOrderItem().getId()!=null
						&& specimenOrderItem.getNewSpecimenArrayOrderItem() instanceof NewSpecimenArrayOrderItem)
				{	
					
					specOrderItemsMap.put((Long)specimenOrderItem.getId(), specimenOrderItem);
					
					/**
					 * Creats ids list of DerivedSpecimenOrderItem
					 * and ExistingSpecimenOrderItem 
					 */
					if(specimenOrderItem instanceof DerivedSpecimenOrderItem)
					{
						derivedSpecimenOrderItemIds.add(specimenOrderItem.getId());
					}
					if(specimenOrderItem instanceof ExistingSpecimenOrderItem)
					{
						existingSpecimenOrderItemIds.add(specimenOrderItem.getId());
					}
					
					
					if(specimenOrderItemMap.containsKey((Long)specimenOrderItem.getNewSpecimenArrayOrderItem().getId()))
					{
						List specimenOrderItems = (List)specimenOrderItemMap.get((Long)specimenOrderItem.getNewSpecimenArrayOrderItem().getId());
						specimenOrderItems.add(specimenOrderItem);
						//specimenOrderItemMap.put((Long)specimenOrderItem.getId(), specimenOrderItems);
															
					} else {
						List specimenOrderItems = new ArrayList();
						specimenOrderItems.add(specimenOrderItem);
						specimenOrderItemMap.put((Long)specimenOrderItem.getNewSpecimenArrayOrderItem().getId(), specimenOrderItems);
						
					}
				}	
			}
			setSpecimenToExistingSpecimenOrderItem(existingSpecimenOrderItemIds,specOrderItemsMap);
			setParentSpecimenToDerivedSpecimenOrderItem(derivedSpecimenOrderItemIds,specOrderItemsMap);
			
			Iterator keyIterator = specimenOrderItemMap.keySet().iterator();
			while(keyIterator.hasNext())
			{
				Long newSpecimenArrayOrderItemId = (Long)keyIterator.next();
				NewSpecimenArrayOrderItem  newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)orderItemMap.get(newSpecimenArrayOrderItemId);
				newSpecimenArrayOrderItem.setSpecimenOrderItemCollection((Collection)specimenOrderItemMap.get(newSpecimenArrayOrderItemId));
			}
			
		}
		
	}
	
	/**
	 * This method retrieves the SpecimenArrayType and 
	 * set it to NewSpecimenArrayOrderItem
	 * @param newSpecimenArrayOrderItemIds
	 * @param specimenOrderItemIds
	 * @param orderItemMap
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	private void setSpecimenArrayTypeToNewSpecimenArrayOrderItem(List newSpecimenArrayOrderItemIds,List specimenOrderItemIds,Map orderItemMap) throws DAOException, ClassNotFoundException
	{
		String ids = Utility.getCommaSeparatedIds(newSpecimenArrayOrderItemIds);
		if(newSpecimenArrayOrderItemIds!=null && !newSpecimenArrayOrderItemIds.isEmpty())
		{
			String hql =" select newSpecArrOrdrItm.id ,newSpecArrOrdrItm.specimenArrayType " +//,elements(specimenArray.specimenArrayContentCollection)" +
			" from edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem as newSpecArrOrdrItm " +
			" where newSpecArrOrdrItm.id in ("+ids+")";
			
			List list = Utility.executeQuery(hql);
			if(list!=null && !list.isEmpty())
			{
				for(int i=0 ; i<list.size();i++)
				{
				   Object[] obj = (Object[])list.get(i);
				   NewSpecimenArrayOrderItem  newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)orderItemMap.get((Long)obj[0]);
				   newSpecimenArrayOrderItem.setSpecimenArrayType((SpecimenArrayType)obj[1]);
				   
				}
							
			}
			setSpecimenOrderItemsToNewSpecOrderItem(ids,specimenOrderItemIds,orderItemMap);
			
		}	
		
		
	}
	
	private void getDetailedDataForOrderItemCollection(Collection orderItemColl) throws DAOException, ClassNotFoundException
	{
		/**orderItemMap holds key as id of orderItem and orderItem as value **/
		Map <Long ,Object>orderItemMap = new HashMap<Long, Object>();
		/**List of ids orderItems **/
		List <Long>orderItemIds = new ArrayList<Long>();
		List <Long> specimenOrderItemIds = new ArrayList<Long>();
		List <Long> newSpecimenArrayOrderItemIds = new ArrayList<Long>();
		List <Long> existingSpecimenArrayOrderItemIds = new ArrayList<Long>();
		List <Long> derivedSpecimenOrderItemIds = new ArrayList<Long>();
		List <Long> existingSpecimenOrderItemIds = new ArrayList<Long>();
		List <Long> pathologicalCaseOrderItemIds = new ArrayList<Long>();
		Iterator iterateOrderItemColl = orderItemColl.iterator();
	
		while (iterateOrderItemColl.hasNext())
		{
			OrderItem orderItem = (OrderItem) iterateOrderItemColl.next();
			orderItemIds.add(orderItem.getId());
			orderItemMap.put((Long)orderItem.getId(), orderItem);
			if (orderItem instanceof SpecimenOrderItem)
			{
				specimenOrderItemIds.add(orderItem.getId());
			} 
			if(orderItem instanceof NewSpecimenArrayOrderItem)
			{
				newSpecimenArrayOrderItemIds.add(orderItem.getId());
			}
			if(orderItem instanceof ExistingSpecimenArrayOrderItem)
			{
				existingSpecimenArrayOrderItemIds.add(orderItem.getId());
			}
			if(orderItem instanceof DerivedSpecimenOrderItem)
			{
				derivedSpecimenOrderItemIds.add(orderItem.getId());
			}
			if(orderItem instanceof ExistingSpecimenOrderItem)
			{
				existingSpecimenOrderItemIds.add(orderItem.getId());
			}
			if(orderItem instanceof PathologicalCaseOrderItem)
			{
				pathologicalCaseOrderItemIds.add(orderItem.getId());
			}
						
		}
		/**This method is for setting DistributedItem to OrderItem **/	
		setDistributedItemToOrderItem(orderItemIds,orderItemMap);
		
		/**This method is for setting NewSpecimenArrayOrderItem to SpecimenOrderItem **/	
		setNewSpecimenArrayOrderItemToSpecimenOrderItem(specimenOrderItemIds,orderItemMap);
		
		/**This method is for setting Specimen and ConsentTierCollection to ExistingSpecimenOrderItem **/
		setSpecimenToExistingSpecimenOrderItem(existingSpecimenOrderItemIds,orderItemMap);
		
		/**This method is for setting SpecimenArray to ExistingSpecimenArrayOrderItem **/
		setSpecimenArrayToExistingSpecimenArrayOrderItem(existingSpecimenArrayOrderItemIds,orderItemMap);
		
		/**This method is for setting SCG and specimens to PathologicalCaseOrderItem **/
		setSCGToPathologicalCaseOrderItem(pathologicalCaseOrderItemIds,orderItemMap);
		
		/**This method is for setting ParentSpecimen and specimens to DerivedSpecimenOrderItem **/
		setParentSpecimenToDerivedSpecimenOrderItem(derivedSpecimenOrderItemIds,orderItemMap);
		
		/**This method is for setting SpecimenArrayType and specimens to NewSpecimenArrayOrderItem **/
		setSpecimenArrayTypeToNewSpecimenArrayOrderItem(newSpecimenArrayOrderItemIds,specimenOrderItemIds,orderItemMap);
		
		//This function is for setting the same instace of newSpecimenArrayOrderItem to specimenOrderItem.
		setNewSpArrayOrderItemToSPOrderItem(orderItemColl);

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