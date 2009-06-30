
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.bean.OrderSpecimenBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

public class AddToOrderListAction extends BaseAction
{

	private transient Logger logger = Logger.getCommonLogger(AddToOrderListAction.class);

	/**
	* Overrides the execute method of BaseAction class.
	* Initializes various fields in OrderItem and OrderList.jsp.Also removes the selected orderitems from speicmen map 
	* @param mapping object
	* @param form object
	* @param request object
	* @param response object
	* @return ActionForward object
	* @throws Exception object
	**/
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		HttpSession session = request.getSession();
		if (session.getAttribute("OrderForm") != null)
		{
			logger.debug(" Start --------- In AddToOrderListAction.java ");
			Map itemMap = new HashMap();

			String target = null;
			String arrayName = null;

			String typeOf = request.getParameter("typeOf");
			//for specimen

			if (request.getParameter("remove") == null)
			{
				//For Biospecimen
				if (typeOf.equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
				{
					OrderSpecimenForm orderSpecimenFormObject = (OrderSpecimenForm) form;
					if (orderSpecimenFormObject.getSelectedItems() != null)
					{
						//put key-value pair in map
						itemMap = (Map) putValueInMapSpecimen(orderSpecimenFormObject, request);
						orderSpecimenFormObject.setSelectedItems(null);
						target = Constants.SPECIMEN_ORDER_FORM_TYPE;
					}
					arrayName = orderSpecimenFormObject.getAddToArray();
				}

				//For BiospecimenArray
				if (typeOf.equals(Constants.ARRAY_ORDER_FORM_TYPE))
				{
					OrderBiospecimenArrayForm orderArrayFormObject = (OrderBiospecimenArrayForm) form;
					if (orderArrayFormObject.getSelectedItems() != null)
					{
						itemMap = (Map) putValueInMapArray(orderArrayFormObject, request);
						orderArrayFormObject.setSelectedItems(null);
						target = Constants.ARRAY_ORDER_FORM_TYPE;
					}
					arrayName = "None";
				}

				//Pathology
				if (typeOf.equals(Constants.PATHOLOGYCASE_ORDER_FORM_TYPE))
				{
					OrderPathologyCaseForm pathologyFormObject = (OrderPathologyCaseForm) form;
					if (pathologyFormObject.getSelectedItems() != null)
					{
						//put key-value pair in map
						itemMap = (Map) putValueInMapPathology(pathologyFormObject, request);
						pathologyFormObject.setSelectedItems(null);
						target = Constants.PATHOLOGYCASE_ORDER_FORM_TYPE;
					}
					arrayName = pathologyFormObject.getAddToArray();
				}
				Collection orderItemSet = null;
				/*
				 * Following code gets list of bean objects from map
				 */
				MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.bean");
				orderItemSet = (HashSet) parser.generateData(itemMap);

				Iterator it = orderItemSet.iterator();
				List orderItemsList = new ArrayList();
				while (it.hasNext())
				{
					OrderSpecimenBean specbean = (OrderSpecimenBean) it.next();
					orderItemsList.add(specbean);
				}

				Map dataMap = null;
				/*
				 * Following code puts lists in map and then puts the map in session
				 */

				if (session.getAttribute(Constants.REQUESTED_BIOSPECIMENS) == null)
				{
					dataMap = new HashMap();
					session.setAttribute(Constants.REQUESTED_BIOSPECIMENS, dataMap);
				}

				dataMap = (Map) session.getAttribute(Constants.REQUESTED_BIOSPECIMENS);
				if (dataMap.containsKey(arrayName))
				{
					List orderItem = (List) dataMap.get(arrayName);
					Iterator orderItemItr = orderItemsList.iterator();
					while (orderItemItr.hasNext())
					{
						OrderSpecimenBean specimenBean = (OrderSpecimenBean) orderItemItr.next();
						orderItem.add(specimenBean);
					}
					dataMap.put(arrayName, orderItem);
				}
				else
				{
					dataMap.put(arrayName, orderItemsList);
				}

				session.setAttribute(Constants.REQUESTED_BIOSPECIMENS, dataMap);
			}//End if(request.getParameter("remove") == null)

			/*
			 * Following code sets the orderitems map in the session after removing the selected specimen items
			 */
			else
			//if(request.getParameter("remove") != null)
			{
				String remove = request.getParameter("remove");
				if (remove.equals("yes"))
				{
					if (typeOf.equals(Constants.SPECIMEN_ORDER_FORM_TYPE)) //If typeOf == 'specimen'
					{
						OrderSpecimenForm orderSpecimenFormObject = (OrderSpecimenForm) form;
						removeSelectedItems(orderSpecimenFormObject.getItemsToRemove(), session);
						target = Constants.SPECIMEN_ORDER_FORM_TYPE;
					}
					else if (typeOf.equals(Constants.ARRAY_ORDER_FORM_TYPE)) //If typeOf == 'biospecimen array'
					{
						OrderBiospecimenArrayForm orderArrayFormObject = (OrderBiospecimenArrayForm) form;
						removeSelectedItems(orderArrayFormObject.getItemsToRemove(), session);
						target = Constants.ARRAY_ORDER_FORM_TYPE;
					}
					else if (typeOf.equals(Constants.PATHOLOGYCASE_ORDER_FORM_TYPE)) //If typeOf == 'pathological case'
					{
						OrderPathologyCaseForm pathologyFormObject = (OrderPathologyCaseForm) form;
						removeSelectedItems(pathologyFormObject.getItemsToRemove(), session);
						target = Constants.PATHOLOGYCASE_ORDER_FORM_TYPE;
					}
				}//End if(remove == "yes")
			}//End else

			logger.debug(" End --------- In AddToOrderListAction.java ");
			return mapping.findForward(target);
		}
		else
		{
			return mapping.findForward("failure");
		}

	}

