
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.query.queryobject.impl.metadata.QueryOutputTreeAttributeMetadata;
import edu.wustl.common.query.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.QuerySessionData;
import edu.wustl.query.util.global.AQConstants;

/**
 * @author santhoshkumar_c
 *
 */
public class AddDeleteCartAction extends QueryShoppingCartAction
{

	/*
	 * (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	/**
	 * @param mapping : mapping
	 * @param form : form
	 * @param request : request
	 * @param response : response
	 * @throws Exception : Exception
	 * @return ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		final HttpSession session = request.getSession();
		String target = null;
		final String operation = request.getParameter(Constants.OPERATION);

		// Extracting map from formbean which gives the serial numbers of selected rows
		final String isCheckAllAcrossAllChecked = request
				.getParameter(Constants.CHECK_ALL_ACROSS_ALL_PAGES);
		final QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);

		//Get Attribute list and column list after define view.
		final SelectedColumnsMetadata selectedColumnMetaData = (SelectedColumnsMetadata) session
				.getAttribute(Constants.SELECTED_COLUMN_META_DATA);

		//QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		List<AttributeInterface> attributeList = null;
		if (selectedColumnMetaData != null)
		{
			attributeList = selectedColumnMetaData.getAttributeList();
		}

		// Check if user wants to add in Shopping Cart.
		if (Constants.ADD.equals(operation))
		{
			target = this.add(request, searchForm, isCheckAllAcrossAllChecked, attributeList);

		}// Check if user wants to delete record from cart.
		else if (Constants.DELETE.equals(operation))
		{
			target = this.delete(request, target, this.getCheckboxValues(searchForm));
		}

		//'id' attribute of the orderable entity is not included in view.
		String message;
		if (selectedColumnMetaData == null)
		{
			message = null;
		}
		else
		{
			message = getMessageIfIdNotPresentForOrderableEntities(selectedColumnMetaData, cart);
		}
		session.setAttribute(Constants.VALIDATION_MESSAGE_FOR_ORDERING, message);
		request.setAttribute(Constants.PAGE_OF, AQConstants.PAGEOF_QUERY_MODULE);
		return mapping.findForward(target);
	}

	/**
	 * checks if the current view contains any orderable entity and if the 'id' attribute of the entity is
	 * included in the view.
	 * @param selectedColumnsMetadata - gives current entity and attribute list
	 * @param cart - gives the attribute list of the entities present in the cart.
	 * @return message if if the 'id' attribute of the orderable entity is not included in view.
	 */
	public static String getMessageIfIdNotPresentForOrderableEntities(
			SelectedColumnsMetadata selectedColumnsMetadata, QueryShoppingCart cart)
	{
		final QueryShoppingCartBizLogic queryShoppingCartBizLogic = new QueryShoppingCartBizLogic();
		boolean areListsequal = true;
		boolean isOrderableEntityPresent = false;
		boolean isAttribIdIncludedInView = false;
		final List<String> orderableEntityNameList = Arrays.asList(Constants.entityNameArray);
		final List<QueryOutputTreeAttributeMetadata> selectedAttributeMetaDataList = selectedColumnsMetadata
				.getSelectedAttributeMetaDataList();
		final List<AttributeInterface> currentAttributeList = selectedColumnsMetadata
				.getAttributeList();
		// check if the cart view and the defined view are same
		if (cart != null)
		{
			final List<AttributeInterface> cartAttributeList = cart.getCartAttributeList();
			if (cartAttributeList != null)
			{
				final int indexArray[] = queryShoppingCartBizLogic.getNewAttributeListIndexArray(
						cartAttributeList, currentAttributeList);
				if (indexArray == null)
				{
					areListsequal = false;
				}
			}
		}
		if (areListsequal)
		{
			//if the two views are same checks if
			//orderable entity is present and id attribute is present
			final Iterator<QueryOutputTreeAttributeMetadata> iterator = selectedAttributeMetaDataList
					.iterator();
			QueryOutputTreeAttributeMetadata element;
			while (iterator.hasNext())
			{
				element = iterator.next();
				if (orderableEntityNameList.contains(element.getAttribute().getEntity().getName()))
				{
					isOrderableEntityPresent = true;
					if (element.getAttribute().getName().equals(Constants.ID))
					{
						isAttribIdIncludedInView = true;
						break;
					}
				}
			}
			if ((isOrderableEntityPresent) && (!isAttribIdIncludedInView))
			{
				ApplicationProperties.getValue("query.defineGridResultsView.messageForPopup");
			}
		}
		return null;
	}

