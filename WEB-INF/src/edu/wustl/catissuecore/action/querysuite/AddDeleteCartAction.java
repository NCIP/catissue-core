package edu.wustl.catissuecore.action.querysuite;

import java.util.ArrayList;
import java.util.Arrays;
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
import edu.wustl.catissuecore.bizlogic.querysuite.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.querysuite.QueryModuleUtil;
import edu.wustl.common.dao.QuerySessionData;
import edu.wustl.common.querysuite.queryobject.impl.metadata.SelectedColumnsMetadata;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author santhoshkumar_c
 *
 */
public class AddDeleteCartAction extends QueryShoppingCartAction
{
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		HttpSession session = request.getSession();
		String target = null;
		String operation = request.getParameter(Constants.OPERATION);				

		// Extracting map from formbean which gives the serial numbers of selected rows
		String isCheckAllAcrossAllChecked = (String) request
				.getParameter(Constants.CHECK_ALL_ACROSS_ALL_PAGES);
		QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);
		
		//Get Attribute list and column list after define view.
		SelectedColumnsMetadata selectedColumnMetaData = (SelectedColumnsMetadata) session
				.getAttribute(Constants.SELECTED_COLUMN_META_DATA);

		//QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		List<AttributeInterface> attributeList = null;
		if (selectedColumnMetaData != null)
			attributeList = selectedColumnMetaData.getAttributeList();

		// Check if user wants to add in Shopping Cart.
		if (Constants.ADD.equals(operation))
		{
			target = add(request, searchForm, isCheckAllAcrossAllChecked, attributeList);

		}// Check if user wants to delete record from cart.
		else if (Constants.DELETE.equals(operation))
			target = delete(request, target, getCheckboxValues(searchForm));
		
        //sets the message in the session. This message is showed in popup on the result view page if the 
		//'id' attribute of the orderable entity is not included in view.
		String message;
		if (selectedColumnMetaData == null)			 
		message=null;			
		else
		message = QueryModuleUtil.getMessageIfIdNotPresentForOrderableEntities( selectedColumnMetaData,  cart);
		session.setAttribute(Constants.VALIDATION_MESSAGE_FOR_ORDERING, message);
		request.setAttribute(Constants.PAGEOF, Constants.PAGEOF_QUERY_MODULE);
		return mapping.findForward(target);
	}

	/**
	 * @param request
	 * @param searchForm
	 * @param isCheckAllAcrossAllChecked
	 * @param attributeList
	 * @return
	 * @throws DAOException
	 */
	private String add(HttpServletRequest request, AdvanceSearchForm searchForm, String isCheckAllAcrossAllChecked,
			List<AttributeInterface> attributeList) throws DAOException 
	{
				
		HttpSession session = request.getSession();
		QueryShoppingCart cart = (QueryShoppingCart) session
		.getAttribute(Constants.QUERY_SHOPPING_CART);
		
		String target;
		List<Integer> chkBoxValues;
		if (Constants.TRUE.equals(isCheckAllAcrossAllChecked))
			chkBoxValues = null;
		else 
			 chkBoxValues = getCheckboxValues(searchForm);
		target = addToCart(request, chkBoxValues, cart,
				attributeList);
		
		// if My List is not empty sets the value to false.
		request.getSession().setAttribute(Constants.IS_LIST_EMPTY, Constants.FALSE);
		return target;
	}

	/**
	 * @param request
	 * @param target
	 * @param chkBoxValues
	 * @return
	 */
	private String delete(HttpServletRequest request, String target,List<Integer> chkBoxValues) 
	{
		HttpSession session = request.getSession();
		QueryShoppingCart cart = (QueryShoppingCart) session
		.getAttribute(Constants.QUERY_SHOPPING_CART);		
			
		deleteFromCart(cart, chkBoxValues);					
			if (cart.getCart().size() == 0)
			{
				ActionErrors errors = new ActionErrors();
				ActionError error = new ActionError("ShoppingCart.emptyCartTitle");
				errors.add(ActionErrors.GLOBAL_ERROR, error);
				saveErrors(request, errors);
				// if My List is empty sets the value to true.
				request.getSession().setAttribute(Constants.IS_LIST_EMPTY, Constants.TRUE);
			}
			// set the options 
			setCartView(request, cart);
			target = new String(Constants.SHOPPING_CART_DELETE);
		
		return target;
	}

	/**
	 * @param request
	 * @param chkBoxValues
	 * @param cart
	 * @param attributeList
	 * @return
	 * @throws DAOException
	 */
	private String addToCart(HttpServletRequest request, List<Integer> chkBoxValues,
			QueryShoppingCart cart,
			List<AttributeInterface> attributeList) throws DAOException
	{
		String target;				
		List<List<String>> dataList = getPaginationDataList(request);
		HttpSession session = request.getSession();
		List<String> columnList = (List<String>) session.getAttribute(Constants.SPREADSHEET_COLUMN_LIST);

		//if cart is not present in session create new cart object
		if (cart == null || cart.isEmpty())
		{
			cart = new QueryShoppingCart();
			cart.setCartAttributeList(attributeList);
			cart.setColumnList(columnList);
			addDataToCart(cart, dataList, chkBoxValues, request, columnList);
		}
		else
		{
			List<AttributeInterface> oldAttributeList = cart.getCartAttributeList();
			QueryShoppingCartBizLogic queryShoppingCartBizLogic = new QueryShoppingCartBizLogic();
			int indexArray[] = queryShoppingCartBizLogic.getNewAttributeListIndexArray(oldAttributeList,
					attributeList);
			if (indexArray != null)
			{

				List<List<String>> tempdataList = getManipulatedDataList(dataList, indexArray);
				addDataToCart(cart, tempdataList, chkBoxValues, request, columnList);
			}
			else
			{
				addDifferentCartViewError(request);
			}
		}

		target = new String(Constants.SHOPPING_CART_ADD);
		request.setAttribute(Constants.PAGINATION_DATA_LIST, dataList);
		return target;
	}

	/**
	 * Add data in Cart .
	 * 
	 * @param cart a shopping cart object preset in session.
	 * @param dataList List of records. 
	 * @param request HttpServletRequest.
	 * @param response HttpServletResponse.
	 * @param chkBoxValues
	 * @param columnList
	*/
	public void addDataToCart(QueryShoppingCart cart, List<List<String>> dataList,
			List<Integer> chkBoxValues, HttpServletRequest request,
			List<String> columnList)
	{
		HttpSession session = request.getSession();
		QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		// Add a record to cart.
		int addRecordCount = bizLogic.add(cart, dataList, chkBoxValues);
		int duplicateRecordCount = 0;

		if (chkBoxValues != null)
			duplicateRecordCount = chkBoxValues.size() - addRecordCount;
		else
			duplicateRecordCount = dataList.size() - addRecordCount;
		//ActionErrors changed to ActionMessages
		ActionMessages messages = new ActionMessages();
		// Check if no. of duplicate records is not zero then set a error message.
		if (duplicateRecordCount != 0)
		{
			messages.add(ActionMessages.GLOBAL_MESSAGE,
					new ActionMessage("shoppingcart.duplicateObjError", addRecordCount,
					duplicateRecordCount));		
		}
		else
		{
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("shoppingCart.addMessage", addRecordCount));
			
			session.setAttribute(Constants.QUERY_SHOPPING_CART, cart);
			request.setAttribute(Constants.SPREADSHEET_COLUMN_LIST, columnList);
		}
		saveMessages(request, messages);
	}
	
	/**
	 * @param cart a shopping cart object preset in session.
	 * @param chkBoxValues
	 */
	public void deleteFromCart(QueryShoppingCart cart, List<Integer> chkBoxValues)
	{
		QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		bizLogic.delete(cart, chkBoxValues);

	}
		
	/**
	 * Get Pegination data list .
	 * @param request HttpServletRequest.
	*/
	public List<List<String>> getPaginationDataList(HttpServletRequest request)
			throws DAOException
	{
		HttpSession session = request.getSession();
		String pageNo = (String) request.getParameter(Constants.PAGE_NUMBER);
		//Integer.parseInt(XMLPropertyHandler.getValue(Constants.NO_OF_RECORDS_PER_PAGE));
		String recordsPerPageStr = (String) session.getAttribute(Constants.RESULTS_PER_PAGE);
		String isCheckAllAcrossAllChecked = (String) request
				.getParameter(Constants.CHECK_ALL_ACROSS_ALL_PAGES);

		if (pageNo != null)
		{
			request.setAttribute(Constants.PAGE_NUMBER, pageNo);
		}

		int recordsPerPage = Integer.parseInt(recordsPerPageStr);
		int pageNum = Integer.parseInt(pageNo);

		if (isCheckAllAcrossAllChecked != null
				&& Constants.TRUE.equals(isCheckAllAcrossAllChecked))
		{
			Integer totalRecords = (Integer) session.getAttribute(Constants.TOTAL_RESULTS);
			recordsPerPage = totalRecords;
			pageNum = 1;
		}

		QuerySessionData querySessionData = (QuerySessionData) request.getSession().getAttribute(
				Constants.QUERY_SESSION_DATA);
		List<List<String>> dataList = Utility.getPaginationDataList(request,
				getSessionData(request), recordsPerPage, pageNum, querySessionData);

		return dataList;
	}
	
	/**
	 * @param dataList list of cart records.
	 * @param indexArray array of indexes of new attributes.
	 * @return
	 */
	private List<List<String>> getManipulatedDataList(List<List<String>> dataList, int[] indexArray)
	{
		List<List<String>> tempDataList = new ArrayList<List<String>>();		
		int size=dataList.size();
		int len=indexArray.length;
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
		ActionErrors errors = new ActionErrors();
		ActionError error = new ActionError("shoppingcart.differentViewError");
		errors.add(ActionErrors.GLOBAL_ERROR, error);
		saveErrors(request, errors);
		//String target = new String(Constants.DIFFERENT_VIEW_IN_CART);
	}
	
}
