
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

/**
 * @author renuka_bajpai
 */
public class AddToOrderListAction extends BaseAction
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(AddToOrderListAction.class);

	/**
	 * Overrides the execute method of BaseAction class. Initializes various
	 * fields in OrderItem and OrderList.jsp. Also removes the selected
	 * orderitems from speicmen map
	 * @param mapping
	 *            object
	 * @param form
	 *            object
	 * @param request
	 *            object
	 * @param response
	 *            object
	 * @return ActionForward object
	 * @throws Exception
	 *             object
	 **/
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		final HttpSession session = request.getSession();
		if (session.getAttribute("OrderForm") != null)
		{
			this.logger.debug(" Start --------- In AddToOrderListAction.java ");
			Map itemMap = new HashMap();

			String target = null;
			String arrayName = null;

			final String typeOf = request.getParameter("typeOf");
			// for specimen

			if (request.getParameter("remove") == null)
			{
				// For Biospecimen
				if (typeOf.equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
				{
					final OrderSpecimenForm orderSpecimenFormObject = (OrderSpecimenForm) form;
					if (orderSpecimenFormObject.getSelectedItems() != null)
					{
						// put key-value pair in map
						itemMap = (Map) this
								.putValueInMapSpecimen(orderSpecimenFormObject, request);
						orderSpecimenFormObject.setSelectedItems(null);
						target = Constants.SPECIMEN_ORDER_FORM_TYPE;
					}
					arrayName = orderSpecimenFormObject.getAddToArray();
				}

				// For BiospecimenArray
				if (typeOf.equals(Constants.ARRAY_ORDER_FORM_TYPE))
				{
					final OrderBiospecimenArrayForm orderArrayFormObject = (OrderBiospecimenArrayForm) form;
					if (orderArrayFormObject.getSelectedItems() != null)
					{
						itemMap = (Map) this.putValueInMapArray(orderArrayFormObject, request);
						orderArrayFormObject.setSelectedItems(null);
						target = Constants.ARRAY_ORDER_FORM_TYPE;
					}
					arrayName = "None";
				}

				// Pathology
				if (typeOf.equals(Constants.PATHOLOGYCASE_ORDER_FORM_TYPE))
				{
					final OrderPathologyCaseForm pathologyFormObject = (OrderPathologyCaseForm) form;
					if (pathologyFormObject.getSelectedItems() != null)
					{
						// put key-value pair in map
						itemMap = (Map) this.putValueInMapPathology(pathologyFormObject, request);
						pathologyFormObject.setSelectedItems(null);
						target = Constants.PATHOLOGYCASE_ORDER_FORM_TYPE;
					}
					arrayName = pathologyFormObject.getAddToArray();
				}
				Collection orderItemSet = null;
				/*
				 * Following code gets list of bean objects from map
				 */
				final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.bean");
				orderItemSet = (HashSet) parser.generateData(itemMap);

				final Iterator it = orderItemSet.iterator();
				final List orderItemsList = new ArrayList();
				while (it.hasNext())
				{
					final OrderSpecimenBean specbean = (OrderSpecimenBean) it.next();
					orderItemsList.add(specbean);
				}

				Map dataMap = null;
				/*
				 * Following code puts lists in map and then puts the map in
				 * session
				 */

				if (session.getAttribute(Constants.REQUESTED_BIOSPECIMENS) == null)
				{
					dataMap = new HashMap();
					session.setAttribute(Constants.REQUESTED_BIOSPECIMENS, dataMap);
				}

				dataMap = (Map) session.getAttribute(Constants.REQUESTED_BIOSPECIMENS);
				if (dataMap.containsKey(arrayName))
				{
					final List orderItem = (List) dataMap.get(arrayName);
					final Iterator orderItemItr = orderItemsList.iterator();
					while (orderItemItr.hasNext())
					{
						final OrderSpecimenBean specimenBean = (OrderSpecimenBean) orderItemItr
								.next();
						orderItem.add(specimenBean);
					}
					dataMap.put(arrayName, orderItem);
				}
				else
				{
					dataMap.put(arrayName, orderItemsList);
				}

				session.setAttribute(Constants.REQUESTED_BIOSPECIMENS, dataMap);
			}// End if(request.getParameter("remove") == null)

			/*
			 * Following code sets the orderitems map in the session after
			 * removing the selected specimen items
			 */
			else
			// if(request.getParameter("remove") != null)
			{
				final String remove = request.getParameter("remove");
				if (remove.equals("yes"))
				{
					if (typeOf.equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
					// If typeOf == 'specimen'
					{
						final OrderSpecimenForm orderSpecimenFormObject = (OrderSpecimenForm) form;
						this.removeSelectedItems(orderSpecimenFormObject.getItemsToRemove(),
								session);
						target = Constants.SPECIMEN_ORDER_FORM_TYPE;
					}
					else if (typeOf.equals(Constants.ARRAY_ORDER_FORM_TYPE))
					// If typeOf == 'biospecimen array'
					{
						final OrderBiospecimenArrayForm orderArrayFormObject = (OrderBiospecimenArrayForm) form;
						this.removeSelectedItems(orderArrayFormObject.getItemsToRemove(), session);
						target = Constants.ARRAY_ORDER_FORM_TYPE;
					}
					else if (typeOf.equals(Constants.PATHOLOGYCASE_ORDER_FORM_TYPE))
					// If typeOf == 'pathological case'
					{
						final OrderPathologyCaseForm pathologyFormObject = (OrderPathologyCaseForm) form;
						this.removeSelectedItems(pathologyFormObject.getItemsToRemove(), session);
						target = Constants.PATHOLOGYCASE_ORDER_FORM_TYPE;
					}
				}// End if(remove == "yes")
			}// End else

			this.logger.debug(" End --------- In AddToOrderListAction.java ");
			return mapping.findForward(target);
		}
		else
		{
			return mapping.findForward("failure");
		}

	}

	/**
	 * This function accepts string array and removes the selected items from.
	 * the list and stores the new listin the session
	 * @param itemsToRemove
	 *            String [] containing the selected order items to be removed
	 * @param session
	 *            HttpSession Object
	 */
	private void removeSelectedItems(String[] itemsToRemove, HttpSession session)
	{
		// String array is obtained containing items that are
		// checked to remove in the format-arrayName_specimenName_rowIndex#
		if (itemsToRemove != null)
		{
			// Map and List to store speicmen items to be retained
			HashMap orderItemsToRetainMap = new HashMap();
			List orderItemsToRetainList = new ArrayList();

			// Temporary maps to store the orderitems to be removed
			final HashMap orderItemstoRemoveMap = new HashMap();
			final List orderItemsToRemoveList = new ArrayList();
			String arrayNameToRemove = "";

			// Iterate till the length of string array
			for (final String itemsKey : itemsToRemove)
			{
				final int index1 = itemsKey.indexOf("_");
				final int index2 = itemsKey.lastIndexOf("_");

				arrayNameToRemove = itemsKey.substring(0, index1);
				final String specimenNameToRemove = itemsKey.substring(index1 + 1, index2);
				final String strRowIndex = itemsKey.substring(index2 + 1, itemsKey.length());
				final int rowIndex = Integer.parseInt(strRowIndex);

				if (session.getAttribute(Constants.REQUESTED_BIOSPECIMENS) != null)
				{
					final Map orderItemsMap = (HashMap) session
							.getAttribute(Constants.REQUESTED_BIOSPECIMENS);

					if (orderItemsMap.containsKey(arrayNameToRemove))
					{
						final List orderItemsList = (ArrayList) orderItemsMap
								.get(arrayNameToRemove);
						final OrderSpecimenBean orderSpecimenBeanObj = (OrderSpecimenBean) orderItemsList
								.get(rowIndex);

						// Move the orderspecimenobjects that are
						// to be removed to orderItemsToRemoveList
						if (specimenNameToRemove.equalsIgnoreCase(orderSpecimenBeanObj
								.getSpecimenName()))
						{
							orderItemsToRemoveList.add(orderSpecimenBeanObj);
						}
					}
					// Collect each list(containing items to be removed) in a
					// map
					orderItemstoRemoveMap.put(arrayNameToRemove, orderItemsToRemoveList);
				}
			}// End for

			orderItemsToRetainMap = (HashMap) session
					.getAttribute(Constants.REQUESTED_BIOSPECIMENS);

			/*
			 * Filter out the map in the session to retain only the required
			 * order items
			 */

			// Iterate the keySet.
			final Iterator orderItemstoRemoveMapItr = orderItemstoRemoveMap.keySet().iterator();
			while (orderItemstoRemoveMapItr.hasNext())
			{
				final String arrayName = (String) orderItemstoRemoveMapItr.next();
				if (orderItemsToRetainMap.containsKey(arrayName))
				{
					final List itemsToRemoveList = (ArrayList) orderItemstoRemoveMap.get(arrayName);
					orderItemsToRetainList = (ArrayList) orderItemsToRetainMap.get(arrayName);

					// Remove the selected order items from the original list
					orderItemsToRetainList.removeAll(itemsToRemoveList);
				}
				// Only retain the items that are not checked to be removed
				orderItemsToRetainMap.put(arrayName, orderItemsToRetainList);
			}
			session.setAttribute(Constants.REQUESTED_BIOSPECIMENS, orderItemsToRetainMap);
		}// End if
	}

	/**
	 * @param orderSpecimenFormObject
	 *            OrderSpecimenForm instance
	 * @param request
	 *            HttpServletRequest instance
	 * @return specimenMap HashMap instance
	 */
	private Map putValueInMapSpecimen(OrderSpecimenForm orderSpecimenFormObject,
			HttpServletRequest request)
	{
		// LinkedHashMap introduced to retain order
		final Map specimenMap = new LinkedHashMap();
		final String[] strSelectedItems = orderSpecimenFormObject.getSelectedItems();
		final Map tempOrderSpecimenFormObjectMap = orderSpecimenFormObject.getValues();
		for (final String strSelectedItem : strSelectedItems)
		{
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenId",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_specimenId"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenName",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_specimenName"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_availableQuantity",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_availableQuantity"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_requestedQuantity",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_requestedQuantity"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_description",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_description"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_unitRequestedQuantity",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_unitRequestedQuantity"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenClass",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_specimenClass"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenType",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_specimenType"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_isDerived",
					orderSpecimenFormObject.getTypeOfSpecimen());
			if (orderSpecimenFormObject.getTypeOfSpecimen().equalsIgnoreCase("true"))
			{
				specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenClass",
						orderSpecimenFormObject.getClassName());
				specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenType",
						orderSpecimenFormObject.getType());
			}
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_checkedToRemove",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_checkedToRemove"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_typeOfItem",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_typeOfItem"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_distributionSite",
					tempOrderSpecimenFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_distributionSite"));
			specimenMap.put("OrderSpecimenBean:" + strSelectedItem + "_arrayName",
					orderSpecimenFormObject.getAddToArray());
		}
		return specimenMap;
	}

	/**
	 * @param orderArrayFormObject
	 *            OrderBiospecimenArrayForm instance
	 * @param request
	 *            HttpServletRequest object
	 * @return arrayMap HashMap instance
	 */
	private Map putValueInMapArray(OrderBiospecimenArrayForm orderArrayFormObject,
			HttpServletRequest request)
	{
		final Map arrayMap = new HashMap();
		final String[] strSelectedItems = orderArrayFormObject.getSelectedItems();
		final Map tempOrderArrayFormObjectMap = orderArrayFormObject.getValues();
		// put key-value pair in map

		for (final String strSelectedItem : strSelectedItems)
		{
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenId",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_specimenId"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenName",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_specimenName"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_availableQuantity",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_availableQuantity"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_requestedQuantity",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_requestedQuantity"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_description",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_description"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_unitRequestedQuantity",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_unitRequestedQuantity"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_checkedToRemove",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_checkedToRemove"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_isDerived",
					orderArrayFormObject.getTypeOfArray());
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_typeOfItem",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_typeOfItem"));
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_arrayName", "None");
			arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_distributionSite",
					tempOrderArrayFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_distributionSite"));
			if (orderArrayFormObject.getTypeOfArray().equals("false"))
			{
				arrayMap
						.put("OrderSpecimenBean:" + strSelectedItem + "_unitRequestedQuantity", " ");
			}
			else
			{
				arrayMap.put("OrderSpecimenBean:" + strSelectedItem + "_unitRequestedQuantity",
						"count");
			}

		}
		return arrayMap;

	}

	/**
	 * @param pathologyFormObject
	 *            OrderPathologyCaseForm object
	 * @param request
	 *            HttpServletRequest object
	 * @return pathologyMap HashMap object
	 */
	private Map putValueInMapPathology(OrderPathologyCaseForm pathologyFormObject,
			HttpServletRequest request)
	{
		// int reqQntyError = 0;
		// boolean isNumber = true;
		final Map pathologyMap = new HashMap();
		final String[] strSelectedItems = pathologyFormObject.getSelectedItems();
		final Map tempOrderPathologyFormObjectMap = pathologyFormObject.getValues();
		for (final String strSelectedItem : strSelectedItems)
		{
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenId",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_specimenId"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenName",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_specimenName"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_availableQuantity",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_availableQuantity"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_requestedQuantity",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_requestedQuantity"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_description",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_description"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenCollectionGroup",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_specimenCollectionGroup"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_collectionProtocol",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_collectionProtocol"));

			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_isDerived",
					pathologyFormObject.getTypeOfCase());

			if (pathologyFormObject.getTypeOfCase().equals("false"))
			{
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenClass",
						pathologyFormObject.getClassName());
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenType",
						pathologyFormObject.getType());
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_unitRequestedQuantity",
						tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
								+ "_unitRequestedQuantity"));
			}
			else
			{
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenClass",
						"Tissue");
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_specimenType",
						"Fixed Tissue Block");
				pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_unitRequestedQuantity",
						"count");
			}
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_checkedToRemove",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_checkedToRemove"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_typeOfItem",
					tempOrderPathologyFormObjectMap.get("OrderSpecimenBean:" + strSelectedItem
							+ "_typeOfItem"));
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_arrayName",
					pathologyFormObject.getAddToArray());

			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_pathologicalStatus",
					pathologyFormObject.getPathologicalStatus());
			pathologyMap.put("OrderSpecimenBean:" + strSelectedItem + "_tissueSite",
					pathologyFormObject.getTissueSite());

		}
		return pathologyMap;

	}

}