	/**
	 * This function accepts string array and removes the selected items from the list and stores the new listin the session
	 * @param itemsToRemove String [] containing the selected order items to be removed 
	 * @param session HttpSession Object
	 */
	private void removeSelectedItems(String[] itemsToRemove, HttpSession session)
	{
		//String array is obtained containing items that are checked to remove in the format-arrayName_specimenName_rowIndex#
		if (itemsToRemove != null)
		{
			//Map and List to store speicmen items to be retained
			HashMap orderItemsToRetainMap = new HashMap();
			List orderItemsToRetainList = new ArrayList();

			//Temporary maps to store the orderitems to be removed
			HashMap orderItemstoRemoveMap = new HashMap();
			List orderItemsToRemoveList = new ArrayList();
			String arrayNameToRemove = "";

			//Iterate till the length of string array
			for (int itemCount = 0; itemCount < itemsToRemove.length; itemCount++)
			{
				String itemsKey = itemsToRemove[itemCount];

				int index1 = itemsKey.indexOf("_");
				int index2 = itemsKey.lastIndexOf("_");

				arrayNameToRemove = itemsKey.substring(0, index1);
				String specimenNameToRemove = itemsKey.substring(index1 + 1, index2);
				String strRowIndex = itemsKey.substring(index2 + 1, itemsKey.length());
				int rowIndex = Integer.parseInt(strRowIndex);

				if (session.getAttribute(Constants.REQUESTED_BIOSPECIMENS) != null)
				{
					Map orderItemsMap = (HashMap) session
							.getAttribute(Constants.REQUESTED_BIOSPECIMENS);

					if (orderItemsMap.containsKey(arrayNameToRemove))
					{
						List orderItemsList = (ArrayList) orderItemsMap.get(arrayNameToRemove);
						OrderSpecimenBean orderSpecimenBeanObj = (OrderSpecimenBean) orderItemsList
								.get(rowIndex);

						//Move the orderspecimenobjects that are to be removed to orderItemsToRemoveList
						if (specimenNameToRemove.equalsIgnoreCase(orderSpecimenBeanObj
								.getSpecimenName()))
						{
							orderItemsToRemoveList.add(orderSpecimenBeanObj);
						}
					}
					//Collect each list(containing items to be removed) in a map
					orderItemstoRemoveMap.put(arrayNameToRemove, orderItemsToRemoveList);
				}
			}//End for

			orderItemsToRetainMap = (HashMap) session
					.getAttribute(Constants.REQUESTED_BIOSPECIMENS);

			/*Filter out the map in the session to retain only the required order items*/

			//Iterate the keySet.
			Iterator orderItemstoRemoveMapItr = orderItemstoRemoveMap.keySet().iterator();
			while (orderItemstoRemoveMapItr.hasNext())
			{
				String arrayName = (String) orderItemstoRemoveMapItr.next();
				if (orderItemsToRetainMap.containsKey(arrayName))
				{
					List itemsToRemoveList = (ArrayList) orderItemstoRemoveMap.get(arrayName);
					orderItemsToRetainList = (ArrayList) orderItemsToRetainMap.get(arrayName);

					//Remove the selected order items from the original list
					orderItemsToRetainList.removeAll(itemsToRemoveList);
				}
				//Only retain the items that are not checked to be removed
				orderItemsToRetainMap.put(arrayName, orderItemsToRetainList);
			}
			session.setAttribute(Constants.REQUESTED_BIOSPECIMENS, orderItemsToRetainMap);
		}//End if
	}