	/**
	 * @param request : request
	 * @param searchForm : searchForm
	 * @param isCheckAllAcrossAllChecked : isCheckAllAcrossAllChecked
	 * @param attributeList : attributeList
	 * @return String : String
	 * @throws ApplicationException : ApplicationException
	 */
	private String add(HttpServletRequest request, AdvanceSearchForm searchForm,
			String isCheckAllAcrossAllChecked, List<AttributeInterface> attributeList)
			throws ApplicationException
	{

		final HttpSession session = request.getSession();
		final QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);

		String target;
		List<Integer> chkBoxValues;
		if (Constants.TRUE.equals(isCheckAllAcrossAllChecked))
		{
			chkBoxValues = null;
		}
		else
		{
			chkBoxValues = this.getCheckboxValues(searchForm);
		}
		target = this.addToCart(request, chkBoxValues, cart, attributeList);

		// if My List is not empty sets the value to false.
		request.getSession().setAttribute(Constants.IS_LIST_EMPTY, Constants.FALSE);
		return target;
	}

	/**
	 * @param request : request
	 * @param target : target
	 * @param chkBoxValues : chkBoxValues
	 * @return String : String
	 */
	private String delete(HttpServletRequest request, String target, List<Integer> chkBoxValues)
	{
		final HttpSession session = request.getSession();
		final QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);

		this.deleteFromCart(cart, chkBoxValues);
		if (cart.getCart().size() == 0)
		{
			final ActionErrors errors = new ActionErrors();
			final ActionError error = new ActionError("ShoppingCart.emptyCartTitle");
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);
			// if My List is empty sets the value to true.
			request.getSession().setAttribute(Constants.IS_LIST_EMPTY, Constants.TRUE);
		}
		// set the options
		this.setCartView(request, cart);
		target = new String(Constants.SHOPPING_CART_DELETE);

		return target;
	}

	/**
	 * @param request : request
	 * @param chkBoxValues : chkBoxValues
	 * @param cart : cart
	 * @param attributeList : attributeList
	 * @return String : String
	 * @throws ApplicationException : ApplicationException
	 */
	private String addToCart(HttpServletRequest request, List<Integer> chkBoxValues,
			QueryShoppingCart cart, List<AttributeInterface> attributeList)
			throws ApplicationException
	{
		String target;
		final List<List<String>> dataList = this.getPaginationDataList(request);
		final HttpSession session = request.getSession();
		final List<String> columnList = (List<String>) session
				.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);

		//if cart is not present in session create new cart object
		if (cart == null || cart.isEmpty())
		{
			cart = new QueryShoppingCart();
			cart.setCartAttributeList(attributeList);
			cart.setColumnList(columnList);
			this.addDataToCart(cart, dataList, chkBoxValues, request, columnList);
		}
		else
		{
			final List<AttributeInterface> oldAttributeList = cart.getCartAttributeList();
			final QueryShoppingCartBizLogic queryShoppingCartBizLogic = new QueryShoppingCartBizLogic();
			final int indexArray[] = queryShoppingCartBizLogic.getNewAttributeListIndexArray(
					oldAttributeList, attributeList);
			if (indexArray != null)
			{
				final List<List<String>> tempdataList = this.getManipulatedDataList(dataList,
						indexArray);
				this.addDataToCart(cart, tempdataList, chkBoxValues, request, columnList);
			}
			else
			{
				this.addDifferentCartViewError(request);
			}
		}

		target = new String(Constants.SHOPPING_CART_ADD);
		request.setAttribute(Constants.PAGINATION_DATA_LIST, dataList);
		return target;
	}

	/**
	 * Add data in Cart .
	 * @param cart a shopping cart object preset in session.
	 * @param dataList : dataList
	 * @param request : request
	 * @param chkBoxValues : chkBoxValues
	 * @param columnList :columnList
	*/
	public void addDataToCart(QueryShoppingCart cart, List<List<String>> dataList,
			List<Integer> chkBoxValues, HttpServletRequest request, List<String> columnList)
	{
		final HttpSession session = request.getSession();
		final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		// Add a record to cart.
		final int addRecordCount = bizLogic.add(cart, dataList, chkBoxValues);
		int duplicateRecordCount = 0;

		if (chkBoxValues != null)
		{
			duplicateRecordCount = chkBoxValues.size() - addRecordCount;
		}
		else
		{
			duplicateRecordCount = dataList.size() - addRecordCount;
		}
		//ActionErrors changed to ActionMessages
		final ActionMessages messages = new ActionMessages();
		// Check if no. of duplicate records is not zero then set a error message.

		if (addRecordCount == 0 && duplicateRecordCount != 0)
		{
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"shoppingcart.HashedOutError", addRecordCount));
		}
		else if (addRecordCount != 0 && duplicateRecordCount != 0)
		{
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"shoppingCart.addMessage", addRecordCount));
		}
		else if (addRecordCount != 0 && duplicateRecordCount == 0)
		{
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"shoppingcart.duplicateObjError", addRecordCount, duplicateRecordCount));
		}

		session.setAttribute(Constants.QUERY_SHOPPING_CART, cart);
		request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnList);

		this.saveMessages(request, messages);
	}

	/**
	 * @param cart a shopping cart object preset in session.
	 * @param chkBoxValues : chkBoxValues
	 */
	public void deleteFromCart(QueryShoppingCart cart, List<Integer> chkBoxValues)
	{
		final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		bizLogic.delete(cart, chkBoxValues);

	}

	/**
	 * Get Pegination data list .
	 * @param request HttpServletRequest.
	 * @throws ApplicationException : ApplicationException
	 * @return dataList : dataList
	*/
	public List<List<String>> getPaginationDataList(HttpServletRequest request)
			throws ApplicationException
	{
		final HttpSession session = request.getSession();
		final String pageNo = request.getParameter(Constants.PAGE_NUMBER);
		//Integer.parseInt(XMLPropertyHandler.getValue(Constants.NO_OF_RECORDS_PER_PAGE));
		final String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		final String isCheckAllAcrossAllChecked = request
				.getParameter(Constants.CHECK_ALL_ACROSS_ALL_PAGES);

		if (pageNo != null)
		{
			request.setAttribute(Constants.PAGE_NUMBER, pageNo);
		}

		int recordsPerPage = Integer.parseInt(recordsPerPageStr);
		int pageNum = Integer.parseInt(pageNo);

		if (isCheckAllAcrossAllChecked != null && Constants.TRUE.equals(isCheckAllAcrossAllChecked))
		{
			final Integer totalRecords = (Integer) session.getAttribute(Constants.TOTAL_RESULTS);
			recordsPerPage = totalRecords;
			pageNum = 1;
		}

		final QuerySessionData querySessionData = (QuerySessionData) request.getSession()
				.getAttribute(Constants.QUERY_SESSION_DATA);
		final List<List<String>> dataList = AppUtility.getPaginationDataList(request, this
				.getSessionData(request), recordsPerPage, pageNum, querySessionData);

		return dataList;
	}

	/**
	 * @param dataList list of cart records.
	 * @param indexArray array of indexes of new attributes.
	 * @return List < List < String >> : List
	 */
	private List<List<String>> getManipulatedDataList(List<List<String>> dataList, int[] indexArray)
	{
		final List<List<String>> tempDataList = new ArrayList<List<String>>();
		final int size = dataList.size();
		final int len = indexArray.length;
		List<String> oldReord;
		String[] newRecordArray;
		List<String> newRecord;
		for (int recordIndex = 0; recordIndex < size; recordIndex++)
		{
			oldReord = dataList.get(recordIndex);
			newRecordArray = new String[len];

			for (int i = 0; i < len; i++)
			{
				newRecordArray[indexArray[i]] = oldReord.get(i);
			}
			newRecord = Arrays.asList(newRecordArray);
			tempDataList.add(newRecord);
		}
		return tempDataList;
	}

	/**
	 * @param request HttpServletRequest.
	 */
	private void addDifferentCartViewError(HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final ActionError error = new ActionError("shoppingcart.differentViewError");
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		this.saveErrors(request, errors);
		//String target = new String(Constants.DIFFERENT_VIEW_IN_CART);
	}

}