	/** 
	 * @param orderSpecimenFormObject OrderSpecimenForm instance
	 * @param request HttpServletRequest instance
	 * @return specimenMap HashMap instance
	 */
	private Map putValueInMapSpecimen(OrderSpecimenForm orderSpecimenFormObject,
			HttpServletRequest request)
	{
		// LinkedHashMap introduced to retain order
		Map specimenMap = new LinkedHashMap();
		String[] strSelectedItems = orderSpecimenFormObject.getSelectedItems();
		Map tempOrderSpecimenFormObjectMap = orderSpecimenFormObject.getValues();
		for (int j = 0; j < strSelectedItems.length; j++)
		{
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenId",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_specimenId"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenName",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_specimenName"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_availableQuantity",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_availableQuantity"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_requestedQuantity",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_requestedQuantity"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_description",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_description"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_unitRequestedQuantity",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_unitRequestedQuantity"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenClass",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_specimenClass"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenType",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_specimenType"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_isDerived",
					orderSpecimenFormObject.getTypeOfSpecimen());
			if (orderSpecimenFormObject.getTypeOfSpecimen().equalsIgnoreCase("true"))
			{
				specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenClass",
						orderSpecimenFormObject.getClassName());
				specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenType",
						orderSpecimenFormObject.getType());
			}
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_checkedToRemove",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_checkedToRemove"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_typeOfItem",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_typeOfItem"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_distributionSite",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_distributionSite"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_arrayName",
					orderSpecimenFormObject.getAddToArray());
		}
		return specimenMap;
	}

	/**
	 * @param orderArrayFormObject OrderBiospecimenArrayForm instance
	 * @param request HttpServletRequest object
	 * @return arrayMap HashMap instance
	 */
	private Map putValueInMapArray(OrderBiospecimenArrayForm orderArrayFormObject,
			HttpServletRequest request)
	{
		Map arrayMap = new HashMap();
		String[] strSelectedItems = orderArrayFormObject.getSelectedItems();
		Map tempOrderArrayFormObjectMap = orderArrayFormObject.getValues();
		//put key-value pair in map

		for (int j = 0; j < strSelectedItems.length; j++)
		{
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenId",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_specimenId"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenName",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_specimenName"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_availableQuantity",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_availableQuantity"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_requestedQuantity",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_requestedQuantity"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_description",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_description"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_unitRequestedQuantity",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_unitRequestedQuantity"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_checkedToRemove",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_checkedToRemove"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_isDerived",
					orderArrayFormObject.getTypeOfArray());
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_typeOfItem",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_typeOfItem"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_arrayName", "None");
			arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_distributionSite",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_distributionSite"));
			if (orderArrayFormObject.getTypeOfArray().equals("false"))
			{
				arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_unitRequestedQuantity",
						" ");
			}
			else
			{
				arrayMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_unitRequestedQuantity",
						"count");
			}

		}
		return arrayMap;

	}

	/**
	 * @param pathologyFormObject OrderPathologyCaseForm object
	 * @param request HttpServletRequest object
	 * @return pathologyMap HashMap object
	 */
	private Map putValueInMapPathology(OrderPathologyCaseForm pathologyFormObject,
			HttpServletRequest request)
	{
		//int reqQntyError = 0;
		//boolean isNumber = true;
		Map pathologyMap = new HashMap();
		String[] strSelectedItems = pathologyFormObject.getSelectedItems();
		Map tempOrderPathologyFormObjectMap = pathologyFormObject.getValues();
		for (int j = 0; j < strSelectedItems.length; j++)
		{
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenId",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_specimenId"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenName",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_specimenName"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_availableQuantity",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_availableQuantity"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_requestedQuantity",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_requestedQuantity"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_description",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_description"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j]
					+ "_specimenCollectionGroup", tempOrderPathologyFormObjectMap
					.get("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenCollectionGroup"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_collectionProtocol",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_collectionProtocol"));

			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_isDerived",
					pathologyFormObject.getTypeOfCase());

			if (pathologyFormObject.getTypeOfCase().equals("false"))
			{
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenClass",
						pathologyFormObject.getClassName());
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenType",
						pathologyFormObject.getType());
				pathologyMap
						.put("OrderSpecimenBean:" + strSelectedItems[j] + "_unitRequestedQuantity",
								tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:"
										+ strSelectedItems[j] + "_unitRequestedQuantity"));
			}
			else
			{
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenClass",
						"Tissue");
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_specimenType",
						"Fixed Tissue Block");
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j]
						+ "_unitRequestedQuantity", "count");
			}
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_checkedToRemove",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_checkedToRemove"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_typeOfItem",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItems[j]
							+ "_typeOfItem"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_arrayName",
					pathologyFormObject.getAddToArray());

			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_pathologicalStatus",
					pathologyFormObject.getPathologicalStatus());
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItems[j] + "_tissueSite",
					pathologyFormObject.getTissueSite());

		}
		return pathologyMap;

	}

}
